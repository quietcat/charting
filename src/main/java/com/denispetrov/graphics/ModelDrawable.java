package com.denispetrov.graphics;

import java.util.Set;

public interface ModelDrawable<T> extends Drawable {

    void setViewContext(ViewContext<T> vc);

    Set<TrackingObject> getTrackingObjects();
}
