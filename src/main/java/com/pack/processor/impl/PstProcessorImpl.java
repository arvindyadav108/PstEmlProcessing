package com.pack.processor.impl;

import com.pack.context.PSTProcessContext;
import com.pack.excdptions.InputDataValidationException;
import com.pack.model.MailAttachment;
import com.pack.processor.PstProcessor;

import com.pack.utils.FileUtil;
import com.pack.parser.RtfParser;
import com.pack.utils.MessageUtil;
import com.pack.utils.StringUtil;
import com.pack.zip.ZipOutput;
import com.pff.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.URLConnection;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PstProcessorImpl implements PstProcessor {
    private final Logger logger = LoggerFactory.getLogger(PstProcessorImpl.class);
    private static final String EML_EXTENSION = ".eml";
    private static final String HYPHEN = "_";
    private int count = 0;
    private static final String separator = File.separator;
    private static final String OUTPUT_EMP_PATH = "outputEml";
    private static final String TMP_PATH = "tmpLocation";
    private static final String ZIP_OUTPUT_PATH = "zipOutputPath";

    @Override
    public void process(String pstFilePath, String outputDir, long thresholdSize, boolean zipRequired, String processId) throws IOException, MessagingException, PSTException {

        if(!FileUtil.isFileExists(pstFilePath)){
            throw new FileNotFoundException("PST file not found");
        }
        if(!StringUtil.isNotEmpty(processId)){
            throw new InputDataValidationException("Process ID Missing");
        }
        PSTProcessContext pstProcessContext = initProcess(outputDir, thresholdSize, zipRequired, processId);
        processPstFile(pstFilePath, pstProcessContext);
    }

    @Override
    public void process(String pstFilePath, String outputDir, long thresholdSize, String processId) throws MessagingException, PSTException, IOException {
        this.process(pstFilePath, outputDir, thresholdSize, true, processId );
    }

    private void processPstFile(String pstFilePath, PSTProcessContext pstContext) throws PSTException, IOException, MessagingException {
        logger.info("PST processing started...");
        ZipOutput zipOutput = new ZipOutput(pstContext.getZipOutputPath(), pstContext.getZipThresholdSize());
        try {
            PSTFile pstFile = new PSTFile(pstFilePath);
            extractMessages(pstFile, FileUtil.getFileNameWithoutExtension(pstFilePath), pstContext, zipOutput);
        } catch (PSTException ex) {
            logger.error("PST Exception", ex);
            throw ex;
        } catch (IOException ex) {
            logger.error("IO Exception", ex);
            throw ex;
        } catch (MessagingException ex) {
            logger.error("Messaging Exception", ex);
            throw ex;
        }
        finally {
            zipOutput.resourceCleanUp();
        }
    }

    private void extractMessages(PSTFile pstFile, String fName, PSTProcessContext pstProcessContext, ZipOutput zipOutput) throws PSTException, IOException, MessagingException {
        Queue<PSTFolder> folderQueue = new LinkedList();
        folderQueue.add(pstFile.getRootFolder());

        while(!(folderQueue.isEmpty())){
            PSTFolder pstFolder = folderQueue.poll();
            // process the messages of pst folder
            if (pstFolder.getContentCount() > 0) {
                PSTMessage email = (PSTMessage)pstFolder.getNextChild();

                while (email != null) {
                    processMail(email, pstFile, fName, false, pstProcessContext, zipOutput);
                    email = (PSTMessage)pstFolder.getNextChild();
                }
            }
            //add all sub folders to queue
            if (pstFolder.hasSubfolders()) {
                Vector<PSTFolder> childFolders = pstFolder.getSubFolders();
                for (PSTFolder childFolder : childFolders) {
                    folderQueue.add(childFolder);
                }
            }
        }
    }

    private void processMail(PSTMessage email, PSTFile pstFile, String fName, boolean isAttchment, PSTProcessContext pstProcessContext, ZipOutput zipOutput) throws IOException, MessagingException, PSTException {

        String outputEmlFolder = pstProcessContext.getEmlOutputPath();
        String tmpFolder = pstProcessContext.getTmpFilePath();
        Message msg = new MimeMessage(getSession());
        // set priority
        msg.setHeader( "X-Priority", String.valueOf(email.getPriority()));
        msg.setHeader( "Importance", String.valueOf(email.getSensitivity()));

        msg.setSubject(getSubject(email));
        msg.setFrom(MessageUtil.getFromAddress(email));
        MessageUtil.setRecipients(email, msg);
        msg.setSentDate(email.getMessageDeliveryTime());

        Multipart mailMp = addMailBodyPart(email);
        MimeBodyPart mailMpContainer = new MimeBodyPart();
        mailMpContainer.setContent(mailMp);

        MimeMultipart rootMp = new MimeMultipart("mixed");
        rootMp.addBodyPart(mailMpContainer);

        addAttachments(email, rootMp, pstFile, pstProcessContext, zipOutput);

        msg.setContent(rootMp);
        msg.saveChanges();
        if(!isAttchment){
            try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(outputEmlFolder+"\\"+fName+HYPHEN+count+EML_EXTENSION)))){
                msg.writeTo(bos);
                zipOutput.write(outputEmlFolder+separator, fName+HYPHEN+count+EML_EXTENSION);
                count++;
            }
        }else{
            try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(tmpFolder+"\\"+fName+EML_EXTENSION)))){
                msg.writeTo(bos);
            }
        }
    }

    private MimeMultipart addMailBodyPart(PSTMessage email) throws MessagingException, PSTException, IOException {
        //set message body...
        MimeMultipart mailMp = new MimeMultipart("related");
        MimeMultipart messageBodyMp = new MimeMultipart("alternative");
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        MimeBodyPart plainTextBodyPart = new MimeBodyPart();
        String htmlText = email.getBodyHTML();
        if(!(StringUtil.isNotEmpty(htmlText))){
            htmlText = RtfParser.parse(email.getRTFBody());
        }
        if(StringUtil.isNotEmpty(htmlText)){
            htmlBodyPart.setContent(htmlText, "text/html; charset=utf-8");
            messageBodyMp.addBodyPart(htmlBodyPart);
            List<String> cids = MessageUtil.extractContentIds(htmlText);
            addBodyAttachments(email, mailMp, cids);
        }
        if(null != email.getBody()){
            plainTextBodyPart.setContent(email.getBody(),"text/plain; charset=utf-8" );
            messageBodyMp.addBodyPart(plainTextBodyPart);
        }
        // add messageBodyMP to mailMP
        MimeBodyPart mailBodyContainerBodyPart = new MimeBodyPart();
        mailBodyContainerBodyPart.setContent(messageBodyMp);
        mailMp.addBodyPart(mailBodyContainerBodyPart);
        return mailMp;
    }

    private String getSubject(PSTMessage email){
        return email.getSubject();
    }

    private void addAttachments(PSTMessage email, MimeMultipart multipart, PSTFile pstFile, PSTProcessContext pstProcessContext, ZipOutput zipOutput) throws PSTException, IOException, MessagingException {

        String tmpFolder = pstProcessContext.getTmpFilePath();
        int numberOfAttachments = email.getNumberOfAttachments();
        for (int x = 0; x < numberOfAttachments; x++) {
            PSTAttachment attachment = email.getAttachment(x);
            boolean isBodyAttachment = attachment.isAttachmentMhtmlRef();//attachment.getBooleanItem(32766);
            if(attachment.getAttachMethod() == 5){
                // It is attached PSTMessage
                String fileName = getAttachmentFileName(attachment).replaceAll("[^a-zA-Z0-9]", "");//attachment.getStringItem(14084).replaceAll("[^a-zA-Z0-9]", "");;
                processMail(attachment.getEmbeddedPSTMessage(), pstFile , fileName , true, pstProcessContext, zipOutput);

                MimetypesFileTypeMap fileTypeMap = (MimetypesFileTypeMap) FileTypeMap.getDefaultFileTypeMap();
                fileTypeMap.addMimeTypes("message/rfc822 eml");
                MailAttachment.MailAttachmentBuilder builder = new MailAttachment.MailAttachmentBuilder();

                FileDataSource fds = new FileDataSource(tmpFolder + separator + fileName+EML_EXTENSION);
                fds.setFileTypeMap(fileTypeMap);

                builder.setDataSource(fds);
                builder.setName(fileName+EML_EXTENSION);
                MimeBodyPart mimeBodyPart =  MessageUtil.addAttachmentToBodyPart(builder.build());
                multipart.addBodyPart(mimeBodyPart);
            }else {
                if (!isBodyAttachment) {
                    String fileName = getAttachmentFileName(attachment);//attachment.getStringItem(14084);
                    //String fileType = attachment.getStringItem(14094);
                    String contentType = URLConnection.guessContentTypeFromName(fileName);
                    MailAttachment.MailAttachmentBuilder builder = new MailAttachment.MailAttachmentBuilder();
                    if(StringUtil.isNotEmpty(attachment.getMimeTag())){
                        builder.setDataSource(new ByteArrayDataSource(attachment.getFileInputStream(), attachment.getMimeTag()));
                    }else{
                        if(StringUtil.isNotEmpty(contentType)){
                            builder.setDataSource(new ByteArrayDataSource(attachment.getFileInputStream(), contentType));
                        }else {
                            builder.setDataSource(new ByteArrayDataSource(attachment.getFileInputStream(), "application/octet-stream"));
                        }
                    }
                    builder.setDisposition("attachment");
                    builder.setName(MimeUtility.encodeText(fileName));
                    MimeBodyPart mimeBodyPart = MessageUtil.addAttachmentToBodyPart(builder.build());
                    multipart.addBodyPart(mimeBodyPart);
                }
            }
        }
    }

    private void addBodyAttachments(PSTMessage email, MimeMultipart multipart, List<String> cids) throws PSTException, IOException, MessagingException {

        int numberOfAttachments = email.getNumberOfAttachments();
        for (int x = 0; x < numberOfAttachments; x++) {
            PSTAttachment attachment = email.getAttachment(x);
            boolean isBodyAttachment = attachment.isAttachmentMhtmlRef();//attachment.getBooleanItem(32766);
            if(isBodyAttachment && cids.contains(attachment.getContentId())){
                MailAttachment.MailAttachmentBuilder attachmentBuilder = new MailAttachment.MailAttachmentBuilder();
                if (attachment.getFileInputStream() != null) {
                    if (attachment.getMimeTag() != null && attachment.getMimeTag().length() > 0) {
                        attachmentBuilder.setDataSource(new ByteArrayDataSource(attachment.getFileInputStream(), attachment.getMimeTag()));
                    } else {
                        attachmentBuilder.setDataSource(new ByteArrayDataSource(attachment.getFileInputStream(), "application/octet-stream"));
                    }
                    attachmentBuilder.setContentId(attachment.getContentId());
                    attachmentBuilder.setName(getAttachmentFileName(attachment));
                    MimeBodyPart mimeBodyPart = MessageUtil.addAttachmentToBodyPart(attachmentBuilder.build());
                    multipart.addBodyPart(mimeBodyPart);
                }
            }
        }

    }

    private String getAttachmentFileName(PSTAttachment attachment){
        String fileName = "";
        if (attachment.getLongFilename() != null && attachment.getLongFilename().length() > 0) {
            fileName = attachment.getLongFilename();
        } else if (attachment.getDisplayName() != null && attachment.getDisplayName().length() > 0) {
            fileName = attachment.getDisplayName();
        } else if (attachment.getFilename() != null && attachment.getFilename().length() > 0) {
            fileName = attachment.getFilename();
        }
        return fileName;
    }

    private PSTProcessContext initProcess(String outputDir, long thresholdSize, boolean zipRequired, String processId) throws IOException {
        //TODO: validate the parameters like outpurDir , thresholdsize should not empty and valid
        String emlOpDir = outputDir+separator+processId+separator+OUTPUT_EMP_PATH;
        String tmpDir = outputDir+separator+processId+separator+TMP_PATH;
        String outputZipDir = outputDir+separator+processId+ separator+ZIP_OUTPUT_PATH;

        FileUtil.createDirectoryIfNotExist(emlOpDir);
        FileUtil.createDirectoryIfNotExist(tmpDir);
        FileUtil.createDirectoryIfNotExist(outputZipDir);

        PSTProcessContext pstContext = new PSTProcessContext.PSTProcessContextBuilder()
                .setProcessId(processId)
                .setEmlOutputPath(emlOpDir)
                .setTmpFilePath(tmpDir)
                .setZipOutputPath(outputZipDir)
                .setZipEml(true).setZipThresholdSize(thresholdSize)
                .build();

        return pstContext;
    }

    private Session getSession(){
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "false");
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);
        return session;
    }

}
