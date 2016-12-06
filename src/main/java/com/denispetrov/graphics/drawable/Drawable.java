package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.view.View;
import com.denispetrov.graphics.view.ViewContext;

/**
 * Objects implementing this interface provide graphical representation of
 * a part of the model or visual elements of the display (e.g. coordinate axes, buttons etc.).
 * For example, for a model that represents a stock price chart, one Drawable may
 * be responsible for drawing the price chart, another for drawing volume chart,
 * and yet another for drawing some symbols representing placed orders.
 */
public interface Drawable {

    /**
     * @param rank
     * 
     * Rank determines the order in which drawables' draw() methods are invoked
     * in a given view. The lower the rank, the earlier the drawing will occur.
     * For drawables with equal rank, the order is not determined.
     * Drawables are sorted once during view initialization, so the rank should
     * be set before {@link View#init()} is called during initialization
     */
    int getRank();

    /**
     * Called to perform the actual drawing in a {@link org.eclipse.swt.graphics.GC}
     * available via an earlier call to {@link #setViewContext(ViewContext)}.
     */
    void draw();

    /**
     * Called when the model has been notified of global changes in the model. A drawable
     * may use this call to instantiate its caches or other internal structures that
     * correspond to the model objects.
     */
    void modelUpdated();

    /**
     * Called when the model has been notified of changes in a particular object in the model. A drawable
     * may use this call to update its caches or to interact with plugins. For example,
     * a drawable responsible for an interactive object may update its coordinates
     * registered with a plugin that handles mouse events.
     * @param component The component reference that helps narrow down the scope of the update
     * @param item The item that has changed
     */
    void modelUpdated(Object component, Object item);

    /**
     * Called when a context has been updated (e.g. scale or origin) or an entirely new context has been set.
     */
    void contextUpdated();

    /**
     * Called during view instantiation to associate this drawable with the view.
     * @param view The view owning the drawable
     */
    void setView(View view);

    /**
     * Called when a new context has been set on the view.
     * @param viewContext The new view context
     */
    void setViewContext(ViewContext viewContext);
}