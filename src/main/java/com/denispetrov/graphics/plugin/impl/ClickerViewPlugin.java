package com.denispetrov.graphics.plugin.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;

import com.denispetrov.graphics.plugin.*;
import com.denispetrov.graphics.view.View;
import com.denispetrov.graphics.view.ViewContext;

public class ClickerViewPlugin extends ViewPluginBase implements MouseListener {

    enum MouseFn {
        NONE, BUTTON_DOWN
    }

    private MouseFn mouseFn;
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
        System.out.println("mouseDown, button =" + e.button);
        if (trackerViewPlugin.isTracking(e.x, e.y) && mouseFn == MouseFn.NONE) {
            clickables.clear();
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
    }

    @Override
    public void mouseUp(MouseEvent e) {
        System.out.println("mouseUp, button =" + e.button);
        if (e.button == button && mouseFn == MouseFn.BUTTON_DOWN) {
            ViewContext viewContext = view.getViewContext();
            if (Math.abs(e.x - mouseCoordinatesOnMouseDown.x) < viewContext.getDragThreshold()
                    && Math.abs(e.y - mouseCoordinatesOnMouseDown.y) < viewContext.getDragThreshold()) {
                for (Clickable t : clickables.keySet()) {
                    t.objectClicked(clickables.get(t), e.button);
                }
            }
        }
        mouseFn = MouseFn.NONE;
        button = 0;
    }

}
