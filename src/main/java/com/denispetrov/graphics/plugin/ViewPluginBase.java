package com.denispetrov.graphics.plugin;

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
    public void contextUpdated() {
    }
}