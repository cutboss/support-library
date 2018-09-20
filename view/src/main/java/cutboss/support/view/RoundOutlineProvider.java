/*
 * Copyright (C) 2018 CUTBOSS
 */

package cutboss.support.view;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * RoundOutlineProvider.
 *
 * @author CUTBOSS
 */
public class RoundOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        int width = view.getWidth();
        int height = view.getHeight();
        outline.setRoundRect(0, 0, width, height, (Math.min(width, height) / 16));
        view.setClipToOutline(true);
    }
}
