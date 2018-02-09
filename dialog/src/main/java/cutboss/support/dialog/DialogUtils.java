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
import android.util.Log;

/**
 * DialogUtils.
 *
 * @author CUTBOSS
 */
public class DialogUtils {
    /** TAG */
    private static final String TAG = DialogUtils.class.getSimpleName();

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
        Log.d(TAG, "showSingleChoiceDialog: start");
        Log.d(TAG, "showSingleChoiceDialog: checkedItem: " + checkedItem);
        Log.d(TAG, "showSingleChoiceDialog: listener: " + listener);

        // set args
        Bundle args = new Bundle();
        args.putCharSequenceArray(SingleChoiceDialog.KEY_ITEMS, items);
        args.putInt(SingleChoiceDialog.KEY_CHECKED_ITEM, checkedItem);
        Log.d(TAG, "showSingleChoiceDialog: args: " + args);

        // show dialog
        SingleChoiceDialog dialog = new SingleChoiceDialog().setOnClickListener(listener);
        dialog.setArguments(args);
        dialog.show(activity.getFragmentManager(), SingleChoiceDialog.TAG);

        Log.d(TAG, "showSingleChoiceDialog: end");
    }
}
