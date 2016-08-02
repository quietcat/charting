package com.denispetrov.graphics.plugin;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;

public interface TrackingObject {

    FRectangle getFRect();

    void setFRect(FRectangle fRect);

    Rectangle getIRect();

    void setIRect(Rectangle iRect);

    Object getTarget();

    void setTarget(Object target);

    int getxPadding();

    void setXPadding(int xPadding);

    int getyPadding();

    void setYPadding(int yPadding);

    Reference getXReference();

    void setXReference(Reference xReference);

    Reference getYReference();

    void setYReference(Reference yReference);

    Cursor getCursor();

    void setCursor(Cursor cursor);

}