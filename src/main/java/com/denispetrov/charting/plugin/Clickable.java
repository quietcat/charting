package com.denispetrov.charting.plugin;

import java.util.Set;

public interface Clickable {

    void objectClicked(Set<TrackableObject> objects, int button);

    void mouseDown(Set<TrackableObject> objects, int button, int x, int y);

    void mouseUp(int button, int x, int y);
}
