/*
 * Copyright (C) 2018 CUTBOSS
 */

package cutboss.support.text;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * LinkableArrowKeyMovementMethod.
 *
 * @author CUTBOSS
 */
public class LinkableArrowKeyMovementMethod extends ArrowKeyMovementMethod {
    /**  */
    private static LinkableArrowKeyMovementMethod sInstance;

    /**
     *
     *
     * @return MovementMethod
     */
    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new LinkableArrowKeyMovementMethod();
        }
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        // get action
        int action = event.getAction();
        if ((action == MotionEvent.ACTION_UP) || (action == MotionEvent.ACTION_DOWN)) {
            // get x
            int x = (int) event.getX();

            // get y
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] linkLeft = buffer.getSpans(off + 1, off, ClickableSpan.class);
            ClickableSpan[] linkRight = buffer.getSpans(off, off - 1, ClickableSpan.class);
            ClickableSpan[] link;
            if (linkLeft.length != 0) {
                link = linkLeft;
            } else {
                link = linkRight;
            }
            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else  {
                    int start = buffer.getSpanStart(link[0]);
                    int stop = buffer.getSpanEnd(link[0]);
                    Selection.setSelection(buffer, start, stop);
                }
                return true;
            }/* else {
                // that's the line we need to remove
                Selection.removeSelection(buffer);
            }*/
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}
