package com.denispetrov.graphics.view;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import com.denispetrov.graphics.drawable.Drawable;
import com.denispetrov.graphics.plugin.ViewPlugin;

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
public class View implements PaintListener, ControlListener {

    private Canvas canvas;

    private ViewContext viewContext;
    private List<Drawable> drawables = new LinkedList<>();
    private List<ViewPlugin> viewPlugins = new LinkedList<>();

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

    /**
     * Plugins and other code manipulating view context should call this method in the end
     * to cause the view to update its drawables and plugins and cause a redraw of the canvas
     */
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
        this.canvas.addControlListener(this);
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

    /**
     * Trigger a narrow update of one item managed by a specific component. This method
     * helps reduce the amount of code that needs to run when only one component is updated
     * as a result of some interactive action, for example. The exact semantics of
     * component and item is left to the implementation.
     * @param component The component reference that helps narrow down the scope of the update
     * @param item The item that has changed
     */
    public void modelUpdated(Object component, Object item) {
        for (ViewPlugin viewPlugin : viewPlugins) {
            viewPlugin.modelUpdated(component, item);
        }
        for (Drawable drawable : drawables) {
            drawable.modelUpdated(component, item);
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

    @Override
    public void controlMoved(ControlEvent e) {
    }

    @Override
    public void controlResized(ControlEvent e) {
        contextUpdated();
    }
}
