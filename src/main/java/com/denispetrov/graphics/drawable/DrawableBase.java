package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.View;

public abstract class DrawableBase implements Drawable {

    public static final int DEFAULT_MODEL_DRAWABLE_RANK = 1000;
    protected ViewContext viewContext;
    protected View view;
    protected int rank = DEFAULT_MODEL_DRAWABLE_RANK;

    @Override
    public int getRank() {
        return rank;
    }

    @Override
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
