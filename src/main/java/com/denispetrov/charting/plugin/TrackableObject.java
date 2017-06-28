package com.denispetrov.charting.plugin;

import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.view.ViewContext;

/**
 * Provides a representation of model objects for {@link com.denispetrov.charting.plugin.impl.TrackerViewPlugin}
 */
public interface TrackableObject {

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

    void contextUpdated(ViewContext viewContext);

    boolean isPixelSized();

    void setPixelSized(boolean isPixelSized);

    Rectangle getPaddedIRect();
}