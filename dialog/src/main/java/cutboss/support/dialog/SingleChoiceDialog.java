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
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

/**
 * SingleChoiceDialog.
 *
 * @author CUTBOSS
 */
public class SingleChoiceDialog extends DialogFragment {
    /** TAG */
    public static final String TAG = SingleChoiceDialog.class.getSimpleName();

    /** KEY */
    public static final String KEY_ITEMS = "items";
    public static final String KEY_CHECKED_ITEM = "checked_item";
    public static final String KEY_LISTENER = "listener";

    /**
     * Default constructor.
     */
    public SingleChoiceDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: start");

        // get arguments
        Bundle args = getArguments();
        Log.d(TAG, "onCreateDialog: args: " + args);
        CharSequence[] items;
        int checkedItem;
        final OnClickListener listener;
        if (null != args) {
            // get items
            if (args.containsKey(KEY_ITEMS)) {
                items = args.getCharSequenceArray(KEY_ITEMS);
            } else {
                throw new IllegalArgumentException("Items no exist.");
            }

            // get checked item
            if (args.containsKey(KEY_CHECKED_ITEM)) {
                checkedItem = args.getInt(KEY_CHECKED_ITEM, 0);
            } else {
                checkedItem = 0;
            }
            Log.d(TAG, "onCreateDialog: checkedItem: " + checkedItem);

            // get listener
            if (args.containsKey(KEY_LISTENER)) {
                listener = (OnClickListener) args.getSerializable(KEY_LISTENER);
            } else {
                listener = null;
            }
            Log.d(TAG, "onCreateDialog: listener: " + listener);
        } else {
            throw new IllegalArgumentException("Args no exist.");
        }

        // create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: start");
                Log.d(TAG, "onClick: which: " + which);

                // listen?
                Log.d(TAG, "onClick: listener: " + listener);
                if (null != listener) {
                    // get tag
                    String tag = getTag();
                    Log.d(TAG, "onClick: tag: " + tag);

                    // item click
                    listener.onItemClick(tag, which);
                }

                // dismiss
                dismiss();

                Log.d(TAG, "onClick: end");
            }
        });
        return builder.create();
    }

    /**
     * OnClickListener.
     */
    public interface OnClickListener extends Serializable {
        void onItemClick(String tag, int which);
    }

    /**
     * Set the OnClickListener.
     *
     * @param listener OnClickListener
     * @return SingleChoiceDialog
     */
    public SingleChoiceDialog setOnClickListener(OnClickListener listener) {
        Log.d(TAG, "setOnClickListener: start");
        Log.d(TAG, "setOnClickListener: listener: " + listener);

        // is null?
        if (null == listener) {
            return this;
        }

        // set args
        Bundle args = new Bundle();
        args.putSerializable(KEY_LISTENER, listener);
        setArguments(args);
        Log.d(TAG, "setOnClickListener: args: " + args);
        Log.d(TAG, "setOnClickListener: end");
        return this;
    }

    @Override
    public void setArguments(Bundle args) {
        Log.d(TAG, "setArguments: start");
        Log.d(TAG, "setArguments: args: " + args);

        // get args
        if (null != getArguments()) {
            args.putAll(getArguments());
        }

        // set args
        Log.d(TAG, "setArguments: args: " + args);
        super.setArguments(args);

        Log.d(TAG, "setArguments: end");
    }
}