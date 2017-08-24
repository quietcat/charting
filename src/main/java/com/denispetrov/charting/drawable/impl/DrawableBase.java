package com.denispetrov.charting.drawable.impl;

import com.denispetrov.charting.drawable.Drawable;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public abstract class DrawableBase implements Drawable {

    public static final int DEFAULT_MODEL_DRAWABLE_RANK = 1000;
    protected ViewContext viewContext;
    protected View view;
    protected int rank = DEFAULT_MODEL_DRAWABLE_RANK;

    @Override
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void setViewContext(ViewContext vc) {
        this.viewContext = vc;
    }

    @Override
    public void modelUpdated() {
    }

    @Override
    public void modelUpdated(Object component, Object item) {
    }

    @Override
    public void contextUpdated() {
    }

}