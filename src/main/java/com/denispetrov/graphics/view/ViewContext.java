package com.denispetrov.graphics.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;

import com.denispetrov.graphics.drawable.DrawParameters;
import com.denispetrov.graphics.model.FPoint;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.XAnchor;
import com.denispetrov.graphics.model.YAnchor;

/**
 * A view context encapsulates all information needed by {@link Drawable}s and {@link Plugin}s to represent the given {@link Model}.
 * A model is an opaque object that specific Drawables and Plugins are able to interpret.
 * The bulk of ViewContext class is concerned with translation between display and model coordinates.
 * The rest is convenience primitive drawing methods that use model coordinates.
 */
public class ViewContext {
    public static final int DEFAULT_MARGIN = 10;
    public static final double DEFAULT_SCALE = 1.0;
    private static final int DEFAULT_DRAG_THRESHOLD = 4;
    public static final int DEFAULT_BACKGROUND_COLOR = SWT.COLOR_BLACK;
    public static final int DEFAULT_FOREGROUND_COLOR = SWT.COLOR_GRAY;

    private int topMargin = DEFAULT_MARGIN, leftMargin = DEFAULT_MARGIN, rightMargin = DEFAULT_MARGIN,
            bottomMargin = DEFAULT_MARGIN;
    private GC gc;
    private Canvas canvas;
    private double scaleX = DEFAULT_SCALE, scaleY = DEFAULT_SCALE;
    private double baseX = 0.0, baseY = 0.0;
    private XAnchor xAnchor = XAnchor.LEFT;
    private YAnchor yAnchor = YAnchor.MIDDLE;
    private int dragThreshold = DEFAULT_DRAG_THRESHOLD;
    private Object model;
    private Color backgroundColor;
    private Color foregroundColor;
    private Rectangle mainAreaRectangle = new Rectangle(0,0,0,0);
    private boolean allowNegativeBaseX = true;
    private boolean allowNegativeBaseY = true;

    public int getMarginTop() {
        return topMargin;
    }

    public void setMarginTop(int marginTop) {
        this.topMargin = marginTop;
    }

    public int getMarginLeft() {
        return leftMargin;
    }

    public void setMarginLeft(int marginLeft) {
        this.leftMargin = marginLeft;
    }

    public int getMarginRight() {
        return rightMargin;
    }

    public void setMarginRight(int marginRight) {
        this.rightMargin = marginRight;
    }

    public int getMarginBottom() {
        return bottomMargin;
    }

    public void setMarginBottom(int marginBottom) {
        this.bottomMargin = marginBottom;
    }

    public void setMargin(int margin) {
        setMarginTop(margin);
        setMarginLeft(margin);
        setMarginRight(margin);
        setMarginBottom(margin);
    }

    public int getCanvasWidth() {
        return canvas.getBounds().width;
    }

    public double getWidth() {
        return w(getCanvasWidth() - leftMargin - rightMargin);
    }

    public int getCanvasHeight() {
        return canvas.getBounds().height;
    }

    public double getHeight() {
        return h(getCanvasHeight() - topMargin - bottomMargin);
    }

    public GC getGC() {
        return gc;
    }

