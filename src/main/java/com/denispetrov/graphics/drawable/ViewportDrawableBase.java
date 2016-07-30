package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;

public abstract class ViewportDrawableBase implements ViewportDrawable {

    protected ViewContext<?> vc;
    protected int rank = 0;

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public void setViewContext(ViewContext<?> vc) {
        this.vc = vc;
    }

}
