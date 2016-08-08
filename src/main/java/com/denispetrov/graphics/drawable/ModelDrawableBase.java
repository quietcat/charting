package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;



public abstract class ModelDrawableBase<T> implements ModelDrawable<T> {

    public static final int DEFAULT_MODEL_DRAWABLE_RANK = 1000;
    protected ViewContext<T> viewContext;
    protected int rank = DEFAULT_MODEL_DRAWABLE_RANK;

    @Override
    public void setViewContext(ViewContext<T> vc) {
        this.viewContext = vc;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

}
