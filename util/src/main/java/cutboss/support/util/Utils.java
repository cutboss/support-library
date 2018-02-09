/*
 * Copyright (C) 2017-2018 CUTBOSS
 */

package cutboss.support.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Utils.
 *
 * @author CUTBOSS
 */
public class Utils {
    /** TAG */
    private static final String TAG = Utils.class.getSimpleName();

    // ---------------------------------------------------------------------------------------------
    // BROWSER
    // ---------------------------------------------------------------------------------------------

    /**
     * Start browser.
     *
     * @param context context
     * @param resId the resource id of the url
     */
    public static void startBrowser(Context context, int resId) {
        startBrowser(context, context.getString(resId));
    }

    /**
     * Start browser.
     *
     * @param context context
     * @param url url
     */
    public static void startBrowser(Context context, String url) {
        startBrowser(context, Uri.parse(url));
    }

    /**
     * Start browser.
     *
     * @param context context
     * @param uri uri
     */
    public static void startBrowser(Context context, Uri uri) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    // ---------------------------------------------------------------------------------------------
    // SHARE
    // ---------------------------------------------------------------------------------------------

    /**
     * Start share.
     *
     * @param context context
     * @param text text
     * @param title title
     */
    public static void startShare(Context context, String text, String title) {
        startShare(context, null, text, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param subject subject
     * @param text text
     * @param title title
     */
    public static void startShare(Context context, String subject, String text, String title) {
        startShare(context, subject, text, null, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param uri uri
     * @param title title
     */
    public static void startShare(Context context, Uri uri, String title) {
        startShare(context, null, uri, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param text text
     * @param uri uri
     * @param title title
     */
    public static void startShare(Context context, String text, Uri uri, String title) {
        startShare(context, null, text, uri, title);
    }

    /**
     * Start share.
     *
     * @param context context
     * @param subject subject
     * @param text text
     * @param uri uri
     * @param title title
     */
    public static void startShare(
            Context context, String subject, String text, Uri uri, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if ((null != subject) && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if ((null != text) && !"".equals(text)) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }
        if (null != uri) {
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        context.startActivity(Intent.createChooser(intent, title));
    }

    // ---------------------------------------------------------------------------------------------
    // CLIPBOARD
    // ---------------------------------------------------------------------------------------------

    /**
     *
     *
     * @param context context
     * @param text text
     */
    public static void copyToClipboard(Context context, String text) {
        copyToClipboard(context, "", text, Toast.LENGTH_LONG);
    }

    /**
     *
     *
     * @param context context
     * @param text text
     * @param duration duration
     */
    public static void copyToClipboard(Context context, String text, int duration) {
        copyToClipboard(context, "", text, duration);
    }

    /**
     *
     *
     * @param context context
     * @param text text
     */
    public static void copyToClipboard(Context context, String label, String text) {
        copyToClipboard(context, label, text, Toast.LENGTH_LONG);
    }

    /**
     * Copy to clipboard.
     *
     * @param context context
     * @param label label
     * @param text text
     * @param duration duration
     */
    public static void copyToClipboard(Context context, String label, String text, int duration) {
        // copy to clipboard
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (null == clipboardManager) {
            return;
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
    }
}
