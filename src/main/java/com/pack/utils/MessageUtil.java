package com.pack.utils;

import com.pack.model.Email;
import com.pack.model.MailAttachment;
import com.pack.parser.MessageTransportHeaderParser;
import com.pff.PSTException;
import com.pff.PSTMessage;
import com.pff.PSTRecipient;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.pack.constants.Constants.RECIPIENT_TYPE_CC;
import static com.pack.constants.Constants.RECIPIENT_TYPE_TO;

public class MessageUtil {

    public static void setRecipients(PSTMessage message, Message msg) throws PSTException, IOException, MessagingException {
        List<Address> toAddresses = new ArrayList();
        List<Address> ccAddress = new ArrayList();

        int numberOfRecipient = message.getNumberOfRecipients();
        if(numberOfRecipient >0) {
            if (!(StringUtil.isNotEmpty(message.getTransportMessageHeaders()))) {
                // means it is sent message so iterate over all the recipient and get mail address...
                for (int i = 0; i < message.getNumberOfRecipients(); i++) {
                    PSTRecipient pstRecipient = message.getRecipient(i);

                    String receiverName = pstRecipient.getDisplayName();
                    String receiverEmail = pstRecipient.getEmailAddress();
                    if(StringUtil.isNotEmpty(pstRecipient.getSmtpAddress())){
                        receiverEmail = pstRecipient.getSmtpAddress();
                    }
                    Address address = new InternetAddress(receiverEmail, receiverName);
                    if(RECIPIENT_TYPE_TO == pstRecipient.getRecipientType()){
                        toAddresses.add(address);
                    }else if(RECIPIENT_TYPE_CC == pstRecipient.getRecipientType()){
                        ccAddress.add(address);
                    }
                }

            } else if (numberOfRecipient == 1 && StringUtil.isNotEmpty(message.getReceivedRepresentingSMTPAddress())) {
                // message send to itself then ...
                PSTRecipient pstRecipient = message.getRecipient(0);
                String receiverName = pstRecipient.getDisplayName();
                String receiverEmail = message.getReceivedRepresentingSMTPAddress();
                Address address = new InternetAddress(receiverEmail, receiverName);
                toAddresses.add(address);
            } else {
                Map<String, Map<String, Queue<Email>>> transportHeaderMap = MessageTransportHeaderParser.parseVer1(message.getTransportMessageHeaders());

                Map<String, Queue<Email>> toRecipient = transportHeaderMap.get("TO");
                if(null != toRecipient && toRecipient.size()>0){
                    for(Map.Entry<String, Queue<Email>> entry: toRecipient.entrySet()) {
                        for (Email mailAddress : entry.getValue()) {
                            toAddresses.add(new InternetAddress(mailAddress.getAddress(), mailAddress.getName()));
                        }
                    }
                }

                Map<String, Queue<Email>> ccRecipient = transportHeaderMap.get("CC");
                if(null != ccRecipient && ccRecipient.size()>0){
                    for(Map.Entry<String, Queue<Email>> entry: ccRecipient.entrySet()) {
                        for (Email mailAddress : entry.getValue()) {
                            ccAddress.add(new InternetAddress(mailAddress.getAddress(), mailAddress.getName()));
                        }
                    }
                }
            }
        }

        if(toAddresses.size()>0) {
            Address[] arr = new Address[toAddresses.size()];
            msg.setRecipients(Message.RecipientType.TO, toAddresses.toArray(arr));
        }
        if(ccAddress.size()>0) {
            Address[] arr = new Address[ccAddress.size()];
            msg.setRecipients(Message.RecipientType.CC, ccAddress.toArray(arr));
        }
    }

    public static Address getFromAddress(PSTMessage message) throws UnsupportedEncodingException {
        String senderEmailAddress = "";
        String senderName = "";

        if("SMTP".equalsIgnoreCase(message.getSenderAddrtype())){
            senderEmailAddress = message.getSenderEmailAddress();
        }else{
            senderEmailAddress = message.getSentRepresentingSMTPAddress();
            if(!(StringUtil.isNotEmpty(senderEmailAddress))){
                senderEmailAddress = message.getStringItem(23818);
            }
            if(!(StringUtil.isNotEmpty(senderEmailAddress))){
                senderEmailAddress = message.getSenderEmailAddress();
            }
        }
        senderName = message.getSenderName();
        Address from_address = new InternetAddress(senderEmailAddress, senderName);

        return from_address;
    }

    public static MimeBodyPart addAttachmentToBodyPart(MailAttachment attachment) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        if(null != attachment.getDataSource()) {
            messageBodyPart.setDataHandler(new DataHandler(attachment.getDataSource()));
        }
        if(null != attachment.getName()) {
            messageBodyPart.setFileName(attachment.getName());
        }
        if(null != attachment.getContentId()) {
            messageBodyPart.setContentID(attachment.getContentId());
        }
        if(null != attachment.getDisposition()){
            messageBodyPart.setDisposition(attachment.getDisposition());
        }
        if(null != attachment.getDescription()){
            messageBodyPart.setDescription(attachment.getDescription());
        }
        return messageBodyPart;
    }

    public static List<String> extractContentIds(String htmlBody){
        String[] a = htmlBody.split("src=\"cid:");
        List<String> cids = new ArrayList();
        for(int i = 1;i<a.length;i++){
            String st = a[i];
            String cid = st.substring(0, st.indexOf("\""));
            cids.add(cid);
        }
        return cids;
    }
}
