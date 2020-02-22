package com.denispetrov.charting.plugin.impl;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class ClickerViewPlugin extends ViewPluginBase implements MouseListener {
    private static final Logger LOG = LoggerFactory.getLogger(ClickerViewPlugin.class);

    enum MouseFn {
        NONE, BUTTON_DOWN
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private int button = 0;

    private Point mouseCoordinatesOnMouseDown;

    private TrackerViewPlugin trackerViewPlugin;
    
    private Clickable clickableMouseDown;
    private TrackableObject trackableObject;

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseListener(this);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        LOG.trace("Mouse down, button = {}", e.button);
        if (trackerViewPlugin.isTracking(e.x, e.y) && mouseFn == MouseFn.NONE) {
        	TrackableObject to = trackerViewPlugin.getTopTrackableObjectUnderMouse();
            if (Clickable.class.isAssignableFrom(to.getTrackable().getClass())) {
            	trackableObject = to;
                mouseCoordinatesOnMouseDown = new Point(e.x, e.y);
                mouseFn = MouseFn.BUTTON_DOWN;
                button = e.button;
            	clickableMouseDown = (Clickable)to.getTrackable();
                clickableMouseDown.mouseDown(to, e.button, e.x, e.y);
            }
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        LOG.trace("Mouse up, button = {}", e.button);
        if (e.button == button && mouseFn == MouseFn.BUTTON_DOWN) {
        	clickableMouseDown.mouseUp(button, e.x, e.y);
            ViewContext viewContext = view.getViewContext();
            if (Math.abs(e.x - mouseCoordinatesOnMouseDown.x) < viewContext.getDragThreshold()
                    && Math.abs(e.y - mouseCoordinatesOnMouseDown.y) < viewContext.getDragThreshold()) {
                clickableMouseDown.objectClicked(trackableObject, button);
            }
        }
        mouseFn = MouseFn.NONE;
        button = 0;
    }

}
