package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;

public abstract class ViewportDrawableBase implements ViewportDrawable {

    public static final int DEFAULT_VIEWPORT_DRAWABLE_RANK = 10000;
    protected ViewContext<?> vc;
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
    public void setViewContext(ViewContext<?> vc) {
        this.vc = vc;
    }

}
