package com.pack.utils;

public class StringUtil {

    public static boolean isNotEmpty(String str){
        if(null != str && str.trim().length() > 0){
            return true;
        }
        return false;
    }
}
