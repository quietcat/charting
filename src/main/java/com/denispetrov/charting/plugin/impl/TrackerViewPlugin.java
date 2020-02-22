package com.denispetrov.charting.plugin.impl;

import java.util.ArrayList;
import java.util.List;

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
public class TrackerViewPlugin extends ViewPluginBase implements MouseMoveListener {
    private static final Logger LOG = LoggerFactory.getLogger(TrackerViewPlugin.class);

    enum MouseFn {
        NONE, TRACK
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private List<TrackableObject> objectsBeingTracked = new ArrayList<TrackableObject>(1024);
    private Cursor cursorDefault = Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW);
    private Cursor cursorDefaultTrack = Display.getDefault().getSystemCursor(SWT.CURSOR_HAND);
    private Cursor cursorTrack = Display.getDefault().getSystemCursor(SWT.CURSOR_HAND);
    private Cursor cursorHidden;
    private boolean cursorIsHidden = false;
    private Point mouseXY = new Point(0, 0);
    private TrackableObject topTrackableObjectUnderMouse = null;

    @Override
    public void setView(View view) {
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

    @Override
    public void mouseMove(MouseEvent e) {
        mouseXY.x = e.x;
        mouseXY.y = e.y;
        checkCursor();
    }

    private void checkCursor() {
        if (isTracking(mouseXY.x, mouseXY.y)) {
            if (mouseFn == MouseFn.NONE) {
                cursorTrack = topTrackableObjectUnderMouse.getTrackable().getCursor();
                if (cursorTrack == null) {
                    cursorTrack = cursorDefaultTrack;
                }
                if (!cursorIsHidden) {
                    view.getViewContext().getCanvas().setCursor(cursorTrack);
                }
                mouseFn = MouseFn.TRACK;
            }
        } else {
            if (mouseFn == MouseFn.TRACK) {
                if (!cursorIsHidden) {
                    view.getViewContext().getCanvas().setCursor(cursorDefault);
                }
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
        for (int t = objectsBeingTracked.size(); t-- > 0;) {
        	TrackableObject to = objectsBeingTracked.get(t);
            if (isIn(viewContext, x, y, to)) {
                LOG.trace("Tracking: x={} y={} tip={}", x, y, to.getIRect());
                topTrackableObjectUnderMouse = to;
                return true;
            }
        }
        topTrackableObjectUnderMouse = null;
        return false;
    }

    public List<TrackableObject> getObjectsUnderMouse(int x, int y) {
        List<TrackableObject> objectsUnderMouse = new ArrayList<>();
        ViewContext viewContext = view.getViewContext();
        objectsUnderMouse.clear();
        for (int t = objectsBeingTracked.size(); t-- > 0;) {
        	TrackableObject to = objectsBeingTracked.get(t);
            if (isIn(viewContext, x, y, to)) {
            	objectsUnderMouse.add(to);
            }
        }
        return objectsUnderMouse;
    }

    public void clearTrackableObjects(Trackable trackable) {
    	List<TrackableObject> toBeRemoved = new ArrayList<>();
    	for (TrackableObject to : objectsBeingTracked) {
    		if (to.getTrackable() == trackable) {
    			toBeRemoved.add(to);
    		}
    	}
        objectsBeingTracked.removeAll(toBeRemoved);
    }

    public void addTrackableObject(TrackableObject trackableObject) {
    	objectsBeingTracked.add(trackableObject);
        trackableObject.contextUpdated(view.getViewContext());
    }

    @Override
    public void contextUpdated() {
        ViewContext viewContext = view.getViewContext();
    	for (TrackableObject to : objectsBeingTracked) {
    		to.contextUpdated(viewContext);
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

    public Trackable getTopTrackableUnderMouse() {
    	if (topTrackableObjectUnderMouse == null) {
    		return null;
    	}
        return topTrackableObjectUnderMouse.getTrackable();
    }

    public TrackableObject getTopTrackableObjectUnderMouse() {
        return topTrackableObjectUnderMouse;
    }

	public List<TrackableObject> getObjects() {
		return this.objectsBeingTracked;
	}

}
