package com.denispetrov.graphics;

import java.util.Set;

import com.denispetrov.graphics.GraphingController.TrackingObject;

public class GraphingZeroHandler extends GraphingObjectHandlerBase<Object> {

    @Override
    public void draw() {
        grc.drawLine(-10, -10, 10, 10);
        grc.drawLine(10, -10, -10, 10);
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
