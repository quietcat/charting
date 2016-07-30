package com.denispetrov.graphics.plugin;

import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;

public class TrackingObject {
    public FRectangle fRect;
    public Rectangle iRect;
    public Object target;
    public int xPadding, yPadding;
    public Reference xReference, yReference;
}