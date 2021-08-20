package com.pack.processor;

import com.pff.PSTException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface PstProcessor {
    void process(String pstFilePath, String outputDir, long thresholdSize, boolean zipRequired, String processId) throws IOException, MessagingException, PSTException;

    void process(String pstFilePath, String outputDir, long thresholdSize, String processId) throws MessagingException, PSTException, IOException;
}
