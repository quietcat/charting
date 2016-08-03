package com.denispetrov.graphics.example.model;

import com.denispetrov.graphics.model.FRectangle;

public class DraggableRectangle extends FRectangle {

    private boolean isBeingDragged = false;

    public DraggableRectangle(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    public DraggableRectangle(FRectangle rect) {
        super(rect);
    }

    public boolean isBeingDragged() {
        return isBeingDragged;
    }

    public void setBeingDragged(boolean isBeingDragged) {
        this.isBeingDragged = isBeingDragged;
    }

}
