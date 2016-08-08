package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.View;

public abstract class PluginBase implements Plugin {

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