package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.model.FPoint;

public interface Draggable {

    void setOrigin(Object object, FPoint origin);

    FPoint getOrigin(Object object);
}
