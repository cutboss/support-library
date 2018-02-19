/*
 * Copyright (C) 2018 CUTBOSS
 */

package cutboss.support.string;

import android.util.Log;

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
     * @param text
     * @param charset
     * @return Bytes
     */
    public static int getByteLength(String text, String charset) {
        int length = 0;
        if (null == text) {
            return length;
        }
        try {
            length = text.getBytes(charset).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return length;
    }
}
