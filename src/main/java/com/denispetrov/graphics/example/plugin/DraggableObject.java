package com.denispetrov.graphics.example.plugin;

import com.denispetrov.graphics.model.FPoint;

public interface DraggableObject {

    void setOrigin(FPoint origin);

    FPoint getOrigin();

}
