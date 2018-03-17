/*
 * Copyright (C) 2017-2018 CUTBOSS
 */

package cutboss.support.util;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    /**
     * Start share.
     *
     * @param context context
     * @param subject subject
     * @param text text
     * @param uri uri
     * @param title optional title that will be displayed in the chooser
     * @return result
     */
    public static boolean startShare(
            Context context, String subject, String text, Uri uri, CharSequence title) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType("text/plain");
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
        Log.d(TAG, "showSoftInput: show: " + show);
        if (!show) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            boolean focus = view.requestFocus();
            Log.d(TAG, "showSoftInput: focus: " + focus);
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
}
