package com.denispetrov.graphics;

public abstract class ViewPluginBase implements ViewPlugin {

    protected ViewController<?> controller;

    @Override
    public void init(ViewController<?> controller) {
        this.controller = controller;
    }

    public ViewController<?> getController() {
        return controller;
    }

    public void setController(ViewController<?> controller) {
        this.controller = controller;
    }

}