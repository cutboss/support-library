/*
 * Copyright (C) 2018 CUTBOSS
 */

package cutboss.support.string;

import java.io.UnsupportedEncodingException;

/**
 * StringUtils.
 *
 * @author CUTBOSS
 */
public class StringUtils {
    /**
     *
     *
     * @param cs CharSequence
     * @return is empty
     */
    public static boolean isEmpty(CharSequence cs) {
        return ((null == cs) || ("".equals(cs)));
    }

    /**
     *
     *
     * @param s
     * @return
     */
    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Count bytes of text.
     *
     * @param text Text
     * @param charsetName The name of a supported charset
     * @return Count result of bytes
     */
    public static int countBytes(String text, String charsetName) {
        if (null == text) {
            return 0;
        }
        try {
            return text.getBytes(charsetName).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
