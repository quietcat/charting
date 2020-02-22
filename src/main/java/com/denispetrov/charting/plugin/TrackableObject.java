package com.denispetrov.charting.plugin;

import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.view.ViewContext;

/**
 * Provides a representation of model objects for {@link com.denispetrov.charting.plugin.impl.TrackerViewPlugin}
 */
public interface TrackableObject {

    FRectangle getFRect();

    TrackableObject setFRect(FRectangle fRect);

    Rectangle getIRect();

    TrackableObject setIRect(Rectangle iRect);

    Object getTarget();

    TrackableObject setTarget(Object target);

    int getxPadding();

    TrackableObject setXPadding(int xPadding);

    int getyPadding();

    TrackableObject setYPadding(int yPadding);

    void contextUpdated(ViewContext viewContext);

    boolean isPixelSized();

    TrackableObject setPixelSized(boolean isPixelSized);

    Rectangle getPaddedIRect();

    int getRank();
    
    TrackableObject setRank(int rank);

    Trackable getTrackable();
    
    TrackableObject setTrackable(Trackable trackable);
}