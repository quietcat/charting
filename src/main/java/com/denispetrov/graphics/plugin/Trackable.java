package com.denispetrov.graphics.plugin;

import java.util.Set;


public interface Trackable {

    void objectClicked(Set<TrackableObject> objects, int button);
}