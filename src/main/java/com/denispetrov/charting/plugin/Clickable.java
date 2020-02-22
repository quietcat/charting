package com.denispetrov.charting.plugin;

public interface Clickable {

    void objectClicked(TrackableObject object, int button);

    void mouseDown(TrackableObject object, int button, int x, int y);

    void mouseUp(int button, int x, int y);
}
