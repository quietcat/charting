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
        GC gc = viewContext.getGC();
        gc.setBackground(viewContext.getBackgroundColor());
        gc.setForeground(viewContext.getForegroundColor());
        gc.fillRectangle(0, 0, viewContext.getCanvasWidth(), viewContext.getCanvasHeight());
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
