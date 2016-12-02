package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.view.View;

public interface ViewPlugin {

    public void setView(View view);

    void modelUpdated();

    void contextUpdated();
}
