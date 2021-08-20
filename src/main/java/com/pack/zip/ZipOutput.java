package com.pack.zip;

import com.pack.processor.impl.PstProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipOutput {

    private final Logger logger = LoggerFactory.getLogger(ZipOutput.class);
    private long THRESHOLD_SIZE_BYTES ;
    private int ZIP_COUNT = -1;
    String outputZipFolder;
    private long currentFileSize = 0;
    private static final String ZIP_EXTENSION = ".zip";
    private static final String separator = File.separator;

    FileOutputStream fout = null;
    ZipOutputStream zout = null;

    public ZipOutput(String outputPath, long THRESHOLD_SIZE){
        this.outputZipFolder = outputPath;
        this.THRESHOLD_SIZE_BYTES = THRESHOLD_SIZE;
    }

    public void write(String filePath, String fileName) throws IOException {

        if(!(filePath.endsWith(separator))){
            filePath= filePath+separator;
        }
        File file = new File(filePath+fileName);
        long fileLength = file.length();

        if(fileLength > THRESHOLD_SIZE_BYTES){
            flushCurrentFile();
            addFileToZip(file, fileName);
            flushCurrentFile();
            currentFileSize = 0;
        }else{
            if(currentFileSize+fileLength <= THRESHOLD_SIZE_BYTES){
                addFileToZip(file, fileName);
                currentFileSize = currentFileSize+ fileLength;
            }else{
                flushCurrentFile();
                addFileToZip(file, fileName);
                currentFileSize = fileLength;
            }
        }
    }

    private void addFileToZip(File file, String fileName) throws IOException {
        String separator = File.separator;
        if(null == fout){
            ZIP_COUNT++;
            initializeOutputStream(outputZipFolder+separator+"output_"+ZIP_COUNT+ZIP_EXTENSION);
        }
        ZipEntry zipEntry = new ZipEntry(fileName);
        byte[] buffer = new byte[1024];
        try {
            zout.putNextEntry(zipEntry);
            FileInputStream fileInputStream = new FileInputStream(file);
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zout.write(buffer, 0, length);
            }
            zout.closeEntry();
            fileInputStream.close();
        } catch (IOException e) {
            logger.error("IO Exception ", e);
            throw e;
        }

    }

    private void initializeOutputStream(String path) throws FileNotFoundException {
        try {
            fout = new FileOutputStream(path);
            zout = new ZipOutputStream(fout);
        } catch (FileNotFoundException e) {
            logger.error("Zip location not found.", e);
            throw e;
        }
    }

    private void flushCurrentFile() throws IOException {
        if(null != zout){
            try {
                fout.flush();
                zout.flush();
                zout.close();
                fout.close();
            } catch (IOException e) {
                logger.error("Resource cleanup error.");
                throw e;
            }
        }
        zout = null;
        fout = null;
    }

    public void resourceCleanUp() throws IOException {
        this.flushCurrentFile();
    }
}
