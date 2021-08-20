package com.pack.parser;

import com.pack.utils.StringUtil;
import org.bbottema.rtftohtml.RTF2HTMLConverter;
import org.bbottema.rtftohtml.impl.RTF2HTMLConverterRFCCompliant;

public class RtfParser {

    public static String parse(String rtf) {
        return parseRtfWithOpenSource(rtf);
    }

    private static String parseRtfWithOpenSource(String input){
        RTF2HTMLConverter converter = RTF2HTMLConverterRFCCompliant.INSTANCE;
        String output = converter.rtf2html(input);
        try {

            if (StringUtil.isNotEmpty(output)) {
                if(output.contains("<html")){
                    output = output.substring(output.indexOf("<html"));
                }
                //output = output.substring(output.indexOf("<html"));
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return output;
    }
}
