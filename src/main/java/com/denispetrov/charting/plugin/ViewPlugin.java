package com.denispetrov.charting.plugin;

import com.denispetrov.charting.view.View;

public interface ViewPlugin {

    public void setView(View view);

    public void modelUpdated();

    void modelUpdated(Object component, Object item);

    void contextUpdated();

}
