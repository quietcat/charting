package com.denispetrov.charting.plugin;

import java.util.Set;

public interface Clickable {

    void objectClicked(Set<TrackableObject> objects, int button);

}
