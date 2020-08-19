package com.denispetrov.charting.layer.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.layer.Layer;
import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.layer.service.trackable.TrackableStack;
import com.denispetrov.charting.layer.service.trackable.TrackableStackEntry;
import com.denispetrov.charting.view.View;

/**
 * Provides a list of objects under mouse cursor, and changes mouse cursor to that provided by the corresponding
 * implementation of {@link com.denispetrov.charting.layer.TrackableLayer} Other layers should avoid implementing their own
 * tracking mechanisms to reduce the amount of work that must be done on mouse move events.
 * 
 * See {@link com.denispetrov.charting.layer.service.ClickerServiceLayer} for a use example
 */
public class TrackerServiceLayer extends LayerAdapter implements MouseMoveListener {
    private static final Logger LOG = LoggerFactory.getLogger(TrackerServiceLayer.class);

    private enum MouseFn {
        NONE, TRACK
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private Cursor cursorDefault = Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW);
    private Cursor cursorDefaultTrack = Display.getDefault().getSystemCursor(SWT.CURSOR_HAND);
    private Cursor cursorHidden;
    private boolean cursorIsHidden = false;
    private TrackableLayer topTrackableUnderMouse = null;
    private TrackableObject topTrackableObjectUnderMouse = null;
    private TrackableStack trackableStack = new TrackableStack();

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().setCursor(cursorDefault);
        view.getCanvas().addMouseMoveListener(this);
        // create a cursor with a transparent image
        setupInvisibleCursor();
        for (Layer layers : view.getLayers()) {
            if (TrackableLayer.class.isAssignableFrom(layers.getClass())) {
                LOG.trace("Add trackable entry {} to stack", layers);
                trackableStack.addEntry((TrackableLayer)layers);
            }
        }
    }

    private void setupInvisibleCursor() {
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

        if (!view.getMainAreaRectangle().contains(e.x, e.y)) {
            topTrackableUnderMouse = null;
            topTrackableObjectUnderMouse = null;
            return;
        }

        double fx = view.getViewContext().x(e.x);
        double fy = view.getViewContext().y(e.y);

        for (TrackableStackEntry entry : trackableStack.getEntries()) {
            for (TrackableObject trackableObject : entry.getTrackableObjects()) {
                if (trackableObject.getFRect().contains(fx, fy)) {
                    // if two trackable objects from different trackables overlap, need to add an exit from the previous trackable
                    if (entry.getTrackable() != topTrackableUnderMouse) {
                        setMouseFn(MouseFn.NONE);
                    }
                    topTrackableUnderMouse = entry.getTrackable();
                    topTrackableObjectUnderMouse = trackableObject;
                    setMouseFn(MouseFn.TRACK);
                    return;
                }
            }
        }
        setMouseFn(MouseFn.NONE);
        topTrackableUnderMouse = null;
        topTrackableObjectUnderMouse = null;
    }


    private void setMouseFn(MouseFn newMouseFn) {
        if (mouseFn != newMouseFn && !cursorIsHidden) {
            if (newMouseFn == MouseFn.TRACK) {
                if (topTrackableUnderMouse.getCursor() != null) {
                    view.getCanvas().setCursor(topTrackableUnderMouse.getCursor());
                } else {
                    view.getCanvas().setCursor(cursorDefaultTrack);
                }
            } else {
                view.getCanvas().setCursor(cursorDefault);
            }
        }
        mouseFn = newMouseFn;
    }

    public void clearTrackableObjects(TrackableLayer trackable) {
        TrackableStackEntry entry = trackableStack.lookupEntry(trackable);
        entry.clear();
    }

    public void addTrackableObject(TrackableLayer trackable, TrackableObject trackableObject) {
        trackableStack.addTrackableObject(trackable, trackableObject);
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
        if (topTrackableUnderMouse != null) {
            if (topTrackableUnderMouse.getCursor() != null) {
                view.getCanvas().setCursor(topTrackableUnderMouse.getCursor());
            } else {
                view.getCanvas().setCursor(cursorDefaultTrack);
            }
        } else {
            view.getCanvas().setCursor(cursorDefault);
        }
        cursorIsHidden = false;
    }

    public TrackableLayer getTopTrackableUnderMouse() {
        return topTrackableUnderMouse;
    }

    public TrackableObject getTopTrackableObjectUnderMouse() {
        return topTrackableObjectUnderMouse;
    }

}
