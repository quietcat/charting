package com.denispetrov.charting.layer.drawable;

import com.denispetrov.charting.model.XAnchor;
import com.denispetrov.charting.model.YAnchor;

public class DrawParameters {
    public XAnchor xAnchor = XAnchor.LEFT;
    public YAnchor yAnchor = YAnchor.BOTTOM;
    public int xMargin = 0, yMargin = 0;
    public boolean isTransparent = false;
    public int textExtentFlags = 0;

    public void reset() {
        xAnchor = XAnchor.LEFT;
        yAnchor = YAnchor.BOTTOM;
        xMargin = 0;
        yMargin = 0;
        isTransparent = false;
        textExtentFlags = 0;
    }
}