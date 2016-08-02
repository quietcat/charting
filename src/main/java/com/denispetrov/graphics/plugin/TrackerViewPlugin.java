package com.denispetrov.graphics.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;
import com.denispetrov.graphics.drawable.ModelDrawable;

public class TrackerViewPlugin extends ViewPluginBase implements MouseMoveListener {

    enum MouseFn {
        NONE, TRACK
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Map<Trackable,Set<SimpleTrackingObject>> objectsBeingTracked = new HashMap<>();

    private Cursor cursorDefault;

    private Cursor cursorTrack;

    private SimpleTrackingObject firstObjectUnderMouse = null;

    @Override
    public void init(ViewController<?> controller) {
        super.init(controller);
        controller.getCanvas().addMouseMoveListener(this);
        for (ModelDrawable<?> modelDrawable : controller.getModelDrawables()) {
            if (Trackable.class.isAssignableFrom(modelDrawable.getClass())) {
                ((Trackable)modelDrawable).setObjectTracker(this);
            }
        }
        cursorTrack = controller.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
        cursorDefault = controller.getCanvas().getCursor();
    }

    public Collection<SimpleTrackingObject> getTrackables(Trackable trackable) {
        return objectsBeingTracked.get(trackable);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext<?> viewContext = controller.getViewContext();
        if (isTracking(e.x, e.y)) {
            if (mouseFn == MouseFn.NONE) {
                if (firstObjectUnderMouse.cursor != null) {
                    viewContext.getCanvas().setCursor(firstObjectUnderMouse.cursor);
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

    private static boolean isIn(ViewContext<?> viewContext, int x, int y, SimpleTrackingObject to) {
        if (!viewContext.getMainAreaRectangle().contains(x,y)) {
            return false;
        }
        switch (to.xReference) {
        case GRAPH:
            if (viewContext.x(x + to.xPadding) < to.fRect.x
                    || viewContext.x(x - to.xPadding) >= to.fRect.x + to.fRect.w) {
                return false;
            }
            break;
        case CANVAS:
            if (x + to.xPadding < to.iRect.x || x - to.xPadding >= to.iRect.x + to.iRect.width) {
                return false;
            }
            break;
        }

        switch (to.yReference) {
        case GRAPH:
            if (viewContext.y(y + to.yPadding) < to.fRect.y
                    || viewContext.y(y - to.yPadding) >= to.fRect.y + to.fRect.h) {
                return false;
            }
            break;
        case CANVAS:
            if (y + to.yPadding < to.fRect.y || y - to.yPadding >= to.fRect.y + to.fRect.h) {
                return false;
            }
            break;
        }
        return true;
    }

    public boolean isTracking(int x, int y) {
        ViewContext<?> viewContext = controller.getViewContext();
        for (Trackable t : objectsBeingTracked.keySet()) {
            Set<SimpleTrackingObject> trackable = objectsBeingTracked.get(t);
            for (SimpleTrackingObject to : trackable) {
                if (isIn(viewContext, x, y, to)) {
                    firstObjectUnderMouse = to;
                    return true;
                }
            }
        }
        firstObjectUnderMouse = null;
        return false;
    }

    public Map<Trackable,Set<SimpleTrackingObject>> getObjectsUnderMouse(int x, int y) {
        Map<Trackable,Set<SimpleTrackingObject>> objectsUnderMouse = new HashMap<>();
        ViewContext<?> viewContext = controller.getViewContext();
        objectsUnderMouse.clear();
        for (Trackable t : objectsBeingTracked.keySet()) {
            Set<SimpleTrackingObject> trackable = objectsBeingTracked.get(t);
            for (SimpleTrackingObject to : trackable) {
                if (isIn(viewContext, x, y, to)) {
                    Set<SimpleTrackingObject> trackingObjectsForTrackable = objectsUnderMouse.get(t);
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

    public void addTrackingObject(Trackable trackable, SimpleTrackingObject trackingObject) {
        ViewContext<?> viewContext = controller.getViewContext();
        Set<SimpleTrackingObject> objects = objectsBeingTracked.get(trackable);
        if (objects == null) {
            objects = new HashSet<>();
            objectsBeingTracked.put(trackable, objects);
        }
        trackingObject.iRect = viewContext.rectangle(trackingObject.fRect);
        objects.add(trackingObject);
    }

    @Override
    public void contextUpdated() {
        ViewContext<?> viewContext = controller.getViewContext();
        for (Set<SimpleTrackingObject> trackable : objectsBeingTracked.values()) {
            for (SimpleTrackingObject trackingObject : trackable) {
                trackingObject.iRect = viewContext.rectangle(trackingObject.fRect);
            }
        }
    }

}
