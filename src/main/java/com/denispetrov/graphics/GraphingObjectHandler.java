package com.denispetrov.graphics;

import java.util.Set;

import com.denispetrov.graphics.GraphingController.TrackingObject;

public interface GraphingObjectHandler<T> {

    int getRank();

    void setGraphingContext(GraphingContext grc);

    void draw();

    Set<TrackingObject> getTrackingObjects();
}
