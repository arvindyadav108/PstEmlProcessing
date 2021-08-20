package com.pack.parser;

import org.junit.Assert;
import org.junit.Test;

public class RtfParserTest {

    @Test
    public void testRtfParsing(){
        String htmlText = RtfParser.parse(getRTFString());
        Assert.assertNotNull(htmlText);
    }

    private String getRTFString(){
        return "{\\rtf1\\mac\\deff2 {\\fonttbl{\\f0\\fswiss Chicago;}{\\f2\\froman New York;}{\\f3\\fswiss Geneva;}{\\f4\\fmodern Monaco;}{\\f11\\fnil Cairo;}{\\f13\\fnil Zapf Dingbats;}{\\f16\\fnil Palatino;}{\\f18\\fnil Zapf Chancery;}{\\f20\\froman Times;}{\\f21\\fswiss Helvetica;}\n" +
                "{\\f22\\fmodern Courier;}{\\f23\\ftech Symbol;}{\\f24\\fnil Mobile;}{\\f100\\fnil FoxFont;}{\\f107\\fnil MathMeteor;}{\\f164\\fnil Futura;}{\\f1024\\fnil American Heritage;}{\\f2001\\fnil Arial;}{\\f2005\\fnil Courier New;}{\\f2010\\fnil Times New Roman;}\n" +
                "{\\f2011\\fnil Wingdings;}{\\f2515\\fnil MT Extra;}{\\f3409\\fnil FoxPrint;}{\\f11132\\fnil InsigniaLQmono;}{\\f11133\\fnil InsigniaLQprop;}{\\f14974\\fnil LB Helvetica Black;}{\\f14976\\fnil L Helvetica Light;}}{\\colortbl\\red0\\green0\\blue0;\\red0\\green0\\blue255;\n" +
                "\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;}{\\stylesheet{\\f4\\fs18 \\sbasedon222\\snext0 Normal;}}{\\info{\\title samplepostscript.msw}{\\author \n" +
                "Computer Science Department}}\\widowctrl\\ftnbj \\sectd \\sbknone\\linemod0\\linex0\\cols1\\endnhere \\pard\\plain \\qc \\f4\\fs18 {\\plain \\b\\f21 Sample Rich Text Format Document\\par \n" +
                "}\\pard {\\plain \\f20 \\par \n" +
                "}\\pard \\ri-80\\sl-720\\keep\\keepn\\absw570 {\\caps\\f20\\fs92\\dn6 T}{\\plain \\f20 \\par \n" +
                "}\\pard \\qj {\\plain \\f20 his is a sample rich text format (RTF), document. This document was created using Microsoft Word and then printing the document to a RTF file. It illustrates the very basic text formatting effects that can be achieved using RTF. \n" +
                "\\par \n" +
                "\\par \n" +
                "}\\pard \\qj\\li1440\\ri1440\\box\\brdrs \\shading1000 {\\plain \\f20 RTF }{\\plain \\b\\f20 contains codes for producing advanced editing effects. Such as this indented, boxed, grayed background, entirely boldfaced paragraph.\\par \n" +
                "}\\pard \\qj {\\plain \\f20 \\par \n" +
                "Microsoft  Word developed RTF for document transportability and gives a user access to the complete set of the effects that can be achieved using RTF. \\par \n" +
                "}}";
    }
}