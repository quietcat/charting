package com.denispetrov.graphics;

import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.graphics.GraphingContext.DrawParameters;
import com.denispetrov.graphics.GraphingContext.XAnchor;
import com.denispetrov.graphics.GraphingContext.YAnchor;
import com.denispetrov.graphics.GraphingController.TrackingObject;

public class GraphingRectHandler extends GraphingObjectHandlerBase<Rectangle> {

    private DrawParameters dp = new DrawParameters();

    private static final int W = 100;
    private static final int H = 100;

    private int x = 100;
    private int y = 100;

    private GraphingContext grc;

    @Override
    public void draw() {
        grc.drawRectangle(x, y, W, H);
        grc.drawLine(x, y + H / 2, x + W, y + H / 2);
        grc.drawText("Default", x + W, y + H, dp);
        dp.yAnchor = YAnchor.MIDDLE;
        grc.drawText("YAnchor MIDDLE", x + W, y + H / 2, dp);
        dp.yAnchor = YAnchor.TOP;
        grc.drawText("YAnchor TOP", x + W, y, dp);
        dp.xAnchor = XAnchor.RIGHT;
        dp.yAnchor = YAnchor.BOTTOM;
        grc.drawText("YAnchor BOTTOM", x, y + H, dp);
        dp.yAnchor = YAnchor.MIDDLE;
        grc.drawText("YAnchor MIDDLE", x, y + H / 2, dp);
        dp.yAnchor = YAnchor.TOP;
        grc.drawText("YAnchor TOP", x, y, dp);
    }

    @Override
    public int getRank() {
        return 0;
    }

    @Override
    public Set<TrackingObject> getTrackingObjects() {
        // TODO Auto-generated method stub
        return null;
    }

}
