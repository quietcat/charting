package com.denispetrov.charting.layer.service;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.layer.ClickableLayer;
import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;
import com.denispetrov.charting.view.View;

public class ClickerServiceLayer extends LayerAdapter implements MouseListener {
    private static final Logger LOG = LoggerFactory.getLogger(ClickerServiceLayer.class);

    enum MouseFn {
        NONE, BUTTON_DOWN
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private int button = 0;

    private Point mouseCoordinatesOnMouseDown;

    private TrackerServiceLayer trackerServiceLayer;

    private ClickableLayer clickable;
    private TrackableObject trackableObjectClicked;

    public ClickerServiceLayer(TrackerServiceLayer trackerServiceLayer) {
        this.trackerServiceLayer = trackerServiceLayer;
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseListener(this);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        LOG.trace("Mouse down, button = {}", e.button);
        TrackableLayer trackable = trackerServiceLayer.getTopTrackableUnderMouse();
        if (trackable != null && ClickableLayer.class.isAssignableFrom(trackable.getClass())) {
            clickable = (ClickableLayer)trackable;
            mouseCoordinatesOnMouseDown = new Point(e.x, e.y);
            mouseFn = MouseFn.BUTTON_DOWN;
            button = e.button;
            trackableObjectClicked = trackerServiceLayer.getTopTrackableObjectUnderMouse();
            clickable.mouseDown(trackableObjectClicked, e.button, e.x, e.y);
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        LOG.trace("Mouse up, button = {}", e.button);
        if (e.button == button && mouseFn == MouseFn.BUTTON_DOWN) {
            int dragThreshold = view.getViewContext().getDragThreshold();
            if (Math.abs(e.x - mouseCoordinatesOnMouseDown.x) < dragThreshold
                    && Math.abs(e.y - mouseCoordinatesOnMouseDown.y) < dragThreshold) {
                clickable.objectClicked(trackableObjectClicked, e.button);
            }
            clickable.mouseUp(button, e.x, e.y);
            clickable = null;
        }
        mouseFn = MouseFn.NONE;
        button = 0;
    }

}
