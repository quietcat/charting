package com.denispetrov.graphics.plugin;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;
import com.denispetrov.graphics.model.FPoint;

public class PanViewPlugin extends ViewPluginBase implements MouseListener, MouseMoveListener {

    private enum MouseFn {
        NONE, MAYBE_PAN, PAN
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Point mouseOrigin = new Point(0, 0);
    private FPoint contextOrigin = new FPoint(0, 0);

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext<?> viewContext;
        switch (mouseFn) {
        case MAYBE_PAN:
            viewContext = controller.getViewContext();
            if (Math.abs(e.x - mouseOrigin.x) >= viewContext.getDragThreshold()
                    || Math.abs(e.y - mouseOrigin.y) >= viewContext.getDragThreshold()) {
                mouseFn = MouseFn.PAN;
            }
            break;
        case PAN:
            viewContext = controller.getViewContext();
            viewContext.setBaseX(contextOrigin.x + viewContext.w(mouseOrigin.x-e.x));
            viewContext.setBaseY(contextOrigin.y + viewContext.h(e.y-mouseOrigin.y));
            controller.contextUpdated();
            break;
        default:
            break;
        }
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (e.button == 3) {
            ViewContext<?> viewContext = controller.getViewContext();
            mouseFn = MouseFn.MAYBE_PAN;
            mouseOrigin = new Point(e.x, e.y);
            contextOrigin = new FPoint(viewContext.getBaseX(), viewContext.getBaseY());
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (e.button == 3) {
            mouseFn = MouseFn.NONE;
        }
    }

    @Override
    public void init(ViewController<?> controller) {
        super.init(controller);
        controller.getCanvas().addMouseMoveListener(this);
        controller.getCanvas().addMouseListener(this);
    }

}