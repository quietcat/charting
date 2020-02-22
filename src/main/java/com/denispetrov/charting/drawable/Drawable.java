package com.denispetrov.charting.drawable;

import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

/**
 * Objects implementing this interface provide graphical representation of
 * a part of the model or visual elements of the display (e.g. coordinate axes, buttons etc.).
 * For example, for a model that represents a stock price chart, one Drawable may
 * be responsible for drawing the price chart, another for drawing volume chart,
 * and yet another for drawing some symbols representing placed orders.
 */
public interface Drawable {

    /**
     * @return rank of the drawable for sorting
     */
    int getRank();

    /**
     * @param rank
     * 
     * Rank determines the order in which drawables' draw() methods are invoked
     * in a given view. The lower the rank, the earlier the drawing will occur.
     * For drawables with equal rank, the order is not determined.
     * Drawables are sorted once during view initialization, so the rank should
     * be set before {@link View#init()} is called during initialization.
     * {@link View#addDrawable(Drawable)} assigns a default rank to ensure
     * drawables are called in the order they're initialized.
     */
    void setRank(int rank);

    /**
     * Called on all drawables prior to actual drawing. This helps implement dependencies
     * between drawables
     */
    void preDraw();
    
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