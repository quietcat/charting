package com.denispetrov.graphics.plugin;

import com.denispetrov.graphics.example.model.DraggableRectangle;
import com.denispetrov.graphics.example.plugin.DraggableObject;
import com.denispetrov.graphics.model.FPoint;

public class SimpleDraggableObject extends SimpleTrackableObject implements DraggableObject {

    private FPoint origin = new FPoint(0,0);

    @Override
    public void setOrigin(FPoint origin) {
        this.origin = origin;
        if (DraggableRectangle.class.isAssignableFrom(getTarget().getClass())) {
            DraggableRectangle target = (DraggableRectangle)getTarget();
            target.x = origin.x;
            target.y = origin.y;
        }
    }

    @Override
    public FPoint getOrigin() {
        if (DraggableRectangle.class.isAssignableFrom(getTarget().getClass())) {
            DraggableRectangle target = (DraggableRectangle)getTarget();
            origin.x = target.x;
            origin.y = target.y;
        }
        return origin;
    }

}
