package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.ViewController;

public interface ViewPlugin {

    public void setViewController(ViewController<?> controller);

    void modelUpdated();

    void contextUpdated();
}
