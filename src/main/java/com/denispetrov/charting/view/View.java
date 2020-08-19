package com.denispetrov.charting.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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
import com.denispetrov.charting.plugin.DrawablePlugin;
import com.denispetrov.charting.plugin.Plugin;

/**
 * A view manages an instance of SWT Canvas and holds lists of plugins ({@link Plugin}), 
 * drawables ({@link Drawable}), and maintains a view context ({@link ViewContext})
 * 
 * Typical view initialization sequence is as follows:
 * <pre>
 * {@code 
    Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED|SWT.NO_BACKGROUND);
    View view = new View();
    view.setCanvas(canvas);
    
    // add plugins
    view.addViewPlugin(new TrackerViewPlugin());
    view.addViewPlugin(new PanViewPlugin());
    view.addViewPlugin(new ZoomViewPlugin());
    view.addViewPlugin(new ClickerViewPlugin());
    view.addViewPlugin(new DraggerViewPlugin());
    . . .
    
    // add drawables
    view.addDrawable(new ViewportBackgroundDrawable());
    view.addDrawable(new ViewportXAxisDrawable());
    view.addDrawable(new ViewportYAxisDrawable());
    view.addDrawable(new ViewportZeroMarkDrawable());
    
    view.addDrawable(new ExampleModelRectDrawable());
    view.addDrawable(new ExampleModelDraggableRectDrawable());
    . . .
    
    view.init();
    
    }
 * </pre>
 * Later, initialize view context and add it to the view
 * <pre>
 * {@code 
    // initialize view context
    ViewContext viewContext = new ViewContext();
    viewContext.setMargin(20);
    viewContext.setAllowNegativeBaseX(false);
    viewContext.setAllowNegativeBaseY(false);
    . . .

    // and add it to the view
    view.setViewContext(viewContext);
    . . .

    // set model
    view.setModel(model);
    }
 * </pre>
 * 
 */
public class View implements PaintListener, ControlListener {

    private static final Logger LOG = LoggerFactory.getLogger(View.class);

    private GC gc;
    private Canvas canvas;

    private ViewContext viewContext;
    private List<Plugin> plugins = new ArrayList<>();
    private List<DrawablePlugin> drawablePlugins = new ArrayList<>();

    private Rectangle mainAreaRectangle = new Rectangle(0, 0, 0, 0);

