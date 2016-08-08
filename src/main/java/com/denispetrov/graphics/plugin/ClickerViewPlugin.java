package com.denispetrov.graphics.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.View;

public class ClickerViewPlugin extends PluginBase implements MouseListener {

    enum MouseFn {
        NONE, BUTTON_DOWN
    }

    private MouseFn mouseFn;
    private int button = 0;

    private Map<Trackable,Set<TrackableObject>> objectsOnMouseDown = new HashMap<>();
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
        if (trackerViewPlugin.isTracking(e.x, e.y)) {
            objectsOnMouseDown = trackerViewPlugin.getObjectsUnderMouse(e.x, e.y);
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
                for (Trackable t : objectsOnMouseDown.keySet()) {
                    t.objectClicked(objectsOnMouseDown.get(t), e.button);
                }
            }
        }
        mouseFn = MouseFn.NONE;
        button = 0;
    }

}
