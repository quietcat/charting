package com.denispetrov.charting.layer.adapters;

import com.denispetrov.charting.layer.ModelLayer;
import com.denispetrov.charting.view.ModelAwareView;
import com.denispetrov.charting.view.View;

public class ModelLayerAdapter<M> extends LayerAdapter implements ModelLayer<M> {

    protected M model;
    protected ModelAwareView<M> view;

    @Override
    public void modelUpdated(M model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setView(View view) {
        super.setView(view);
        this.view = (ModelAwareView<M>) view;
    }
}
