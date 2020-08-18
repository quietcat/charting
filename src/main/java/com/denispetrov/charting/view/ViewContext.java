package com.denispetrov.charting.view;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.DrawParameters;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.model.HRectangle;

/**
 * A view context encapsulates all information needed by {@link Drawable}s and {@link Plugin}s to represent the given
 * {@link Model}. A model is an opaque object that specific Drawables and Plugins are able to interpret. The bulk of
 * ViewContext class is concerned with translation between display and model coordinates. The rest is convenience
 * primitive drawing methods that use model coordinates.
 */
public class ViewContext {
    private static final Logger LOG = LoggerFactory.getLogger(ViewContext.class);

    public static enum AxisRange {
        FULL,
        POSITIVE_ONLY,
        NEGATIVE_ONLY
    }

    public static final int DEFAULT_MARGIN = 10;
    public static final double DEFAULT_SCALE = 1.0;
    public static final int DEFAULT_DRAG_THRESHOLD = 4;

    private int topMargin = DEFAULT_MARGIN, leftMargin = DEFAULT_MARGIN, rightMargin = DEFAULT_MARGIN,
            bottomMargin = DEFAULT_MARGIN;
    private View<?> view;
    private double scaleX = DEFAULT_SCALE, scaleY = DEFAULT_SCALE;
    private double baseX = 0.0, baseY = 0.0;
    private double resizeCenterX = 0.0, resizeCenterY = 0.0;
    private int dragThreshold = DEFAULT_DRAG_THRESHOLD;
    private Color backgroundColor;
    private Color foregroundColor;
    private AxisRange xAxisRange = AxisRange.FULL;
    private AxisRange yAxisRange = AxisRange.FULL;

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
        switch (xAxisRange) {
        default:
            this.baseX = baseX;
            break;
        case POSITIVE_ONLY:
            this.baseX = Math.max(0.0, baseX);
            break;
        case NEGATIVE_ONLY:
            Rectangle mainAreaRectangle = view.getMainAreaRectangle();
            double maxBaseX = -w(mainAreaRectangle.width);
            this.baseX = Math.min(baseX, maxBaseX);
            LOG.debug("maxBaseX = {}, baseX = {}", maxBaseX, baseX);
            break;
        }
    }

    public double getBaseY() {
        return baseY;
    }

    public void setBaseY(double baseY) {
        switch (yAxisRange) {
        default:
            this.baseY = baseY;
            break;
        case POSITIVE_ONLY:
            this.baseY = Math.max(0.0, baseY);
            break;
        case NEGATIVE_ONLY:
            Rectangle mainAreaRectangle = view.getMainAreaRectangle();
            double maxBaseY = -h(mainAreaRectangle.height);
            this.baseY = Math.min(baseY, maxBaseY);
            break;
        }
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
        return view.getCanvasHeight() - bottomMargin - h(y - baseY);
    }

    public double y(int y) {
        return h(view.getCanvasHeight() - bottomMargin - y) + baseY;
    }

    public FPoint point(Point p) {
        return new FPoint(x(p.x), y(p.y));
    }

    public Point point(FPoint fp) {
        return new Point(x(fp.x), y(fp.y));
    }

    public FRectangle rectangle(Rectangle r) {
        return new FRectangle(x(r.x), y(r.y + r.height), w(r.width), h(r.height));
    }

    public Rectangle rectangle(FRectangle fr) {
        return new Rectangle(x(fr.x), y(fr.y + fr.h), w(fr.w), h(fr.h));
    }

    public HRectangle rectangleH(Rectangle r) {
        return new HRectangle(x(r.x), y(r.y + r.height), r.width, r.height);
    }

    public Rectangle rectangle(HRectangle fr) {
        return new Rectangle(x(fr.x), y(fr.y) + fr.h, fr.w, fr.h);
    }

    public Rectangle extent(FRectangle r, DrawParameters dp) {
        int ix, iy;
        switch (dp.xAnchor) {
        case LEFT:
            ix = x(r.x);
            break;
        case MIDDLE:
            ix = x(r.x - r.w / 2);
            break;
        case RIGHT:
            ix = x(r.x - r.w);
            break;
        default:
            return null;
        }
        switch (dp.yAnchor) {
        case TOP:
            iy = y(r.y + r.h);
            break;
        case MIDDLE:
            iy = y(r.y + r.h / 2.0);
            break;
        case BOTTOM:
            iy = y(r.y);
            break;
        default:
            return null;
        }
        return new Rectangle(ix, iy, w(r.w), h(r.h));
    }

    public Rectangle extent(FPoint org, Point size, DrawParameters dp) {
        int ix, iy;
        int width = size.x + 2 * dp.xMargin;
        int height = size.y + 2 * dp.yMargin;
        switch (dp.xAnchor) {
        case LEFT:
            ix = x(org.x);
            break;
        case MIDDLE:
            ix = x(org.x) - (int) Math.round(width / 2.0);
            break;
        case RIGHT:
            ix = x(org.x) - width;
            break;
        default:
            return null;
        }
        switch (dp.yAnchor) {
        case TOP:
            iy = y(org.y);
            break;
        case MIDDLE:
            iy = y(org.y) - (int) Math.round(height / 2.0);
            break;
        case BOTTOM:
            iy = y(org.y) - height;
            break;
        default:
            return null;
        }
        return new Rectangle(ix, iy, width, height);
    }

    public void setView(View<?> view) {
        this.view = view;
        validateBase();
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

    public AxisRange getXAxisRange() {
        return xAxisRange;
    }

    public void setXAxisRange(AxisRange xAxisRange) {
        this.xAxisRange = xAxisRange;
    }

    public AxisRange getYAxisRange() {
        return yAxisRange;
    }

    public void setYAxisRange(AxisRange yAxisRange) {
        this.yAxisRange = yAxisRange;
    }

    public double getResizeCenterX() {
        return resizeCenterX;
    }

    public void setResizeCenterX(double resizeCenterX) {
        this.resizeCenterX = resizeCenterX;
    }

    public double getResizeCenterY() {
        return resizeCenterY;
    }

    public void setResizeCenterY(double resizeCenterY) {
        this.resizeCenterY = resizeCenterY;
    }

    public void validateBase() {
        validateBaseX();
        validateBaseY();
    }

    public void validateBaseX() {
        setBaseX(getBaseX());
    }

    public void validateBaseY() {
        setBaseY(getBaseY());
    }
}
