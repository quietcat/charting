package com.denispetrov.charting.layer;

import com.denispetrov.charting.view.View;

public interface Layer {

    void setView(View view);

    void contextUpdated();

}
