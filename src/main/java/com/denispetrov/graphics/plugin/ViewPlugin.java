package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.View;

public interface ViewPlugin {

    public void setView(View view);

    void modelUpdated();

    void contextUpdated();
}
