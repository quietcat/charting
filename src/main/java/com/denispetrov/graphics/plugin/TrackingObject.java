package com.denispetrov.graphics.plugin;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;

public interface TrackingObject {

    FRectangle getfRect();

    void setfRect(FRectangle fRect);

    Rectangle getiRect();

    void setiRect(Rectangle iRect);

    Object getTarget();

    void setTarget(Object target);

    int getxPadding();

    void setxPadding(int xPadding);

    int getyPadding();

    void setyPadding(int yPadding);

    Reference getxReference();

    void setxReference(Reference xReference);

    Reference getyReference();

    void setyReference(Reference yReference);

    Cursor getCursor();

    void setCursor(Cursor cursor);

}