    public void setGC(GC gc) {
        this.gc = gc;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getBaseX() {
        return baseX;
    }

    public void setBaseX(double baseX) {
        if (!allowNegativeBaseX && baseX < 0) {
            this.baseX = 0.0;
        } else {
            this.baseX = baseX;
        }
    }

    public double getBaseY() {
        return baseY;
    }

    public void setBaseY(double baseY) {
        if (!allowNegativeBaseY && baseY < 0) {
            this.baseY = 0.0;
        } else {
            this.baseY = baseY;
        }
    }

    public XAnchor getxAnchor() {
        return xAnchor;
    }

    public void setxAnchor(XAnchor xAnchor) {
        this.xAnchor = xAnchor;
    }

    public YAnchor getyAnchor() {
        return yAnchor;
    }

    public void setyAnchor(YAnchor yAnchor) {
        this.yAnchor = yAnchor;
    }

    public int w(double w) {
        return (int) Math.round(w * scaleX);
    }

    public double w(int w) {
        return w / scaleX;
    }

    public int x(double x) {
        return leftMargin + w(x - baseX);
    }

    public double x(int x) {
        return w(x - leftMargin) + baseX;
    }

    public int h(double h) {
        return (int) Math.round(h * scaleY);
    }

    public double h(int h) {
        return h / scaleY;
    }

    public int y(double y) {
        return getCanvasHeight() - bottomMargin - h(y - baseY);
    }

    public double y(int y) {
        return h(getCanvasHeight() - bottomMargin - y) + baseY;
    }

    public FPoint point(Point p) {
        return new FPoint(x(p.x), y(p.y));
    }

    public Point point(FPoint fp) {
        return new Point(x(fp.x), y(fp.y));
    }

    public FRectangle rectangle(Rectangle r) {
        return new FRectangle(x(r.x), x(r.y), w(r.width), h(r.height));
    }

    public Rectangle rectangle(FRectangle fr) {
        return new Rectangle(x(fr.x), y(fr.y), w(fr.w), h(fr.h));
    }

    public void drawRectangle(double x, double y, double width, double height) {
        gc.drawRectangle(x(x), y(y + height), w(width), h(height));
    }

    public void drawRectangle(FRectangle rectangle) {
        gc.drawRectangle(rectangle(rectangle));
    }

    public void fillRectangle(double x, double y, double width, double height) {
        gc.fillRectangle(x(x), y(y + height), w(width), h(height));
    }

    public void fillRectangle(FRectangle rectangle) {
        gc.fillRectangle(rectangle(rectangle));
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        gc.drawLine(x(x1), y(y1), x(x2), y(y2));
    }

    public void drawPolyLine(double[] polyLine) {
        int[] pointArray = new int[polyLine.length];
        for (int i = 0; i < polyLine.length; i += 2) {
            pointArray[i] = x(polyLine[i]);
            pointArray[i + 1] = y(polyLine[i + 1]);
        }
        gc.drawPolyline(pointArray);
    }

    public void drawImage(Image image, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        int ix = 0, iy = 0;
        if (dp == null) {
            dp = new DrawParameters();
        }
        Rectangle ib = image.getBounds();
        switch (dp.xAnchor) {
        case LEFT:
            ix = x(x) + dp.xMargin;
            break;
        case MIDDLE:
            ix = x(x) - (int) Math.round(ib.width / 2.0);
            break;
        case RIGHT:
            ix = x(x) - ib.width - dp.xMargin;
            break;
        }
        switch (dp.yAnchor) {
        case TOP:
            iy = y(y) + dp.yMargin;
            break;
        case MIDDLE:
            iy = y(y) - (int) Math.round(ib.height / 2.0);
            break;
        case BOTTOM:
            iy = y(y) - ib.height - dp.yMargin;
            break;
        }
        gc.drawImage(image, ix, iy);
    }

    public void drawText(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        int ix = 0, iy = 0;
        if (dp == null) {
            dp = new DrawParameters();
        }
        Point ib = gc.textExtent(text, dp.textExtentFlags);
        switch (dp.xAnchor) {
        case LEFT:
            ix = x(x) + dp.xMargin;
            break;
        case MIDDLE:
            ix = x(x) - (int) Math.round(ib.x / 2.0);
            break;
        case RIGHT:
            ix = x(x) - ib.x - dp.xMargin;
            break;
        }
        switch (dp.yAnchor) {
        case TOP:
            iy = y(y) + dp.yMargin;
            break;
        case MIDDLE:
            iy = y(y) - (int) Math.round(ib.y / 2.0);
            break;
        case BOTTOM:
            iy = y(y) - ib.y - dp.yMargin;
            break;
        }
        gc.drawText(text, ix, iy, dp.isTransparent);
    }

    public Object getModel() {
        return this.model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        if (backgroundColor == null) {
            backgroundColor = canvas.getDisplay().getSystemColor(DEFAULT_BACKGROUND_COLOR);
        }
        if (foregroundColor == null) {
            foregroundColor = canvas.getDisplay().getSystemColor(DEFAULT_FOREGROUND_COLOR);
        }
    }

    public Rectangle getMainAreaRectangle() {
        Rectangle canvasBounds = canvas.getBounds();
        mainAreaRectangle.x = leftMargin;
        mainAreaRectangle.y = topMargin;
        mainAreaRectangle.width = canvasBounds.width - rightMargin - mainAreaRectangle.x;
        mainAreaRectangle.height = canvasBounds.height - bottomMargin - mainAreaRectangle.y;
        return mainAreaRectangle;
    }

    public int getDragThreshold() {
        return dragThreshold;
    }

    public void setDragThreshold(int dragThreshold) {
        this.dragThreshold = dragThreshold;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public boolean isAllowNegativeBaseX() {
        return allowNegativeBaseX;
    }

    public void setAllowNegativeBaseX(boolean allowNegativeBaseX) {
        this.allowNegativeBaseX = allowNegativeBaseX;
    }

    public boolean isAllowNegativeBaseY() {
        return allowNegativeBaseY;
    }

    public void setAllowNegativeBaseY(boolean allowNegativeBaseY) {
        this.allowNegativeBaseY = allowNegativeBaseY;
    }

}
