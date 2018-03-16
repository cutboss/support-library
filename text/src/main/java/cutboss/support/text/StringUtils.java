/*
 * Copyright (C) 2018 CUTBOSS
 */

package cutboss.support.text;

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

    /**
     * Get the sentence of text.
     *
     * @param text Text
     * @param maxLength
     * @return Sentence
     */
    public static String getSentence(String text, int maxLength) {
        // invalid?
        if ((null == text) || "".equals(text)) {
            return "";
        }
        if (1 > maxLength) {
            return "";
        }

        // get paragraph
        String[] paragraph = text.split("[\r\n]");
        for (String sentence : paragraph) {
            // empty?
            if ("".equals(sentence)) {
                // skip
                continue;
            }
            if (maxLength < sentence.length()) {
                return sentence.substring(0, maxLength);
            }
            return sentence;
        }
        return "";
    }

    /**
     *
     *
     * @param text Text
     * @return
     */
    public static String removeLineBreaks(String text) {
        if (null == text) {
            return null;
        }
        if ("".equals(text)) {
            return "";
        }
        return text.replaceAll("[\r\n]", "");
    }

    /**
     *
     *
     * @param text
     * @return
     */
    public static String removeSpaces(String text) {
        if (null == text) {
            return null;
        }
        if ("".equals(text)) {
            return "";
        }
        return text.replaceAll("[\\u00A0\\u0020\\u3000\t]", "");
    }

    /**
     *
     *
     * @param text
     * @return
     */
    public static String removeWhitespace(String text) {
        if (null == text) {
            return null;
        }
        if ("".equals(text)) {
            return "";
        }
        return text.replaceAll("[\\u00A0\\u0020\\u3000\t\r\n]", "");
    }
}
