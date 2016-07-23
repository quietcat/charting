package com.denispetrov.graphics;

import java.util.Set;

import com.denispetrov.graphics.GraphingController.TrackingObject;

public class GraphingXAxisHandler extends GraphingObjectHandlerBase<Axis> {

    @Override
    public void draw() {
        grc.drawLine(grc.getBaseX(), grc.getBaseY(), grc.getBaseX() + grc.getWidth(), grc.getBaseY());
    }

    @Override
    public int getRank() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Set<TrackingObject> getTrackingObjects() {
        // TODO Auto-generated method stub
        return null;
    }

}
