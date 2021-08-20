package com.pack.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testStringEmpty(){
        String emptyString = "";
        Assert.assertFalse(StringUtil.isNotEmpty(emptyString));
        String input ="some string";
        Assert.assertTrue(StringUtil.isNotEmpty(input));
    }
}