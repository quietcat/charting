package com.denispetrov.graphics.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.denispetrov.graphics.ViewContext;

public class ViewportBackgroundDrawable extends ViewportDrawableBase implements ViewportDrawable {

    public static int BACKGROUND_RANK = 10000;

    Color backgroundColor;
    Color foregroundColor;
    public ViewportBackgroundDrawable() {
        super();
        setRank(BACKGROUND_RANK);
    }

    @Override
    public void draw() {
        GC gc = vc.getGC();
        gc.setBackground(backgroundColor);
        gc.setForeground(foregroundColor);
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

    @Override
    public void setViewContext(ViewContext<?> vc) {
        super.setViewContext(vc);
        backgroundColor = vc.getCanvas().getDisplay().getSystemColor(SWT.COLOR_BLACK);
        foregroundColor = vc.getCanvas().getDisplay().getSystemColor(SWT.COLOR_GRAY);
    }

}
