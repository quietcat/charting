package com.denispetrov.graphics.plugin.impl;

import com.denispetrov.graphics.plugin.ViewPlugin;
import com.denispetrov.graphics.view.View;

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
    public void modelUpdated(Object component, Object item) {
    }

    @Override
    public void contextUpdated() {
    }
}