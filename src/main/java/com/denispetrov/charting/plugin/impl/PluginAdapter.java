package com.denispetrov.charting.plugin.impl;

import com.denispetrov.charting.plugin.Plugin;
import com.denispetrov.charting.view.View;

public class PluginAdapter implements Plugin {

    protected View view;
    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void contextUpdated() {
    }

}
