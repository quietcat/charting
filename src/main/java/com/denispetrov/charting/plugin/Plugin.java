package com.denispetrov.charting.plugin;

import com.denispetrov.charting.view.View;

public interface Plugin<M> {

    void setView(View<M> view);

    void modelUpdated();

    void modelUpdated(Object component, Object item);

    void contextUpdated();

    void draw(View<M> view, M model);
}
