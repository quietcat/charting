package com.denispetrov.graphics.plugin;

import java.util.Set;
import org.eclipse.swt.graphics.Cursor;

public interface Trackable {

    void objectClicked(Set<TrackableObject> objects, int button);

    void setCursor(Cursor cursor);

    Cursor getCursor();
}