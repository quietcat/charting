package com.denispetrov.charting.plugin.impl;

import com.denispetrov.charting.plugin.ViewPlugin;
import com.denispetrov.charting.view.View;

public abstract class ViewPluginBase implements ViewPlugin {

    protected View view;

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void modelUpdated() {
    }

    @Override
    public void contextUpdated() {
    }
}