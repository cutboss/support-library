/*
 * MIT License
 *
 * Copyright (c) 2018 CUTBOSS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cutboss.support.dialog;

import android.app.Activity;
import android.os.Bundle;

/**
 * DialogUtils.
 *
 * @author CUTBOSS
 */
public class DialogUtils {
    /**
     * Show the SingleChoiceDialog.
     *
     * @param activity activity
     * @param items items
     * @param checkedItem checked item position
     * @param listener SingleChoiceDialog.OnClickListener
     */
    public static void showSingleChoiceDialog(
            Activity activity, CharSequence[] items, int checkedItem,
            SingleChoiceDialog.OnClickListener listener) {
        // set args
        Bundle args = new Bundle();
        args.putCharSequenceArray(SingleChoiceDialog.KEY_ITEMS, items);
        args.putInt(SingleChoiceDialog.KEY_CHECKED_ITEM, checkedItem);

        // show dialog
        SingleChoiceDialog dialog = new SingleChoiceDialog().setOnClickListener(listener);
        dialog.setArguments(args);
        dialog.show(activity.getFragmentManager(), SingleChoiceDialog.TAG);
    }

    /**
     *
     *
     * @param activity
     * @param titleId
     * @param messageId
     * @param positiveButtonTextId
     * @param negativeButtonTextId
     * @param listener
     */
    public static void showConfirmDialog(
            Activity activity, int titleId, int messageId,
            int positiveButtonTextId, int negativeButtonTextId,
            ConfirmDialog.OnClickListener listener) {
        // set args
        Bundle args = new Bundle();
        if (0 < titleId) {
            args.putInt(ConfirmDialog.KEY_TITLE_ID, titleId);
        }
        if (0 < messageId) {
            args.putInt(ConfirmDialog.KEY_MESSAGE_ID, messageId);
        }
        if (0 < positiveButtonTextId) {
            args.putInt(ConfirmDialog.KEY_POSITIVE_BUTTON_TEXT_ID, positiveButtonTextId);
        }
        if (0 < negativeButtonTextId) {
            args.putInt(ConfirmDialog.KEY_NEGATIVE_BUTTON_TEXT_ID, negativeButtonTextId);
        }

        // show dialog
        ConfirmDialog dialog = new ConfirmDialog().setOnClickListener(listener);
        dialog.setArguments(args);
        dialog.show(activity.getFragmentManager(), ConfirmDialog.TAG);
    }

    /**
     *
     *
     * @param activity
     * @param titleId
     * @param hintId
     * @param positiveButtonTextId
     * @param negativeButtonTextId
     * @param listener
     */
    public static void showPasswordDialog(
            Activity activity, int titleId, int hintId,
            int positiveButtonTextId, int negativeButtonTextId,
            PasswordDialog.OnClickListener listener) {
        showPasswordDialog(
                activity, titleId, hintId,
                positiveButtonTextId, negativeButtonTextId, true, listener);
    }

    /**
     *
     *
     * @param activity
     * @param titleId
     * @param hintId
     * @param positiveButtonTextId
     * @param negativeButtonTextId
     * @param cancelable
     * @param listener
     */
    public static void showPasswordDialog(
            Activity activity, int titleId, int hintId,
            int positiveButtonTextId, int negativeButtonTextId, boolean cancelable,
            PasswordDialog.OnClickListener listener) {
        // set args
        Bundle args = new Bundle();
        if (0 < titleId) {
            args.putInt(PasswordDialog.KEY_TITLE_ID, titleId);
        }
        if (0 < hintId) {
            args.putInt(PasswordDialog.KEY_HINT_ID, hintId);
        }
        if (0 < positiveButtonTextId) {
            args.putInt(PasswordDialog.KEY_POSITIVE_BUTTON_TEXT_ID, positiveButtonTextId);
        }
        if (0 < negativeButtonTextId) {
            args.putInt(PasswordDialog.KEY_NEGATIVE_BUTTON_TEXT_ID, negativeButtonTextId);
        }

        // show dialog
        PasswordDialog dialog = new PasswordDialog().setOnClickListener(listener);
        dialog.setArguments(args);
        dialog.setCancelable(cancelable);
        dialog.show(activity.getFragmentManager(), PasswordDialog.TAG);
    }
}