    public Rectangle getMainAreaRectangle() {
        return mainAreaRectangle;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
        if (DrawablePlugin.class.isAssignableFrom(plugin.getClass())) {
            drawablePlugins.add((DrawablePlugin)plugin);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }

    public void setViewContext(ViewContext viewContext) {
        this.viewContext = viewContext;
        mainAreaRectangle = calculateMainAreaRectangle();
        this.viewContext.setView(this);
        contextUpdated();
    }

    /**
     * Plugins and other code manipulating view context should call this method in the end
     * to cause the view to update its drawables and plugins and cause a redraw of the canvas
     */
    public void contextUpdated() {
        LOG.trace("Calling contextUpdated on plugins");
        for (Plugin plugin : plugins) {
            plugin.contextUpdated();
        }
        LOG.trace("Calling redraw on canvas");
        canvas.redraw();
    }

    public void init() {
        for (Plugin plugin : plugins) {
            plugin.setView(this);
        }
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.addControlListener(this);
        this.canvas.addPaintListener(this);
    }

    @Override
    public void paintControl(PaintEvent e) {
        gc = e.gc;
        LOG.trace("View paint");
        long time0 = System.nanoTime();
        for (DrawablePlugin plugin : drawablePlugins) {
            plugin.draw();
        }
        LOG.trace("Paint time {} ns", System.nanoTime() - time0);
    }

    @Override
    public void controlMoved(ControlEvent e) {
    }

    @Override
    public void controlResized(ControlEvent e) {
        onCanvasResized();
        contextUpdated();
    }

    private void onCanvasResized() {
        Rectangle oldMainAreaRectangle = mainAreaRectangle;
        mainAreaRectangle = calculateMainAreaRectangle();
        viewContext.setBaseX(
                viewContext.getBaseX()
                + viewContext.w(oldMainAreaRectangle.width) * viewContext.getResizeCenterX()
                - viewContext.w(mainAreaRectangle.width) * viewContext.getResizeCenterX());
        viewContext.setBaseY(
                viewContext.getBaseY()
                + viewContext.h(oldMainAreaRectangle.height) * viewContext.getResizeCenterY()
                - viewContext.h(mainAreaRectangle.height) * viewContext.getResizeCenterY());
        LOG.trace("Canvas resized, main area w={} h={}", mainAreaRectangle.width, mainAreaRectangle.height);
    }

    private Rectangle calculateMainAreaRectangle() {
        Rectangle canvasBounds = canvas.getBounds();
        return new Rectangle(
                viewContext.getMarginLeft(),
                viewContext.getMarginTop(),
                canvasBounds.width - viewContext.getMarginLeft() - viewContext.getMarginRight(),
                canvasBounds.height - viewContext.getMarginTop() - viewContext.getMarginBottom());
    }


    public void drawRectangle(double x, double y, double width, double height) {
        gc.drawRectangle(viewContext.x(x), viewContext.y(y + height), viewContext.w(width), viewContext.h(height));
    }

    public void drawRectangle(FRectangle rectangle) {
        gc.drawRectangle(viewContext.rectangle(rectangle));
    }

    public void drawRectangle(HRectangle rectangle) {
        gc.drawRectangle(viewContext.x(rectangle.x), viewContext.y(rectangle.y) + rectangle.h, rectangle.w, rectangle.h);
    }

    public void fillRectangle(double x, double y, double width, double height) {
        gc.fillRectangle(viewContext.x(x), viewContext.y(y + height), viewContext.w(width), viewContext.h(height));
    }

    public void fillRectangle(FRectangle rectangle) {
        gc.fillRectangle(viewContext.rectangle(rectangle));
    }

    public void fillRectangle(HRectangle rectangle) {
        gc.fillRectangle(viewContext.x(rectangle.x), viewContext.y(rectangle.y) + rectangle.h, rectangle.w, rectangle.h);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        gc.drawLine(viewContext.x(x1), viewContext.y(y1), viewContext.x(x2), viewContext.y(y2));
    }

    public void drawPolyLine(double[] polyLine) {
        int[] pointArray = new int[polyLine.length];
        for (int i = 0; i < polyLine.length; i += 2) {
            pointArray[i] = viewContext.x(polyLine[i]);
            pointArray[i + 1] = viewContext.y(polyLine[i + 1]);
        }
        gc.drawPolyline(pointArray);
    }

    public Rectangle drawImage(Image image, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        Rectangle ib = image.getBounds();
        Rectangle extent = viewContext.extent(new FPoint(x, y), new Point(ib.width, ib.height), dp);
        gc.drawImage(image, extent.x + dp.xMargin, extent.y + dp.yMargin);
        return extent;
    }

    public Rectangle drawText(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        Rectangle extent = viewContext.extent(new FPoint(x, y), gc.textExtent(text, dp.textExtentFlags), dp);
        gc.drawText(text, extent.x + dp.xMargin, extent.y + dp.yMargin, dp.isTransparent);
        return extent;
    }

    public Rectangle textRectangle(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        return viewContext.extent(new FPoint(x, y), gc.textExtent(text, dp.textExtentFlags), dp);
    }

    public HRectangle textRectangleH(String text, double x, double y, DrawParameters drawParameters) {
        DrawParameters dp = drawParameters;
        if (dp == null) {
            dp = new DrawParameters();
        }
        return viewContext.rectangleH(viewContext.extent(new FPoint(x, y), gc.textExtent(text, dp.textExtentFlags), dp));
    }

    public int getCanvasWidth() {
        return canvas.getBounds().width;
    }

    public double getWidth() {
        return viewContext.w(getCanvasWidth() - viewContext.getMarginLeft() - viewContext.getMarginRight());
    }

    public int getCanvasHeight() {
        return canvas.getBounds().height;
    }

    public double getHeight() {
        return viewContext.h(getCanvasHeight() - viewContext.getMarginTop() - viewContext.getMarginBottom());
    }

    public GC getGC() {
        return gc;
    }

    public void setGC(GC gc) {
        this.gc = gc;
    }
}
