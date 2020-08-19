package com.denispetrov.charting.layer.service;

import com.denispetrov.charting.layer.Layer;
import com.denispetrov.charting.view.View;

public class LayerAdapter implements Layer {

    protected View view;
    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void contextUpdated() {
    }

}
