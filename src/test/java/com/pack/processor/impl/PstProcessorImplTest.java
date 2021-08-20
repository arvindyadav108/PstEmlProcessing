package com.pack.processor.impl;

import com.pack.processor.PstProcessor;
import com.pff.PSTException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;


public class PstProcessorImplTest {

    String separator = File.separator;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();


    @Test
    public void test() throws MessagingException, PSTException, IOException {

        String pstFileName = "Sample002.pst";
        String processId = "tmp01";
        long thresholdSize = 4*1048576;
        String outputDir = folder.getRoot().getPath();
        Path resourceDirectory = Paths.get("src","test","resources", pstFileName);
        String pstPath = resourceDirectory.toFile().getAbsolutePath();
        PstProcessor pstProcessor= new PstProcessorImpl();
        pstProcessor.process(pstPath, outputDir,thresholdSize,  true, processId);
        int count = new File(outputDir+separator+processId).list().length;
        assertEquals(count, 3);

    }
}