package com.denispetrov.graphics.drawable;

import org.eclipse.swt.graphics.GC;

public class ViewportBackgroundDrawable extends ViewportDrawableBase implements ViewportDrawable {

    public static final int BACKGROUND_RANK = 0;

    public ViewportBackgroundDrawable() {
        super();
        setRank(BACKGROUND_RANK);
    }

    @Override
    public void draw() {
        GC gc = vc.getGC();
        gc.setBackground(vc.getBackgroundColor());
        gc.setForeground(vc.getForegroundColor());
        gc.fillRectangle(0, 0, vc.getCanvasWidth(), vc.getCanvasHeight());
    }

    @Override
    public void modelUpdated() {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextUpdated() {
        // TODO Auto-generated method stub

    }

}
