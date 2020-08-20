package com.denispetrov.charting.layer.adapters;

import com.denispetrov.charting.layer.ModelLayer;

public class ModelLayerAdapter<M> extends LayerAdapter implements ModelLayer<M> {

    protected M model;

    @Override
    public void modelUpdated(M model) {
        this.model = model;
    }

}
