package com.denispetrov.charting.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
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
public class ViewContext implements ControlListener {
    private static final Logger LOG = LoggerFactory.getLogger(ViewContext.class);

    public static enum AxisRange {
        FULL,
        POSITIVE_ONLY,
        NEGATIVE_ONLY
    }

    public static final int DEFAULT_MARGIN = 10;
    public static final double DEFAULT_SCALE = 1.0;
    public static final int DEFAULT_DRAG_THRESHOLD = 4;
    public static final int DEFAULT_BACKGROUND_COLOR = SWT.COLOR_BLACK;
    public static final int DEFAULT_FOREGROUND_COLOR = SWT.COLOR_GRAY;

    private int topMargin = DEFAULT_MARGIN, leftMargin = DEFAULT_MARGIN, rightMargin = DEFAULT_MARGIN,
            bottomMargin = DEFAULT_MARGIN;
    private GC gc;
    private Canvas canvas;
    private View view;
    private double scaleX = DEFAULT_SCALE, scaleY = DEFAULT_SCALE;
    private double baseX = 0.0, baseY = 0.0;
    private double resizeCenterX = 0.0, resizeCenterY = 0.0;
    private int dragThreshold = DEFAULT_DRAG_THRESHOLD;
    private Object model;
    private Color backgroundColor;
    private Color foregroundColor;
    private Rectangle mainAreaRectangle = new Rectangle(0, 0, 0, 0);
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
        switch (xAxisRange) {
        default:
            this.baseX = baseX;
            break;
        case POSITIVE_ONLY:
            this.baseX = Math.max(0.0, baseX);
            break;
        case NEGATIVE_ONLY:
            Rectangle mainAreaRectangle = getMainAreaRectangle();
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
            Rectangle mainAreaRectangle = getMainAreaRectangle();
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

    public void drawRectangle(double x, double y, double width, double height) {
        gc.drawRectangle(x(x), y(y + height), w(width), h(height));
    }

    public void drawRectangle(FRectangle rectangle) {
        gc.drawRectangle(rectangle(rectangle));
    }

    public void drawRectangle(HRectangle rectangle) {
        gc.drawRectangle(x(rectangle.x), y(rectangle.y) + rectangle.h, rectangle.w, rectangle.h);
    }

    public void fillRectangle(double x, double y, double width, double height) {
        gc.fillRectangle(x(x), y(y + height), w(width), h(height));
    }

    public void fillRectangle(FRectangle rectangle) {
        gc.fillRectangle(rectangle(rectangle));
    }

    public void fillRectangle(HRectangle rectangle) {
        gc.fillRectangle(x(rectangle.x), y(rectangle.y) + rectangle.h, rectangle.w, rectangle.h);
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
        if (dp == null) {
            dp = new DrawParameters();
        }
        Rectangle ib = image.getBounds();
        Rectangle extent = extent(new FPoint(x, y), new Point(ib.width, ib.height), dp);
        gc.drawImage(image, extent.x + dp.xMargin, extent.y + dp.yMargin);
    }

    public void drawText(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        Rectangle extent = extent(new FPoint(x, y), gc.textExtent(text, dp.textExtentFlags), dp);
        gc.drawText(text, extent.x, extent.y, dp.isTransparent);
    }

    public Rectangle textRectangle(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        return extent(new FPoint(x, y), gc.textExtent(text, dp.textExtentFlags), dp);
    }

    public HRectangle textRectangleH(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        return rectangleH(extent(new FPoint(x, y), gc.textExtent(text, dp.textExtentFlags), dp));
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
            ix = x(org.x) + dp.xMargin;
            break;
        case MIDDLE:
            ix = x(org.x) - (int) Math.round(width / 2.0);
            break;
        case RIGHT:
            ix = x(org.x) - width - dp.xMargin;
            break;
        default:
            return null;
        }
        switch (dp.yAnchor) {
        case TOP:
            iy = y(org.y) + dp.yMargin;
            break;
        case MIDDLE:
            iy = y(org.y) - (int) Math.round(height / 2.0);
            break;
        case BOTTOM:
            iy = y(org.y) - height - dp.yMargin;
            break;
        default:
            return null;
        }
        return new Rectangle(ix, iy, width, height);
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

    public void setView(View view) {
        if (this.canvas != null) {
            this.canvas.removeControlListener(this);
        }
        this.view = view;
        this.canvas = view.getCanvas();
        this.canvas.addControlListener(this);
        calculateMainAreaRectangle();
        validateBase();
        if (backgroundColor == null) {
            backgroundColor = canvas.getDisplay().getSystemColor(DEFAULT_BACKGROUND_COLOR);
        }
        if (foregroundColor == null) {
            foregroundColor = canvas.getDisplay().getSystemColor(DEFAULT_FOREGROUND_COLOR);
        }
    }

    public Rectangle getMainAreaRectangle() {
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

    @Override
    public void controlMoved(ControlEvent e) {
    }

    @Override
    public void controlResized(ControlEvent e) {
        onCanvasResized();
        view.contextUpdated();
    }

    private void onCanvasResized() {
        Rectangle oldMainAreaRectangle = mainAreaRectangle;
        calculateMainAreaRectangle();
        setBaseX(getBaseX() + w(oldMainAreaRectangle.width) * resizeCenterX - w(mainAreaRectangle.width) * resizeCenterX);
        setBaseY(getBaseY() + h(oldMainAreaRectangle.height) * resizeCenterY - h(mainAreaRectangle.height) * resizeCenterY);
        LOG.trace("Canvas resized, main area w={} h={}", mainAreaRectangle.width, mainAreaRectangle.height);
    }

    private void calculateMainAreaRectangle() {
        Rectangle canvasBounds = canvas.getBounds();
        mainAreaRectangle = new Rectangle(
                leftMargin,
                topMargin,
                canvasBounds.width - leftMargin - rightMargin,
                canvasBounds.height - topMargin - bottomMargin);
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
