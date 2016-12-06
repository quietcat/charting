package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.view.View;

public interface ViewPlugin {

    public void setView(View view);

    public void modelUpdated();

    void modelUpdated(Object component, Object item);

    void contextUpdated();

}
