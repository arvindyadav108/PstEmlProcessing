package com.pack.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static synchronized void  createDirectoryIfNotExist(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }

    public static boolean isFileExists(String filePathString){
        if(StringUtil.isNotEmpty(filePathString)){
            return Files.exists(Paths.get(filePathString));
        }
        return false;
    }

    public static String getFileNameWithoutExtension(String fPath){
        Path p = Paths.get(fPath);
        String fname = p.getFileName().toString();
        int pos = fname.lastIndexOf(".");
        if (pos > 0) {
            fname = fname.substring(0, pos);
        }
        return fname;
    }
}
