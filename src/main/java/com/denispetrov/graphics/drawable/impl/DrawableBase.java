package com.denispetrov.graphics.drawable.impl;

import com.denispetrov.graphics.drawable.Drawable;
import com.denispetrov.graphics.view.View;
import com.denispetrov.graphics.view.ViewContext;

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
    public void contextUpdated() {
    }

}
