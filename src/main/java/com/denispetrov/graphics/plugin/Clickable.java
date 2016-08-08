package com.denispetrov.graphics.plugin;

import java.util.Set;

public interface Clickable {

    void objectClicked(Set<TrackableObject> objects, int button);

}
