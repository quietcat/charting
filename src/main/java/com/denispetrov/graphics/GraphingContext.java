package com.denispetrov.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class GraphingContext {
    public enum XAnchor {
        LEFT, MIDDLE, RIGHT
    }

    public enum YAnchor {
        TOP, MIDDLE, BOTTOM
    }

    public static class Anchor {
        public XAnchor xAnchor;
        public YAnchor yAnchor;

        public Anchor(XAnchor xAnchor, YAnchor yAnchor) {
            this.xAnchor = xAnchor;
            this.yAnchor = yAnchor;
        }
    }

    public static class FPoint {
        public double x, y;

        public FPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class FRectangle {

        public double x, y;
        public double w, h;

        public FRectangle(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    public static class DrawParameters {
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

    public static final int DEFAULT_MARGIN = 10;
    public static final int DEFAULT_X_AXIS_HEIGHT = 20;
    public static final int DEFAULT_Y_AXIS_WIDTH = 40;
    public static final double DEFAULT_SCALE = 1.0;

    private int topMargin = DEFAULT_MARGIN, leftMargin = DEFAULT_MARGIN, rightMargin = DEFAULT_MARGIN,
            bottomMargin = DEFAULT_MARGIN;
    private int canvasWidth, canvasHeight;
    private int xAxisHeight = DEFAULT_X_AXIS_HEIGHT, yAxisWidth = DEFAULT_Y_AXIS_WIDTH;
    private GC gc;
    private double scaleX = DEFAULT_SCALE, scaleY = DEFAULT_SCALE;
    private double baseX = 0.0, baseY = 0.0;
    private XAnchor xAnchor = XAnchor.LEFT;
    private YAnchor yAnchor = YAnchor.MIDDLE;

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
        return canvasWidth;
    }

    public void setCanvasWidth(int width) {
        this.canvasWidth = width;
    }

    public double getWidth() {
        return w(canvasWidth - leftMargin - rightMargin - yAxisWidth);
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public double getHeight() {
        return h(canvasHeight - topMargin - bottomMargin - xAxisHeight);
    }

    public void setCanvasHeight(int height) {
        this.canvasHeight = height;
    }

    public int getxAxisHeight() {
        return xAxisHeight;
    }

    public void setxAxisHeight(int xAxisHeight) {
        this.xAxisHeight = xAxisHeight;
    }

    public int getyAxisWidth() {
        return yAxisWidth;
    }

    public void setyAxisWidth(int yAxisWidth) {
        this.yAxisWidth = yAxisWidth;
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
        this.baseX = baseX;
    }

    public double getBaseY() {
        return baseY;
    }

    public void setBaseY(double baseY) {
        this.baseY = baseY;
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
        return leftMargin + yAxisWidth + w(x - baseX);
    }

    public double x(int x) {
        return w(x - leftMargin - yAxisWidth) + baseX;
    }

    public int h(double h) {
        return (int) Math.round(h * scaleY);
    }

    public double h(int h) {
        return h / scaleY;
    }

    public int y(double y) {
        return canvasHeight - bottomMargin - xAxisHeight - h(y - baseY);
    }

    public double y(int y) {
        return h(canvasHeight - bottomMargin - xAxisHeight - y) + baseY;
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
}
