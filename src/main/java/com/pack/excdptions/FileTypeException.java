package com.pack.excdptions;

public class FileTypeException extends RuntimeException{

    public FileTypeException(String msg, Throwable ex){
        super(msg);
    }

}
