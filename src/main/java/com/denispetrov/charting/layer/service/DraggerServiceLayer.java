package com.denispetrov.charting.layer.service;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.layer.DraggableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.layer.adapters.LayerAdapter;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class DraggerServiceLayer extends LayerAdapter implements MouseMoveListener {
    private static final Logger LOG = LoggerFactory.getLogger(DraggerServiceLayer.class);

    private enum MouseFn {
        NONE, MAYBE_DRAGGING, DRAGGING
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private Point mouseXY = new Point(0,0);
    private FPoint mouseOrigin = new FPoint(0, 0);
    private FPoint objectOrigin = new FPoint(0, 0);
    private FPoint trackableObjectOrigin = new FPoint(0, 0);
    private TrackableObject trackableObject;
    private DraggableLayer draggable;
    private TrackerServiceLayer trackerServiceLayer;

    public DraggerServiceLayer(TrackerServiceLayer trackerServiceLayer) {
        this.trackerServiceLayer = trackerServiceLayer;
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseMoveListener(this);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        switch (mouseFn) {
        case MAYBE_DRAGGING:
            int dragThreshold = view.getViewContext().getDragThreshold();
            if (Math.abs(e.x - mouseOrigin.x) >= dragThreshold
                    || Math.abs(e.y - mouseOrigin.y) >= dragThreshold) {
                LOG.trace("Dragging beyond threshold of {}", dragThreshold);
                mouseFn = MouseFn.DRAGGING;
                trackerServiceLayer.hideCursor();
            }
            break;
        case DRAGGING: {
            FPoint origin = draggable.getOrigin(trackableObject);
            origin.x = objectOrigin.x + (view.getViewContext().x(e.x) - mouseOrigin.x);
            origin.y = objectOrigin.y + (view.getViewContext().y(e.y) - mouseOrigin.y);
            draggable.setOrigin(trackableObject, origin);
            break;
        }
        case NONE:
            break;
        }
        mouseXY.x = e.x;
        mouseXY.y = e.y;
    }

    public boolean beginDrag(DraggableLayer draggable, TrackableObject trackableObject) {
        if (this.trackableObject != null) {
            LOG.trace("Already dragging, ignore request");
            return false;
        }
        LOG.trace("Begin drag {} {}", draggable, trackableObject);
        this.draggable = draggable;
        this.trackableObject = trackableObject;
        ViewContext viewContext = view.getViewContext();
        mouseOrigin.x = viewContext.x(mouseXY.x);
        mouseOrigin.y = viewContext.y(mouseXY.y);
        objectOrigin = this.draggable.getOrigin(this.trackableObject);
        trackableObjectOrigin.x = trackableObject.getFRect().x;
        trackableObjectOrigin.y = trackableObject.getFRect().y;
        mouseFn = MouseFn.MAYBE_DRAGGING;
        return true;
    }

    public void endDrag() {
        if (mouseFn == MouseFn.NONE) {
            LOG.trace("Not dragging, ignore request");
            return;
        }
        LOG.trace("End drag {} {}", draggable, trackableObject);
        FPoint origin = draggable.getOrigin(trackableObject);
        trackableObject.getFRect().x = trackableObjectOrigin.x + (origin.x - objectOrigin.x);
        trackableObject.getFRect().y = trackableObjectOrigin.y + (origin.y - objectOrigin.y);
        trackerServiceLayer.showCursor();
        mouseFn = MouseFn.NONE;
        this.draggable = null;
        this.trackableObject = null;
    }

    public boolean isDragging() {
        return mouseFn == MouseFn.DRAGGING;
    }
}
