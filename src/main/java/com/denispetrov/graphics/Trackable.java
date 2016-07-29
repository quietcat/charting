package com.denispetrov.graphics;

import java.util.Set;


public interface Trackable {

    void setObjectTracker(ObjectTrackerViewPlugin objectTracker);

    void objectClicked(Set<TrackingObject> objects);
}