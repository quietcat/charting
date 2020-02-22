package com.denispetrov.charting.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.Drawable;
import com.denispetrov.charting.drawable.impl.DrawableBase;
import com.denispetrov.charting.plugin.ViewPlugin;

/**
 * A view manages an instance of SWT Canvas and holds lists of plugins ({@link ViewPlugin}), 
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
public class View implements PaintListener {

    private static final Logger LOG = LoggerFactory.getLogger(View.class);

    private Canvas canvas;

    private ViewContext viewContext;
    private List<Drawable> drawables = new ArrayList<>();
    private List<ViewPlugin> viewPlugins = new ArrayList<>();

    private int drawableRankAssign = DrawableBase.DEFAULT_MODEL_DRAWABLE_RANK;

    public List<ViewPlugin> getViewPlugins() {
        return viewPlugins;
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }

    /**
     * @param drawable Drawable to be added
     * 
     * Adds the drawable to its list and if the drawable has a rank equal to
     * {@link DrawableBase#DEFAULT_MODEL_DRAWABLE_RANK} it reassigns is so that
     * drawables are called in the order they're initialized.
     */
    public void addDrawable(Drawable drawable) {
        if (drawable.getRank() == DrawableBase.DEFAULT_MODEL_DRAWABLE_RANK) {
            drawableRankAssign += 100;
            drawable.setRank(drawableRankAssign);
        }
        drawables.add(drawable);
    }

    public <S extends Drawable> S findDrawable(Class<S> drawableClass) {
        for (Drawable drawable : drawables) {
            if (drawableClass.isAssignableFrom(drawable.getClass())) {
                return drawableClass.cast(drawable);
            }
        }
        return null;
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
        this.viewContext.setView(this);
        for (Drawable drawable : drawables) {
            drawable.setViewContext(viewContext);
        }
        contextUpdated();
    }

    /**
     * Plugins and other code manipulating view context should call this method in the end
     * to cause the view to update its drawables and plugins and cause a redraw of the canvas
     */
    public void contextUpdated() {
        LOG.trace("Calling contextUpdated on plugins");
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.contextUpdated();
        }
        LOG.trace("Calling contextUpdated on drawables");
        for (Drawable drawable : drawables) {
            drawable.contextUpdated();
        }
        LOG.trace("Calling redraw on canvas");
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

    /**
     * Trigger complete update of internal structures of all plugins and drawables
     */
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
        LOG.trace("View paint");
        viewContext.setGC(e.gc);
        long time0 = System.nanoTime();
        for (Drawable h : drawables) {
            h.preDraw();
        }
        LOG.trace("Paint prep time {} ns", System.nanoTime() - time0);
        time0 = System.nanoTime();
        for (Drawable h : drawables) {
            h.draw();
        }
        LOG.trace("Paint time {} ns", System.nanoTime() - time0);
    }
}
