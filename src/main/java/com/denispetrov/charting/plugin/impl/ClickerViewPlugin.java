package com.denispetrov.charting.plugin.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.drawable.Drawable;
import com.denispetrov.charting.plugin.Clickable;
import com.denispetrov.charting.plugin.Trackable;
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

    private Map<Clickable,Set<TrackableObject>> clickables = new HashMap<>();
    private Point mouseCoordinatesOnMouseDown;

    private TrackerViewPlugin trackerViewPlugin;

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
        clickables.clear();
        if (trackerViewPlugin.isTracking(e.x, e.y) && mouseFn == MouseFn.NONE) {
            Map<Trackable,Set<TrackableObject>> objectsOnMouseDown = trackerViewPlugin.getObjectsUnderMouse(e.x, e.y);
            for (Trackable t : objectsOnMouseDown.keySet()) {
                if (Clickable.class.isAssignableFrom(t.getClass())) {
                    clickables.put((Clickable)t, objectsOnMouseDown.get(t));
                }
            }
            mouseCoordinatesOnMouseDown = new Point(e.x, e.y);
            mouseFn = MouseFn.BUTTON_DOWN;
            button = e.button;
        }
        for (Drawable drawable : view.getDrawables()) {
            if (Clickable.class.isAssignableFrom(drawable.getClass())) {
                ((Clickable)drawable).mouseDown(clickables, e.button, e.x, e.y);
            }
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        LOG.trace("Mouse up, button = {}", e.button);
        if (e.button == button && mouseFn == MouseFn.BUTTON_DOWN) {
            ViewContext viewContext = view.getViewContext();
            if (Math.abs(e.x - mouseCoordinatesOnMouseDown.x) < viewContext.getDragThreshold()
                    && Math.abs(e.y - mouseCoordinatesOnMouseDown.y) < viewContext.getDragThreshold()) {
                for (Clickable t : clickables.keySet()) {
                    t.objectClicked(clickables.get(t), e.button);
                }
            }
        }
        for (Drawable drawable : view.getDrawables()) {
            if (Clickable.class.isAssignableFrom(drawable.getClass())) {
                ((Clickable)drawable).mouseUp(e.button, e.x, e.y);
            }
        }
        mouseFn = MouseFn.NONE;
        button = 0;
    }

}
