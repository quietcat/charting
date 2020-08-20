package com.denispetrov.charting.layer.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.adapters.LayerAdapter;

public class ViewportBackgroundLayer extends LayerAdapter implements DrawableLayer {

    public static final int DEFAULT_BACKGROUND_COLOR = SWT.COLOR_BLACK;
    public static final int DEFAULT_FOREGROUND_COLOR = SWT.COLOR_GRAY;

    @Override
    public void draw() {
        Color backgroundColor = view.getViewContext().getBackgroundColor();
        Color foregroundColor = view.getViewContext().getForegroundColor();
        if (backgroundColor == null) {
            backgroundColor = view.getCanvas().getDisplay().getSystemColor(DEFAULT_BACKGROUND_COLOR);
            view.getViewContext().setBackgroundColor(backgroundColor);
        }
        if (foregroundColor == null) {
            foregroundColor = view.getCanvas().getDisplay().getSystemColor(DEFAULT_FOREGROUND_COLOR);
            view.getViewContext().setForegroundColor(foregroundColor);
        }
        GC gc = view.getGC();
        gc.setBackground(backgroundColor);
        gc.setForeground(foregroundColor);
        gc.fillRectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight());
    }

}
