package com.denispetrov.charting.plugin.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.plugin.Trackable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

/**
 * Provides a list of objects under mouse cursor, and changes mouse cursor to that provided by the corresponding
 * implementation of {@link com.denispetrov.charting.plugin.Trackable} Other plugins should avoid implementing their own
 * tracking mechanisms to reduce the amount of work that must be done on mouse move events.
 * 
 * See {@link com.denispetrov.charting.plugin.impl.ClickerViewPlugin} for a use example
 */
public class TrackerViewPlugin<M> extends PluginAdapter<M> implements MouseMoveListener {
    private static final Logger LOG = LoggerFactory.getLogger(TrackerViewPlugin.class);

    enum MouseFn {
        NONE, TRACK
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private Map<Trackable, Set<TrackableObject>> objectsBeingTracked = new HashMap<>();
    private Cursor cursorDefault = Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW);
    private Cursor cursorDefaultTrack = Display.getDefault().getSystemCursor(SWT.CURSOR_HAND);
    private Cursor cursorTrack = Display.getDefault().getSystemCursor(SWT.CURSOR_HAND);
    private Cursor cursorHidden;
    private boolean cursorIsHidden = false;
    private Point mouseXY = new Point(0, 0);
    private Trackable firstTrackableUnderMouse = null;
    private TrackableObject firstTrackableObjectUnderMouse = null;

    @Override
    public void setView(View<M> view) {
        super.setView(view);
        view.getCanvas().setCursor(cursorDefault);
        view.getCanvas().addMouseMoveListener(this);
        // create a cursor with a transparent image
        Color colorWhite = view.getCanvas().getDisplay().getSystemColor(SWT.COLOR_WHITE);
        Color colorBlack = view.getCanvas().getDisplay().getSystemColor(SWT.COLOR_BLACK);
        PaletteData palette = new PaletteData(new RGB[] { colorWhite.getRGB(),
                colorBlack.getRGB() });
        ImageData sourceData = new ImageData(16, 16, 1, palette);
        sourceData.transparentPixel = 0;
        cursorHidden = new Cursor(view.getCanvas().getDisplay(), sourceData, 0, 0);
    }

    public Collection<TrackableObject> getTrackables(Trackable trackable) {
        return objectsBeingTracked.get(trackable);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        mouseXY.x = e.x;
        mouseXY.y = e.y;
        checkCursor();
    }

    private void checkCursor() {
        if (isTracking(mouseXY.x, mouseXY.y)) {
            if (mouseFn == MouseFn.NONE) {
                cursorTrack = firstTrackableUnderMouse.getCursor();
                if (cursorTrack == null) {
                    cursorTrack = cursorDefaultTrack;
                }
                if (!cursorIsHidden) {
                    view.getCanvas().setCursor(cursorTrack);
                }
                mouseFn = MouseFn.TRACK;
            }
        } else {
            if (mouseFn == MouseFn.TRACK) {
                if (!cursorIsHidden) {
                    view.getCanvas().setCursor(cursorDefault);
                }
                mouseFn = MouseFn.NONE;
            }
        }
    }

    private boolean isIn(ViewContext viewContext, int x, int y, TrackableObject to) {
        if (!view.getMainAreaRectangle().contains(x, y)) {
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
                    LOG.trace("Tracking: x={} y={} tip={}", x, y, to.getIRect());
                    firstTrackableUnderMouse = t;
                    firstTrackableObjectUnderMouse = to;
                    return true;
                }
            }
        }
        firstTrackableUnderMouse = null;
        firstTrackableObjectUnderMouse = null;
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

    public void clearTrackableObjects(Trackable trackable) {
        objectsBeingTracked.remove(trackable);
    }

    public void addTrackableObject(Trackable trackable, TrackableObject trackableObject) {
        ViewContext viewContext = view.getViewContext();
        Set<TrackableObject> objects = objectsBeingTracked.get(trackable);
        if (objects == null) {
            objects = new HashSet<>();
            objectsBeingTracked.put(trackable, objects);
        }
        objects.add(trackableObject);
        trackableObject.contextUpdated(viewContext);
    }

    @Override
    public void contextUpdated() {
        ViewContext viewContext = view.getViewContext();
        for (Set<TrackableObject> trackable : objectsBeingTracked.values()) {
            for (TrackableObject trackableObject : trackable) {
                trackableObject.contextUpdated(viewContext);
            }
        }
    }

    public Cursor getCursorDefault() {
        return cursorDefault;
    }

    public void setCursorDefault(Cursor cursorDefault) {
        this.cursorDefault = cursorDefault;
    }

    public Cursor getCursorDefaultTrack() {
        return cursorDefaultTrack;
    }

    public void setCursorDefaultTrack(Cursor cursorDefaultTrack) {
        this.cursorDefaultTrack = cursorDefaultTrack;
    }

    @Override
    protected void finalize() throws Throwable {
        cursorHidden.dispose();
        super.finalize();
    }

    public void hideCursor() {
        cursorIsHidden = true;
        view.getCanvas().setCursor(cursorHidden);
    }

    public void showCursor() {
        checkCursor();
        if (mouseFn == MouseFn.TRACK) {
            view.getCanvas().setCursor(cursorTrack);
        } else {
            view.getCanvas().setCursor(cursorDefault);
        }
        cursorIsHidden = false;
    }

    public Trackable getFirstTrackableUnderMouse() {
        return firstTrackableUnderMouse;
    }

    public TrackableObject getFirstTrackableObjectUnderMouse() {
        return firstTrackableObjectUnderMouse;
    }

}
