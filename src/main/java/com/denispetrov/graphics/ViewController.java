package com.denispetrov.graphics;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import com.denispetrov.graphics.drawable.Drawable;
import com.denispetrov.graphics.drawable.ModelDrawable;
import com.denispetrov.graphics.drawable.ViewportDrawable;
import com.denispetrov.graphics.plugin.ViewPlugin;

public class ViewController<T> implements PaintListener {

    private Canvas canvas;

    private ViewContext<T> viewContext;
    private List<ModelDrawable<T>> modelDrawers = new LinkedList<>();

    private List<ViewportDrawable> viewportDrawers = new LinkedList<>();
    private List<Drawable> rankedDrawables = new LinkedList<>();
    private List<ViewPlugin> viewPlugins = new LinkedList<>();

    Object mouseObject = null;

    public List<ModelDrawable<T>> getModelDrawables() {
        return modelDrawers;
    }

    public List<ViewportDrawable> getViewportDrawers() {
        return viewportDrawers;
    }

    public List<ViewPlugin> getViewPlugins() {
        return viewPlugins;
    }

    public void addModelDrawable(ModelDrawable<T> handler) {
        modelDrawers.add(handler);
    }

    public void addViewportDrawable(ViewportDrawable handler) {
        viewportDrawers.add(handler);
    }

    public void addViewPlugin(ViewPlugin viewPlugin) {
        viewPlugins.add(viewPlugin);
    }

    public <S extends ViewPlugin> S findPlugin(Class<S> pluginClass) {
        for (ViewPlugin viewPlugin : viewPlugins) {
            if (pluginClass.isAssignableFrom(viewPlugin.getClass())) {
                return pluginClass.cast(viewPlugin);
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

        for (ViewportDrawable viewportDrawer : viewportDrawers) {
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

        for (ViewportDrawable viewportDrawer : viewportDrawers) {
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
            viewPlugin.setViewController(this);
        }
        for (Drawable drawable : rankedDrawables) {
            drawable.setViewController(this);
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
