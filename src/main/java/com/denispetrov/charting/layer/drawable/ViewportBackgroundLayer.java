package com.denispetrov.charting.layer.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.denispetrov.charting.layer.DrawableLayer;
import com.denispetrov.charting.layer.service.LayerAdapter;

public class ViewportBackgroundLayer extends LayerAdapter implements DrawableLayer {

    public static final int DEFAULT_BACKGROUND_COLOR = SWT.COLOR_BLACK;
    public static final int DEFAULT_FOREGROUND_COLOR = SWT.COLOR_GRAY;

    @Override
    public void draw() {
        Color backgroundColor = view.getViewContext().getBackgroundColor();
        Color foregroundColor = view.getViewContext().getForegroundColor();
        if (backgroundColor == null) {
            backgroundColor = view.getCanvas().getDisplay().getSystemColor(DEFAULT_BACKGROUND_COLOR);
        }
        if (foregroundColor == null) {
            foregroundColor = view.getCanvas().getDisplay().getSystemColor(DEFAULT_FOREGROUND_COLOR);
        }
        GC gc = view.getGC();
        gc.setBackground(backgroundColor);
        gc.setForeground(foregroundColor);
        gc.fillRectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight());
    }

}
