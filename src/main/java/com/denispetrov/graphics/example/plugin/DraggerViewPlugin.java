package com.denispetrov.graphics.example.plugin;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;
import com.denispetrov.graphics.model.FPoint;
import com.denispetrov.graphics.plugin.DraggableObject;
import com.denispetrov.graphics.plugin.Trackable;
import com.denispetrov.graphics.plugin.TrackableObject;
import com.denispetrov.graphics.plugin.TrackerViewPlugin;
import com.denispetrov.graphics.plugin.ViewPluginBase;

public class DraggerViewPlugin extends ViewPluginBase implements MouseListener, MouseMoveListener {

    private enum MouseFn {
        NONE, MAYBE_DRAGGING, DRAGGING
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private FPoint mouseOrigin = new FPoint(0,0);
    private FPoint objectOrigin = new FPoint(0,0);
    private DraggableObject objectBeingDragged;

    private TrackerViewPlugin trackerViewPlugin;

    public TrackerViewPlugin getTrackerViewPlugin() {
        return trackerViewPlugin;
    }

    public void setTrackerViewPlugin(TrackerViewPlugin trackerViewPlugin) {
        this.trackerViewPlugin = trackerViewPlugin;
    }

    @Override
    public void init(ViewController<?> controller) {
        super.init(controller);
        controller.getCanvas().addMouseListener(this);
        controller.getCanvas().addMouseMoveListener(this);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext<?> viewContext;
        switch (mouseFn) {
        case MAYBE_DRAGGING:
            viewContext = controller.getViewContext();
            if (Math.abs(e.x - mouseOrigin.x) >= viewContext.getDragThreshold()
                    || Math.abs(e.y - mouseOrigin.y) >= viewContext.getDragThreshold()) {
                mouseFn = MouseFn.DRAGGING;
            }
            break;
        case DRAGGING:
            if (objectBeingDragged != null) {
                viewContext = controller.getViewContext();
                FPoint origin = objectBeingDragged.getOrigin();
                origin.x = objectOrigin.x + (viewContext.x(e.x) - mouseOrigin.x);
                origin.y = objectOrigin.y + (viewContext.y(e.y) - mouseOrigin.y);
                objectBeingDragged.setOrigin(origin);
            }
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
            objectBeingDragged = null;
            for (Set<TrackableObject> objects : clickedOn.values()) {
                for (TrackableObject object : objects) {
                    if (DraggableObject.class.isAssignableFrom(object.getClass())) {
                        ViewContext<?> viewContext = controller.getViewContext();
                        objectBeingDragged = (DraggableObject)object;
                        mouseOrigin.x = viewContext.x(e.x);
                        mouseOrigin.y = viewContext.y(e.y);
                        objectOrigin.x = objectBeingDragged.getOrigin().x;
                        objectOrigin.y = objectBeingDragged.getOrigin().y;
                        mouseFn = MouseFn.MAYBE_DRAGGING;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (e.button == 1) {
            mouseFn = MouseFn.NONE;
        }
    }

}
