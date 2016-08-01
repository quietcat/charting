package com.denispetrov.graphics.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;
import com.denispetrov.graphics.drawable.ModelDrawable;

public class TrackerViewPlugin extends ViewPluginBase implements MouseMoveListener, MouseListener {

    enum MouseFn {
        NONE, TRACK, BUTTON_DOWN
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Map<Trackable,Set<TrackingObject>> objectsBeingTracked = new HashMap<>();

    private Map<Trackable,Set<TrackingObject>> objectsOnMouseDown = new HashMap<>();
    private Point mouseCoordinatesOnMouseDown;

    private Cursor cursorDefault;

    private Cursor cursorTrack;

    @Override
    public void init(ViewController<?> controller) {
        super.init(controller);
        controller.getCanvas().addMouseMoveListener(this);
        controller.getCanvas().addMouseListener(this);
        for (ModelDrawable<?> modelDrawable : controller.getModelDrawables()) {
            if (Trackable.class.isAssignableFrom(modelDrawable.getClass())) {
                ((Trackable)modelDrawable).setObjectTracker(this);
            }
        }
        cursorTrack = controller.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
        cursorDefault = controller.getCanvas().getCursor();
    }

    public Collection<TrackingObject> getTrackables(Trackable trackable) {
        return objectsBeingTracked.get(trackable);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext<?> viewContext = controller.getViewContext();
        if (isTracking(e.x, e.y)) {
            if (mouseFn == MouseFn.NONE) {
                viewContext.getCanvas().setCursor(cursorTrack);
                mouseFn = MouseFn.TRACK;
            }
        } else {
            if (mouseFn == MouseFn.TRACK) {
                viewContext.getCanvas().setCursor(cursorDefault);
                mouseFn = MouseFn.NONE;
            }
        }
    }

    private static boolean isIn(ViewContext<?> viewContext, int x, int y, TrackingObject to) {
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
            Set<TrackingObject> trackable = objectsBeingTracked.get(t);
            for (TrackingObject to : trackable) {
                if (isIn(viewContext, x, y, to)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Trackable,Set<TrackingObject>> getObjectsUnderMouse(int x, int y) {
        Map<Trackable,Set<TrackingObject>> objectsUnderMouse = new HashMap<>();
        ViewContext<?> viewContext = controller.getViewContext();
        objectsUnderMouse.clear();
        for (Trackable t : objectsBeingTracked.keySet()) {
            Set<TrackingObject> trackable = objectsBeingTracked.get(t);
            for (TrackingObject to : trackable) {
                if (isIn(viewContext, x, y, to)) {
                    Set<TrackingObject> trackingObjectsForTrackable = objectsUnderMouse.get(t);
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

    public void addTrackingObject(Trackable trackable, TrackingObject trackingObject) {
        ViewContext<?> viewContext = controller.getViewContext();
        Set<TrackingObject> objects = objectsBeingTracked.get(trackable);
        if (objects == null) {
            objects = new HashSet<>();
            objectsBeingTracked.put(trackable, objects);
        }
        trackingObject.iRect = viewContext.rectangle(trackingObject.fRect);
        objects.add(trackingObject);
    }

    @Override
    public void modelUpdated() {
    }

    @Override
    public void contextUpdated() {
        ViewContext<?> viewContext = controller.getViewContext();
        for (Set<TrackingObject> trackable : objectsBeingTracked.values()) {
            for (TrackingObject trackingObject : trackable) {
                trackingObject.iRect = viewContext.rectangle(trackingObject.fRect);
            }
        }
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        System.out.println("mouseDown, button =" + e.button);
        if (e.button == 1 && mouseFn == MouseFn.TRACK) {
            objectsOnMouseDown = getObjectsUnderMouse(e.x, e.y);
            mouseCoordinatesOnMouseDown = new Point(e.x, e.y);
            mouseFn = MouseFn.BUTTON_DOWN;
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        System.out.println("mouseUp, button =" + e.button);
        if (e.button == 1 && mouseFn == MouseFn.BUTTON_DOWN) {
            ViewContext<?> viewContext = controller.getViewContext();
            if (Math.abs(e.x - mouseCoordinatesOnMouseDown.x) < viewContext.getDragThreshold()
                    && Math.abs(e.y - mouseCoordinatesOnMouseDown.y) < viewContext.getDragThreshold()) {
                for (Trackable t : objectsOnMouseDown.keySet()) {
                    t.objectClicked(objectsOnMouseDown.get(t));
                }
            }
        }
        mouseFn = MouseFn.NONE;
    }

}
