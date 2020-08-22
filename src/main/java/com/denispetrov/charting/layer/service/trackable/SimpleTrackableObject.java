package com.denispetrov.charting.layer.service.trackable;

import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.model.FRectangle;

public class SimpleTrackableObject implements TrackableObject {

    private final FRectangle fRect = new FRectangle(0,0,0,0);
    private Object target = null;

    @Override
    public FRectangle getFRect() {
        return fRect;
    }

    public void setFRect(FRectangle fRect) {
        FRectangle.copyRect(fRect, this.fRect);
    }

    @Override
    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
