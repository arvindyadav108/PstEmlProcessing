package com.pack.parser;

import com.pack.model.Email;
import com.pack.utils.StringUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageTransportHeaderParser {

    private static final String headerRegex = "^[a-zA-Z]+[a-zA-Z-]*[:]";
    private static final Pattern pattern = Pattern.compile(headerRegex);

    public static Map<String, Map<String, Queue<Email>>> parseVer1(String str){
        Map<String, Map<String, Queue<Email>>> map = new HashMap<>();
        if (StringUtil.isNotEmpty(str)) {
            String[] splittedString = str.split("\\n");
            String toHeader = extractHeader("To:", splittedString);
            if (StringUtil.isNotEmpty(toHeader)) {
                map.put("TO", extractMailAddress(toHeader.substring("To:".length())));
            }
            String ccHeader = extractHeader("CC:", splittedString);
            if (StringUtil.isNotEmpty(ccHeader)) {
                map.put("CC", extractMailAddress(ccHeader.substring("CC:".length())));
            }
            String bccHeader = extractHeader("BCC:", splittedString);
            if (StringUtil.isNotEmpty(bccHeader)) {
                map.put("BCC", extractMailAddress(bccHeader.substring("BCC:".length())));
            }
        }
        return map;
    }

    private static String extractHeader(String headerName, String[] headerArray){
        boolean found = false;
        StringBuilder builder = new StringBuilder("");
        for(int i =0; i< headerArray.length && !found; i++ ){
            String line = headerArray[i];
            if(StringUtil.isNotEmpty(line) && line.startsWith(headerName)){
                found = true;
                builder.append(line);
                //start looking next line if it is part of the same header
                i++;
                while(i< headerArray.length && !lineStartWithHeader(headerArray[i])){
                    builder.append(headerArray[i]);
                    i++;
                }
            }

        }
        return builder.toString();
    }

    private static boolean lineStartWithHeader(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.start() == 0;
        }
        return false;
    }

    private static Map<String, Queue<Email>> extractMailAddress(String mailAddressString){
        Map<String, Queue<Email>> map = new HashMap<>();
        if(StringUtil.isNotEmpty(mailAddressString)){
            String[] arr = mailAddressString.trim().split(",");
            for(int i=0;i<arr.length; i++){
                String id = arr[i];
                int start = id.indexOf('<');
                int end = id.lastIndexOf('>');
                String displayName = id.substring(0, start).trim().replaceAll("\\n", "");
                String mailId = id.substring(start+1, end).trim().replaceAll("\\n", "");
                Email emailAddress = new Email(displayName, mailId);
                if(!(map.containsKey(displayName))){
                    Queue<Email> mailIdQueue = new LinkedList();
                    mailIdQueue.add(emailAddress);
                    map.put(displayName, mailIdQueue);
                }else{
                    map.get(displayName).add(emailAddress);
                }
            }
        }
        return map;
    }
}
