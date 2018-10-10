/*
 * Copyright (C) 2017-2018 CUTBOSS
 */

package cutboss.support.util;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Utils.
 *
 * @author CUTBOSS
 */
public class Utils {
    /** TAG */
    private static final String TAG = Utils.class.getSimpleName();

    /**
     *
     *
     * @param context Context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    // ---------------------------------------------------------------------------------------------
    // BROWSER
    // ---------------------------------------------------------------------------------------------

    /**
     * Start browser.
     *
     * @param context context
     * @param resId the resource id of the url
     * @return result
     */
    public static boolean startBrowser(Context context, int resId) {
        return startBrowser(context, context.getString(resId));
    }

    /**
     * Start browser.
     *
     * @param context context
     * @param url url
     * @return result
     */
    public static boolean startBrowser(Context context, String url) {
        return startBrowser(context, Uri.parse(url));
    }

    /**
     * Start browser.
     *
     * @param context context
     * @param uri uri
     * @return result
     */
    public static boolean startBrowser(Context context, Uri uri) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------
    // SHARE
    // ---------------------------------------------------------------------------------------------

    /**
     * Start share.
     *
     * @param context context
     * @param text text
     * @param title optional title that will be displayed in the chooser
     * @return result
     */
    public static boolean startShare(Context context, String text, CharSequence title) {
        return startShare(context, null, text, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param subject subject
     * @param text text
     * @param title optional title that will be displayed in the chooser
     * @return result
     */
    public static boolean startShare(
            Context context, String subject, String text, CharSequence title) {
        return startShare(context, subject, text, null, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param uri uri
     * @param title optional title that will be displayed in the chooser
     * @return result
     */
    public static boolean startShare(Context context, Uri uri, CharSequence title) {
        return startShare(context, null, uri, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param text text
     * @param uri uri
     * @param title optional title that will be displayed in the chooser
     * @return result
     */
    public static boolean startShare(Context context, String text, Uri uri, CharSequence title) {
        return startShare(context, null, text, uri, title);
    }

    public static boolean startShare(
            Context context, String subject, String text, Uri uri, CharSequence title) {
        return startShare(context, subject, text, uri, title, "text/plain");
    }

    /**
     * Start share.
     *
     * @param context context
     * @param subject subject
     * @param text text
     * @param uri uri
     * @param title optional title that will be displayed in the chooser
     * @param mimeType
     * @return result
     */
    public static boolean startShare(
            Context context, String subject, String text, Uri uri, CharSequence title, String mimeType) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType(mimeType);
        if ((null != subject) && !"".equals(subject)) {
            target.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if ((null != text) && !"".equals(text)) {
            target.putExtra(Intent.EXTRA_TEXT, text);
        }
        if (null != uri) {
            target.putExtra(Intent.EXTRA_STREAM, uri);
        }
        try {
            context.startActivity(Intent.createChooser(target, title));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------
    // CLIPBOARD
    // ---------------------------------------------------------------------------------------------

    /**
     * Copy to clipboard.
     *
     * @param context the context to use
     * @param text the actual text in the clip
     * @return result
     */
    public static boolean copyToClipboard(Context context, CharSequence text) {
        return copyToClipboard(context, "", text, Toast.LENGTH_LONG);
    }

    /**
     * Copy to clipboard.
     *
     * @param context the context to use
     * @param text the actual text in the clip
     * @param duration how long to display the message
     * @return result
     */
    public static boolean copyToClipboard(Context context, CharSequence text, int duration) {
        return copyToClipboard(context, "", text, duration);
    }

    /**
     * Copy to clipboard.
     *
     * @param context the context to use
     * @param text the actual text in the clip
     * @return result
     */
    public static boolean copyToClipboard(Context context, CharSequence label, CharSequence text) {
        return copyToClipboard(context, label, text, Toast.LENGTH_LONG);
    }

    /**
     * Copy to clipboard.
     *
     * @param context the context to use
     * @param label user-visible label for the clip data
     * @param text the actual text in the clip
     * @param duration how long to display the message
     * @return result
     */
    public static boolean copyToClipboard(
            Context context, CharSequence label, CharSequence text, int duration) {
        // copy to clipboard
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (null == clipboardManager) {
            return false;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text));

        // show toast
        switch (duration) {
            case Toast.LENGTH_SHORT:
            case Toast.LENGTH_LONG:
                Toast.makeText(context, R.string.cutboss_support_util_copy_to_clipboard, duration).show();
                break;

            default:
                break;
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------
    // SOFT INPUT
    // ---------------------------------------------------------------------------------------------

    /**
     * Soft input area be shown to the user.
     *
     * @param context the context to use
     * @param view the currently focused view, which would like to receive soft keyboard input
     * @return result
     */
    public static boolean showSoftInput(Context context, View view) {
        return showSoftInput(context, view, 0);
    }

    /**
     * Soft input area be shown to the user.
     *
     * @param context the context to use
     * @param view the currently focused view, which would like to receive soft keyboard input
     * @param flags provides additional operating flags
     * @return result
     */
    public static boolean showSoftInput(Context context, View view, int flags) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null == imm) {
            return false;
        }
        boolean show = imm.showSoftInput(view, flags);
        if (!show) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            boolean focus = view.requestFocus();
            if (focus) {
                return showSoftInput(context, view, flags);
            }
        }
        return show;
    }

    /**
     *
     *
     * @param context
     * @param view
     * @return
     */
    public static boolean hideSoftInput(Context context, View view) {
        return hideSoftInput(context, view, 0);
    }

    /**
     *
     *
     * @param context
     * @param view
     * @param flags
     * @return
     */
    public static boolean hideSoftInput(Context context, View view, int flags) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return null != imm && imm.hideSoftInputFromWindow(view.getWindowToken(), flags);
    }

    // ---------------------------------------------------------------------------------------------
    // DATE
    // ---------------------------------------------------------------------------------------------

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

    // ---------------------------------------------------------------------------------------------
    // LOCALE
    // ---------------------------------------------------------------------------------------------

    /**
     * .
     *
     * @param context Context
     * @param newLocale The new default locale.
     */
    public static void setLocale(Context context, Locale newLocale) {
        // set new locale
        Locale.setDefault(newLocale);

        // Resourcesに対するロケールを設定
        Configuration config = new Configuration();
        config.setLocale(newLocale);
        context.getResources().updateConfiguration(config, null);
    }

    // ---------------------------------------------------------------------------------------------
    // ISBN
    // ---------------------------------------------------------------------------------------------

    /**
     *
     *
     * @param isbn13 ISBN13
     * @return ISBN10
     */
    public static String convertIsbn13ToIsbn10(String isbn13) {
        if ((null == isbn13) || "".equals(isbn13) || (13 != isbn13.length())) {
            return "";
        }
        String isbn10 = isbn13.substring(3, (isbn13.length() - 1));
        int digit = 0;
        for (int k = 0; k < isbn10.length(); k++) {
            digit += (Integer.parseInt(String.valueOf(isbn10.charAt(k))) * (10 - k));
        }
        digit = (11 - (digit % 11));
        if (10 == digit) {
            isbn10 += "X";
        } else if (11 == digit) {
            isbn10 += "0";
        } else {
            isbn10 += String.valueOf(digit);
        }
        return isbn10;
    }

    /**
     *
     *
     * @param isbn10 ISBN10
     * @return ISBN13
     */
    public static String convertIsbn10ToIsbn13(String isbn10) {
        // invalid?
        if ((null == isbn10) || "".equals(isbn10) || (10 != isbn10.length())) {
            return "";
        }
        String isbn13 = ("978" + isbn10.substring(0, (isbn10.length() - 1)));
        try {
            Integer.parseInt(isbn13);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
        int digit = 0;
        for (int k = 0; k < isbn13.length(); k++) {
            int num = Integer.parseInt(String.valueOf(isbn13.charAt(k)));
            if (0 == (k % 2)) {
                digit += num;
            } else {
                digit += (num * 3);
            }
        }
        digit = (10 - (digit % 10));
        if (10 == digit) {
            digit = 0;
        }
        isbn13 += digit;
        return isbn13;
    }

    // ---------------------------------------------------------------------------------------------
    // AMAZON
    // ---------------------------------------------------------------------------------------------

    /**
     *
     *
     * @param isbn13 ISBN13
     * @return Url
     */
    public static String convertAmazonUrlFromIsbn13(String isbn13) {
        return convertAmazonUrlFromIsbn10(convertIsbn13ToIsbn10(isbn13));
    }

    /**
     *
     *
     * @param isbn10 ISBN10
     * @return Url
     */
    public static String convertAmazonUrlFromIsbn10(String isbn10) {
        if ((null == isbn10) || "".equals(isbn10) || (10 != isbn10.length())) {
            return "";
        }
        return ("https://www.amazon.co.jp/dp/"
                + isbn10
                + "/?_encoding=UTF8&tag=jempdac-22&language=ja_JP");
    }

    /**
     *
     *
     * @param isbn13 ISBN13
     * @return Image url
     */
    public static String convertAmazonImageUrlFromIsbn13(String isbn13) {
        return convertAmazonImageUrlFromIsbn10(convertIsbn13ToIsbn10(isbn13));
    }

    /**
     *
     *
     * @param isbn10 ISBN10
     * @return Image url
     */
    public static String convertAmazonImageUrlFromIsbn10(String isbn10) {
        if ((null == isbn10) || "".equals(isbn10) || (10 != isbn10.length())) {
            return "";
        }
//        return ("http://images-jp.amazon.com/images/P/" + isbn10 + ".09.MZZZZZZZ");
        return ("http://images-jp.amazon.com/images/P/" + isbn10 + ".09.LZZZZZZZ");
    }

    /**
     *
     *
     * @param url Url of amazon
     * @return ASIN
     */
    public static String getAsinFromAmazonUrl(String url) {
        // invalid?
        if ((null == url) || "".equals(url)
                || !url.contains("amazon") || !url.contains("/ASIN/")) {
            return "";
        }
        return url.split("/ASIN/")[1].split("/")[0];
    }
}
