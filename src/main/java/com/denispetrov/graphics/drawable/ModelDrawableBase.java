package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;



public abstract class ModelDrawableBase<T> implements ModelDrawable<T> {

    protected ViewContext<T> viewContext;
    protected int rank = 0;

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
