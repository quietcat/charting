package com.denispetrov.graphics;

public interface ViewPlugin {

    public void init(ViewController<?> controller);

    void modelUpdated();

    void contextUpdated();
}
