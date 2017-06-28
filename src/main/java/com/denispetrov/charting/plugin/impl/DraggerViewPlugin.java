package com.denispetrov.charting.plugin.impl;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.*;

import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.plugin.*;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class DraggerViewPlugin extends ViewPluginBase implements MouseListener, MouseMoveListener {

    private enum MouseFn {
        NONE, MAYBE_DRAGGING, DRAGGING
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private FPoint mouseOrigin = new FPoint(0,0);
    private FPoint objectOrigin = new FPoint(0,0);
    private TrackableObject trackableObject;
    private Draggable draggable;

    private TrackerViewPlugin trackerViewPlugin;

    private Cursor cursorHidden;
    private Cursor saveCursor;

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseListener(this);
        view.getCanvas().addMouseMoveListener(this);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
        
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
        ViewContext viewContext;
        switch (mouseFn) {
        case MAYBE_DRAGGING:
            viewContext = view.getViewContext();
            if (Math.abs(e.x - mouseOrigin.x) >= viewContext.getDragThreshold()
                    || Math.abs(e.y - mouseOrigin.y) >= viewContext.getDragThreshold()) {
                mouseFn = MouseFn.DRAGGING;
                saveCursor = viewContext.getCanvas().getCursor();
                viewContext.getCanvas().setCursor(cursorHidden);
            }
            break;
        case DRAGGING:
            viewContext = view.getViewContext();
            FPoint origin = draggable.getOrigin(trackableObject.getTarget());
            origin.x = objectOrigin.x + (viewContext.x(e.x) - mouseOrigin.x);
            origin.y = objectOrigin.y + (viewContext.y(e.y) - mouseOrigin.y);
            draggable.setOrigin(trackableObject.getTarget(),origin);
            view.modelUpdated(draggable, trackableObject);
            break;
        case NONE:
            break;
        }
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (e.button == 1 && mouseFn == MouseFn.NONE) {
            Map<Trackable, Set<TrackableObject>> clickedOn = trackerViewPlugin.getObjectsUnderMouse(e.x, e.y);
            trackableObject = null;
            draggable = null;
            for (Trackable trackable : clickedOn.keySet()) {
                if (Draggable.class.isAssignableFrom(trackable.getClass())) {
                    for (TrackableObject object : clickedOn.get(trackable)) {
                        ViewContext viewContext = view.getViewContext();
                        mouseOrigin.x = viewContext.x(e.x);
                        mouseOrigin.y = viewContext.y(e.y);
                        draggable = (Draggable) trackable;
                        trackableObject = object;
                        objectOrigin = draggable.getOrigin(trackableObject.getTarget());
                        mouseFn = MouseFn.MAYBE_DRAGGING;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (e.button == 1 && mouseFn != MouseFn.NONE) {
            view.getCanvas().setCursor(saveCursor);
            mouseFn = MouseFn.NONE;
        }
    }

}
