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

import android.app.DialogFragment;
import android.os.Bundle;

/**
 * BaseDialog.
 *
 * @author CUTBOSS
 */
public class BaseDialog extends DialogFragment {
    /** KEY */
    protected static final String KEY_TITLE_ID = "title_id";
    protected static final String KEY_MESSAGE_ID = "message_id";
    protected static final String KEY_POSITIVE_BUTTON_TEXT_ID = "positive_button_text_id";
    protected static final String KEY_NEGATIVE_BUTTON_TEXT_ID = "negative_button_text_id";
    protected static final String KEY_LISTENER = "listener";

    @Override
    public void setArguments(Bundle args) {
        // get args
        if (null != getArguments()) {
            args.putAll(getArguments());
        }

        // set args
        super.setArguments(args);
    }

    /**
     *
     *
     * @param listener
     * @return
     */
    protected BaseDialog setOnClickListener(ParcelableListener listener) {
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
