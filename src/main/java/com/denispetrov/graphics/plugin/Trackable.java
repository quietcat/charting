package com.denispetrov.graphics.plugin;

import java.util.Set;


public interface Trackable {

    void setObjectTracker(TrackerViewPlugin objectTracker);

    void objectClicked(Set<SimpleTrackingObject> objects, int button);
}