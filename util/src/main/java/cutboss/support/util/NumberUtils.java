/*
 * Copyright (C) 2017-2018 CUTBOSS
 */

package cutboss.support.util;

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
}
