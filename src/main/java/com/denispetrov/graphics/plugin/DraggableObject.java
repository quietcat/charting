package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.model.FRectangle;

public interface DraggableObject {

    void setOrigin(double originX);

    FRectangle getOrigin();

}
