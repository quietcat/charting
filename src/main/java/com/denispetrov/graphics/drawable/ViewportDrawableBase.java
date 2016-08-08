package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;

public abstract class ViewportDrawableBase implements ViewportDrawable {

    public static final int DEFAULT_VIEWPORT_DRAWABLE_RANK = 10000;
    protected ViewContext<?> viewContext;
    protected ViewController<?> viewController;
    protected int rank = DEFAULT_VIEWPORT_DRAWABLE_RANK;

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public void setViewContext(ViewContext<?> viewContext) {
        this.viewContext = viewContext;
    }

    @Override
    public void setViewController(ViewController<?> viewController) {
        this.viewController = viewController;
    }

}
