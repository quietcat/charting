package com.denispetrov.charting.plugin;

import com.denispetrov.charting.view.View;

public interface Plugin {

    void setView(View view);

    void contextUpdated();

}
