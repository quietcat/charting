package com.denispetrov.graphics;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

public class ViewController<T> implements PaintListener {

    private Canvas canvas;

    private ViewContext<T> viewContext;
    private List<ModelDrawable<T>> modelDrawers = new LinkedList<>();

    private List<ViewportDrawer> viewportDrawers = new LinkedList<>();
    private List<Drawable> rankedDrawables = new LinkedList<>();
    private List<ViewPlugin> viewPlugins = new LinkedList<>();

    Object mouseObject = null;

    public List<ModelDrawable<T>> getModelDrawers() {
        return modelDrawers;
    }

    public List<ViewportDrawer> getViewportDrawers() {
        return viewportDrawers;
    }

    public List<ViewPlugin> getViewPlugins() {
        return viewPlugins;
    }

    public void addModelDrawer(ModelDrawable<T> handler) {
        modelDrawers.add(handler);
    }

    public void addViewportDrawer(ViewportDrawer handler) {
        viewportDrawers.add(handler);
    }

    public void addViewPlugin(ViewPlugin viewPlugin) {
        viewPlugins.add(viewPlugin);
    }

    public ViewPlugin findPlugin(Class<? extends ViewPlugin> pluginClass) {
        for (ViewPlugin viewPlugin : viewPlugins) {
            if (pluginClass.isAssignableFrom(viewPlugin.getClass())) {
                return viewPlugin;
            }
        }
        return null;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public ViewContext<T> getViewContext() {
        return viewContext;
    }

    public void setViewContext(ViewContext<T> vc) {
        this.viewContext = vc;
        this.viewContext.setCanvas(canvas);
        for (ModelDrawable<T> modelDrawer : modelDrawers) {
            modelDrawer.setViewContext(viewContext);
        }

        for (ViewportDrawer viewportDrawer : viewportDrawers) {
            viewportDrawer.setViewContext(viewContext);
        }
        contextUpdated();
    }

    public void contextUpdated() {
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.contextUpdated();
        }
        for (ModelDrawable<T> modelDrawer : modelDrawers) {
            modelDrawer.contextUpdated();
        }

        for (ViewportDrawer viewportDrawer : viewportDrawers) {
            viewportDrawer.contextUpdated();
        }
        canvas.redraw();
    }

    public void init() {
        rankedDrawables.addAll(modelDrawers);
        rankedDrawables.addAll(viewportDrawers);
        rankedDrawables.sort(new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Integer.compare(o1.getRank(), o2.getRank());
            }
        });
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.init(this);
        }
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.addPaintListener(this);
    }

    public void setModel(T model) {
        this.viewContext.setModel(model);
        modelUpdated();
    }

    public void modelUpdated() {
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.modelUpdated();
        }
        for (ModelDrawable<T> modelDrawer : modelDrawers) {
            modelDrawer.modelUpdated();
        }
        canvas.redraw();
    }

    @Override
    public void paintControl(PaintEvent e) {
        viewContext.setGC(e.gc);
        for (Drawable h : rankedDrawables) {
            h.draw();
        }
    }
}

//public void paint(GC gc) {
//        dp.reset();
//        drawAxes(gc);
//        drawZero(gc);
//        Color savedBackground = e.gc.getBackground();
//        gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
//        draw1(gc, 200, 100);
//        e.gc.setBackground(savedBackground);
//        dp.reset();
//        dp.xMargin = 5;
//        dp.yMargin = 5;
//        draw1(gc, 200, 300);
//        dp.reset();
//        dp.isTransparent = true;
//        dp.xAnchor = XAnchor.MIDDLE;
//        dp.yAnchor = YAnchor.MIDDLE;
//        grc.drawLine(200, 400, 300, 400);
//        grc.drawText("Transparent", 250, 400, dp);
//        grc.drawImage(image, 200, 200, null);
//}

