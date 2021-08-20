package com.pack;

import com.pack.processor.PstProcessor;
import com.pack.processor.impl.PstProcessorImpl;
import com.pff.PSTException;

import javax.mail.MessagingException;
import java.io.IOException;

public class TestApplication {
    public static void main(String[] args) throws MessagingException, PSTException, IOException {

        PstProcessor pstProcessor= new PstProcessorImpl();
        String pstFile = "backup.pst";
        long thresholdSize = 4*1048576;//2097152;//262144;//1048576;
        pstProcessor.process("D:\\psttoeml\\inputPst\\"+pstFile, "D:\\psttoeml", thresholdSize, "pr003");
        System.out.println("PROCESS COMPLETED.");
    }
}
