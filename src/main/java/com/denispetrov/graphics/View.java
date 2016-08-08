package com.denispetrov.graphics;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import com.denispetrov.graphics.drawable.Drawable;
import com.denispetrov.graphics.plugin.ViewPlugin;

public class View implements PaintListener {

    private Canvas canvas;

    private ViewContext viewContext;
    private List<Drawable> drawables = new LinkedList<>();
    private List<ViewPlugin> viewPlugins = new LinkedList<>();

    Object mouseObject = null;

    public List<ViewPlugin> getViewPlugins() {
        return viewPlugins;
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
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

    public ViewContext getViewContext() {
        return viewContext;
    }

    public void setViewContext(ViewContext viewContext) {
        this.viewContext = viewContext;
        this.viewContext.setCanvas(canvas);
        for (Drawable drawable : drawables) {
            drawable.setViewContext(viewContext);
        }
        contextUpdated();
    }

    public void contextUpdated() {
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.contextUpdated();
        }
        for (Drawable drawable : drawables) {
            drawable.contextUpdated();
        }
        canvas.redraw();
    }

    public void init() {
        drawables.sort(new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Integer.compare(o1.getRank(), o2.getRank());
            }
        });
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.setView(this);
        }
        for (Drawable drawable : drawables) {
            drawable.setView(this);
        }
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.addPaintListener(this);
    }

    public void setModel(Object model) {
        this.viewContext.setModel(model);
        modelUpdated();
    }

    public void modelUpdated() {
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.modelUpdated();
        }
        for (Drawable drawable : drawables) {
            drawable.modelUpdated();
        }
        canvas.redraw();
    }

    @Override
    public void paintControl(PaintEvent e) {
        viewContext.setGC(e.gc);
        for (Drawable h : drawables) {
            h.draw();
        }
    }
}
