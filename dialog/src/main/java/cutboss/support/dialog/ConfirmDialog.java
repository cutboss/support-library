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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * ConfirmDialog.
 *
 * @author CUTBOSS
 */
public class ConfirmDialog extends BaseDialog {
    /** TAG */
    public static final String TAG = ConfirmDialog.class.getSimpleName();

    /** KEY */
    public static final String KEY_TITLE_ID = "title_id";
    public static final String KEY_MESSAGE_ID = "message_id";
    public static final String KEY_POSITIVE_BUTTON_TEXT_ID = "positive_button_text_id";
    public static final String KEY_NEGATIVE_BUTTON_TEXT_ID = "negative_button_text_id";
    public static final String KEY_LISTENER = BaseDialog.KEY_LISTENER;

    /**
     * Default constructor.
     */
    public ConfirmDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get arguments
        Bundle args = getArguments();
        int titleId = 0;
        int messageId = 0;
        int positiveButtonTextId = android.R.string.ok;
        int negativeButtonTextId = android.R.string.cancel;
        final OnClickListener listener;
        if (null != args) {
            // get title id
            if (args.containsKey(KEY_TITLE_ID)){
                titleId = args.getInt(KEY_TITLE_ID);
            }

            // get message id
            if (args.containsKey(KEY_MESSAGE_ID)){
                messageId = args.getInt(KEY_MESSAGE_ID);
            }

            // get positive button text id
            if (args.containsKey(KEY_POSITIVE_BUTTON_TEXT_ID)){
                positiveButtonTextId = args.getInt(KEY_POSITIVE_BUTTON_TEXT_ID);
            }

            // get negative button text id
            if (args.containsKey(KEY_NEGATIVE_BUTTON_TEXT_ID)){
                negativeButtonTextId = args.getInt(KEY_NEGATIVE_BUTTON_TEXT_ID);
            }

            // get listener
            if (args.containsKey(KEY_LISTENER)) {
                listener = args.getParcelable(KEY_LISTENER);
            } else {
                listener = null;
            }
        } else {
            throw new IllegalArgumentException("Args no exist.");
        }

        // create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (0 < titleId) {
            builder.setTitle(titleId);
        }
        if (0 < messageId) {
            builder.setMessage(messageId);
        }
        builder.setPositiveButton(positiveButtonTextId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // listen?
                if (null != listener) {
                    // get tag
                    String tag = getTag();

                    // positive click
                    listener.onPositiveClick(tag, which);
                }
            }
        });
        builder.setNegativeButton(negativeButtonTextId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // listen?
                if (null != listener) {
                    // get tag
                    String tag = getTag();

                    // negative click
                    listener.onNegativeClick(tag, which);
                }
            }
        });
        return builder.create();
    }

    /**
     * OnClickListener.
     */
    public static abstract class OnClickListener implements Parcelable {
        public abstract void onPositiveClick(String tag, int which);
        public abstract void onNegativeClick(String tag, int which);

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {}
    }

    /**
     * Set the OnClickListener.
     *
     * @param listener OnClickListener
     * @return ConfirmDialog
     */
    public ConfirmDialog setOnClickListener(OnClickListener listener) {
        // is null?
        if (null == listener) {
            return this;
        }

        // set args
        Bundle args = new Bundle();
        args.putParcelable(KEY_LISTENER, listener);
        setArguments(args);
        return this;
    }
}
