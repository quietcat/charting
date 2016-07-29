package com.denispetrov.graphics;



public abstract class ModelDrawerBase<T> implements ModelDrawable<T> {

    protected ViewContext<T> vc;
    protected int rank = 0;

    @Override
    public void setViewContext(ViewContext<T> vc) {
        this.vc = vc;
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
