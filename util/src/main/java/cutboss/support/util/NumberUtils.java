/*
 * Copyright (C) 2017-2018 CUTBOSS
 */

package cutboss.support.util;

import java.util.Locale;

/**
 * NumberUtils.
 *
 * @author CUTBOSS
 */
public class NumberUtils {
    /**
     *
     *
     * @param s
     * @return
     */
    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     *
     * @param digits Digits
     * @param integer Integer
     * @return
     */
    public static String zeroPadding(int digits, int integer) {
        return String.format(Locale.getDefault(), ("%0" + digits + "d"), integer);
    }
}
