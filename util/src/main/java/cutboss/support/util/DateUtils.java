/*
 * Copyright (C) 2018 CUTBOSS
 */

package cutboss.support.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * DateUtils.
 *
 * @author CUTBOSS
 */
public class DateUtils {
    /**
     * Formats an object to produce a string.
     *
     * @param obj The object to format
     * @return Formatted string
     */
    public static String formatDate(Object obj) {
        return formatDateTime(obj, true, false, false);
    }

    /**
     *
     *
     * @param obj
     * @param day
     * @return
     */
    public static String formatDate(Object obj, boolean day) {
        return formatDateTime(obj, true, day, false);
    }

    /**
     *
     *
     * @param obj
     * @return
     */
    public static String formatDateTime(Object obj) {
        return formatDateTime(obj, true, false, true);
    }

    /**
     *
     *
     * @param obj
     * @param day
     * @return
     */
    public static String formatDateTime(Object obj, boolean day) {
        return formatDateTime(obj, true, day, true);
    }

    /**
     *
     *
     * @param obj
     * @return
     */
    public static String formatYearMonth(Object obj) {
        return formatDateTime(obj, false, false, false);
    }

    /**
     * Formats an object to produce a string.
     *
     * @param obj The object to format
     * @param date
     * @param day Display day of the week
     * @param time
     * @return Formatted string
     */
    public static String formatDateTime(Object obj, boolean date, boolean day, boolean time) {
        // get default locale
        Locale locale = Locale.getDefault();

        //
        String pattern;
        if (Locale.JAPAN.equals(locale) || Locale.JAPANESE.equals(locale)) {
            // japanese
            pattern = "yyyy/MM";
            if (date) {
                pattern = pattern + "/dd";
                if (day) {
                    pattern = pattern + "(E)";
                }
                if (time) {
                    pattern = pattern + " HH:mm";
                }
            }
        } else if (Locale.KOREA.equals(locale) || Locale.KOREAN.equals(locale)) {
            // 韓国語
            pattern = "yyyy.MM";
            if (date) {
                pattern = pattern + ".dd";
                if (day) {
                    pattern = pattern + " (E)";
                }
                if (time) {
                    pattern = pattern + " HH:mm";
                }
            }
        } else if (Locale.SIMPLIFIED_CHINESE.equals(locale)
                || Locale.CHINA.equals(locale) || Locale.CHINESE.equals(locale)) {
            // 簡体字
            pattern = "yyyy-MM";
            if (date) {
                pattern = pattern + "-dd";
                if (day) {
                    pattern = pattern + "(E)";
                }
                if (time) {
                    pattern = pattern + " HH:mm";
                }
            }
        } else if (Locale.TRADITIONAL_CHINESE.equals(locale) || Locale.TAIWAN.equals(locale)) {
            // 繫体字
            pattern = "yyyy-MM";
            if (date) {
                pattern = pattern + "-dd";
                if (day) {
                    pattern = pattern + "(E)";
                }
                if (time) {
                    pattern = pattern + " HH:mm";
                }
            }
        } else {
            // english
            pattern = "";
            if (date && day) {
                pattern = pattern + "E ";
            }
            pattern = pattern + "MM";
            if (date) {
                pattern = pattern + "/dd";
            }
            pattern = pattern + "/yyyy";
            if (date && time) {
                pattern = pattern + " HH:mm";
            }
        }

        // Formatted string.
        return new SimpleDateFormat(pattern, locale).format(obj);
    }

    /**
     *
     *
     * @param month Month
     * @return
     */
    public static String formatAbbreviatedMonthInEnglish(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "Jan";

            case Calendar.FEBRUARY:
                return "Feb";

            case Calendar.MARCH:
                return "Mar";

            case Calendar.APRIL:
                return "Apr";

            case Calendar.MAY:
                return "May";

            case Calendar.JUNE:
                return "Jun";

            case Calendar.JULY:
                return "Jul";

            case Calendar.AUGUST:
                return "Aug";

            case Calendar.SEPTEMBER:
                return "Sep";

            case Calendar.OCTOBER:
                return "Oct";

            case Calendar.NOVEMBER:
                return "Nov";

            case Calendar.DECEMBER:
                return "Dec";

            default:
                return "";
        }
    }
}
