package com.denispetrov.graphics.plugin.impl;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.graphics.plugin.Trackable;
import com.denispetrov.graphics.plugin.TrackableObject;
import com.denispetrov.graphics.view.View;
import com.denispetrov.graphics.view.ViewContext;

/**
 * Provides a list of objects under mouse cursor, and changes mouse cursor to that provided by the corresponding
 * implementation of {@link com.denispetrov.graphics.plugin.Trackable} Other plugins should avoid implementing their own
 * tracking mechanisms to reduce the amount of work that must be done on mouse move events.
 * 
 * See {@link com.denispetrov.graphics.plugin.impl.ClickerViewPlugin} for a use example
 */
public class TrackerViewPlugin extends ViewPluginBase implements MouseMoveListener {
    private static final Logger LOG = LoggerFactory.getLogger(TrackerViewPlugin.class);

    enum MouseFn {
        NONE, TRACK
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Map<Trackable, Set<TrackableObject>> objectsBeingTracked = new HashMap<>();

    private Cursor cursorDefault;

    private Cursor cursorTrack;

    private Trackable firstTrackableUnderMouse = null;

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseMoveListener(this);
        cursorTrack = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
        cursorDefault = view.getCanvas().getCursor();
    }

    public Collection<TrackableObject> getTrackables(Trackable trackable) {
        return objectsBeingTracked.get(trackable);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext viewContext = view.getViewContext();
        if (isTracking(e.x, e.y)) {
            if (mouseFn == MouseFn.NONE) {
                if (firstTrackableUnderMouse.getCursor() != null) {
                    viewContext.getCanvas().setCursor(firstTrackableUnderMouse.getCursor());
                } else {
                    viewContext.getCanvas().setCursor(cursorTrack);
                }
                mouseFn = MouseFn.TRACK;
            }
        } else {
            if (mouseFn == MouseFn.TRACK) {
                viewContext.getCanvas().setCursor(cursorDefault);
                mouseFn = MouseFn.NONE;
            }
        }
    }

    private static boolean isIn(ViewContext viewContext, int x, int y, TrackableObject to) {
        if (!viewContext.getMainAreaRectangle().contains(x, y)) {
            return false;
        }
        return to.getPaddedIRect().contains(x,y);
    }

    public boolean isTracking(int x, int y) {
        ViewContext viewContext = view.getViewContext();
        for (Trackable t : objectsBeingTracked.keySet()) {
            Set<TrackableObject> trackable = objectsBeingTracked.get(t);
            for (TrackableObject to : trackable) {
                if (isIn(viewContext, x, y, to)) {
                    firstTrackableUnderMouse = t;
                    return true;
                }
            }
        }
        firstTrackableUnderMouse = null;
        return false;
    }

    public Map<Trackable, Set<TrackableObject>> getObjectsUnderMouse(int x, int y) {
        Map<Trackable, Set<TrackableObject>> objectsUnderMouse = new HashMap<>();
        ViewContext viewContext = view.getViewContext();
        objectsUnderMouse.clear();
        for (Trackable t : objectsBeingTracked.keySet()) {
            Set<TrackableObject> trackable = objectsBeingTracked.get(t);
            for (TrackableObject to : trackable) {
                if (isIn(viewContext, x, y, to)) {
                    Set<TrackableObject> trackingObjectsForTrackable = objectsUnderMouse.get(t);
                    if (trackingObjectsForTrackable == null) {
                        trackingObjectsForTrackable = new HashSet<>();
                        objectsUnderMouse.put(t, trackingObjectsForTrackable);
                    }
                    trackingObjectsForTrackable.add(to);
                }
            }
        }
        return objectsUnderMouse;
    }

    public void clearTrackingObjects(Trackable trackable) {
        objectsBeingTracked.remove(trackable);
    }

    public void addTrackingObject(Trackable trackable, TrackableObject trackingObject) {
        ViewContext viewContext = view.getViewContext();
        Set<TrackableObject> objects = objectsBeingTracked.get(trackable);
        if (objects == null) {
            objects = new HashSet<>();
            objectsBeingTracked.put(trackable, objects);
        }
        objects.add(trackingObject);
        trackingObject.contextUpdated(viewContext);
    }

    @Override
    public void contextUpdated() {
        ViewContext viewContext = view.getViewContext();
        for (Set<TrackableObject> trackable : objectsBeingTracked.values()) {
            for (TrackableObject trackingObject : trackable) {
                trackingObject.contextUpdated(viewContext);
            }
        }
    }

}
