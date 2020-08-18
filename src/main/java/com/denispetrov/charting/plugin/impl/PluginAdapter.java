package com.denispetrov.charting.plugin.impl;

import com.denispetrov.charting.plugin.Plugin;
import com.denispetrov.charting.view.View;

public class PluginAdapter<M> implements Plugin<M> {

    protected View<M> view;
    @Override
    public void setView(View<M> view) {
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

    @Override
    public void draw(View<M> view, M model) {
    }

}
