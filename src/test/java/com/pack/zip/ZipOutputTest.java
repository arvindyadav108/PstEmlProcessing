package com.pack.zip;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class ZipOutputTest {

    String separator = File.separator;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void testZipOutput() throws IOException {
        String sampleFile = "sample.txt";
        long thresholdSize = 4*1048576;
        String zipOutputPath = folder.getRoot().getPath()+separator+"output";
        Files.createDirectories(Paths.get(zipOutputPath));
        Path resourceDirectory = Paths.get("src","test","resources");
        ZipOutput zipOutput = new ZipOutput(zipOutputPath,thresholdSize);

        zipOutput.write(resourceDirectory.toFile().getAbsolutePath(),sampleFile );
        // write completed so need to do resource cleanup
        zipOutput.resourceCleanUp();
        int count = new File(zipOutputPath).list().length;
        assertEquals(count, 1);
    }

    @Test(expected=IOException.class)
    public void testZipOutputExpectedException() throws IOException {
        String sampleFile = "notfound.txt";
        long thresholdSize = 4*1048576;
        String zipOutputPath = folder.getRoot().getPath()+separator+"output";
        Files.createDirectories(Paths.get(zipOutputPath));
        Path resourceDirectory = Paths.get("src","test","resources");
        ZipOutput zipOutput = new ZipOutput(zipOutputPath,thresholdSize);

        zipOutput.write(resourceDirectory.toFile().getAbsolutePath(),sampleFile );
        // write completed so need to do resource cleanup
        zipOutput.resourceCleanUp();
    }

}