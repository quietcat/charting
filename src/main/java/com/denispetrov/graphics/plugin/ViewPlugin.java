package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.ViewController;

public interface ViewPlugin {

    public void init(ViewController<?> controller);

    void modelUpdated();

    void contextUpdated();
}
