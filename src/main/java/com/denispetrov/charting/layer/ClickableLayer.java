package com.denispetrov.charting.layer;

public interface ClickableLayer extends TrackableLayer {

    void objectClicked(TrackableObject object, int button);

    void mouseDown(TrackableObject object, int button, int x, int y);

    void mouseUp(int button, int x, int y);
}
