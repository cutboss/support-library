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
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * PasswordDialog.
 *
 * @author CUTBOSS
 */
public class PasswordDialog extends BaseDialog {
    /** TAG */
    public static final String TAG = PasswordDialog.class.getSimpleName();

    /** KEY */
    public static final String KEY_HINT_ID = "hint_id";

    /**
     * Default constructor.
     */
    public PasswordDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get arguments
        Bundle args = getArguments();
        int titleId = 0;
        int hintId = 0;
        int positiveButtonTextId = android.R.string.ok;
        int negativeButtonTextId = android.R.string.cancel;
        final OnClickListener listener;
        if (null != args) {
            // get title id
            if (args.containsKey(KEY_TITLE_ID)){
                titleId = args.getInt(KEY_TITLE_ID);
            }

            // get hint id
            if (args.containsKey(KEY_HINT_ID)){
                hintId = args.getInt(KEY_HINT_ID);
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

        // get layout view
        final View view =
                ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.cutboss_support_dialog_password, null);

        // get input view
        final EditText inputView = view.findViewById(R.id.cutboss_support_dialog_password_input);
        final int defaultInputType = inputView.getInputType();
        inputView.setInputType(defaultInputType|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if (0 < hintId) {
            inputView.setHint(hintId);
        }
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(16);
        inputView.setFilters(filters);

        // get check box
        CheckBox checkBox = view.findViewById(R.id.cutboss_support_dialog_password_check_box);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                // set input type
                int type;
                if (checked) {
                    type = defaultInputType|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                } else {
                    type = defaultInputType|InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }
                inputView.setInputType(type);

                //
                inputView.setSelection(inputView.getText().toString().length());
            }
        });

        // create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (0 < titleId) {
            builder.setTitle(titleId);
        }
        builder.setView(view);
        builder.setPositiveButton(positiveButtonTextId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // listen?
                if (null != listener) {
                    // get tag
                    String tag = getTag();

                    // positive click
                    listener.onPositiveClick(tag, which, inputView.getText().toString());
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
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //
                if (inputView.requestFocus()) {
                    //
                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (null != imm) {
                        imm.showSoftInput(inputView, 0);
                    }
                }
            }
        });
        return dialog;
    }

    /**
     * OnClickListener.
     */
    public static abstract class OnClickListener extends ParcelableListener {
        protected abstract void onPositiveClick(String tag, int which, String password);
        protected abstract void onNegativeClick(String tag, int which);
    }

    /**
     * Set the OnClickListener.
     *
     * @param listener OnClickListener
     * @return PasswordDialog
     */
    public PasswordDialog setOnClickListener(OnClickListener listener) {
        return (PasswordDialog) super.setOnClickListener(listener);
    }
}
