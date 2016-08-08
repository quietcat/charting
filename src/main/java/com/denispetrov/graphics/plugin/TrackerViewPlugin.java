package com.denispetrov.graphics.plugin;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.View;

public class TrackerViewPlugin extends ViewPluginBase implements MouseMoveListener {

    enum MouseFn {
        NONE, TRACK
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Map<Trackable,Set<TrackableObject>> objectsBeingTracked = new HashMap<>();

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
        if (!viewContext.getMainAreaRectangle().contains(x,y)) {
            return false;
        }
        switch (to.getXReference()) {
        case CHART:
            if (viewContext.x(x + to.getxPadding()) < to.getFRect().x
                    || viewContext.x(x - to.getxPadding()) >= to.getFRect().x + to.getFRect().w) {
                return false;
            }
            break;
        case CANVAS:
            if (x + to.getxPadding() < to.getIRect().x || x - to.getxPadding() >= to.getIRect().x + to.getIRect().width) {
                return false;
            }
            break;
        }

        switch (to.getYReference()) {
        case CHART:
            if (viewContext.y(y + to.getyPadding()) < to.getFRect().y
                    || viewContext.y(y - to.getyPadding()) >= to.getFRect().y + to.getFRect().h) {
                return false;
            }
            break;
        case CANVAS:
            if (y + to.getyPadding() < to.getFRect().y || y - to.getyPadding() >= to.getFRect().y + to.getFRect().h) {
                return false;
            }
            break;
        }
        return true;
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

    public Map<Trackable,Set<TrackableObject>> getObjectsUnderMouse(int x, int y) {
        Map<Trackable,Set<TrackableObject>> objectsUnderMouse = new HashMap<>();
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
        trackingObject.setIRect(viewContext.rectangle(trackingObject.getFRect()));
        objects.add(trackingObject);
    }

    @Override
    public void contextUpdated() {
        ViewContext viewContext = view.getViewContext();
        for (Set<TrackableObject> trackable : objectsBeingTracked.values()) {
            for (TrackableObject trackingObject : trackable) {
                trackingObject.setIRect(viewContext.rectangle(trackingObject.getFRect()));
            }
        }
    }

}
