package com.pack.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public class FileUtilTest {

    String separator = File.separator;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void testFetchFileNameWithPathExtension(){
        String filePath = "folder1"+separator+"folder2"+separator+"abc.xml";
        Assert.assertEquals("abc", FileUtil.getFileNameWithoutExtension(filePath));
    }

    @Test
    public void testFetchFileNameWithoutPathExtension(){
        String filePath = "folder1"+separator+"folder2"+separator+"abc";
        Assert.assertEquals("abc", FileUtil.getFileNameWithoutExtension(filePath));
    }

    @Test
    public void testFileExist(){
        Assert.assertTrue(FileUtil.isFileExists(folder.getRoot().getPath()));
    }

    @Test
    public void testFileNotExist(){
        Assert.assertFalse(FileUtil.isFileExists(folder.getRoot().getPath() + separator+"folder1"));
    }

}