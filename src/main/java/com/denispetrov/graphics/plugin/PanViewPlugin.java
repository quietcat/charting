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
        NONE, PAN
    }

    private MouseFn mouseFn = MouseFn.NONE;

    Point mouseOrigin = new Point(0, 0);
    FPoint contextOrigin = new FPoint(0, 0);

    @Override
    public void mouseMove(MouseEvent e) {
        if (mouseFn == MouseFn.PAN) {
            ViewContext<?> viewContext = controller.getViewContext();
            viewContext.setBaseX(contextOrigin.x + viewContext.w(mouseOrigin.x-e.x));
            viewContext.setBaseY(contextOrigin.y + viewContext.h(e.y-mouseOrigin.y));
            controller.contextUpdated();
        }
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (e.button == 3) {
            ViewContext<?> viewContext = controller.getViewContext();
            mouseFn = MouseFn.PAN;
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

    @Override
    public void modelUpdated() {
    }

    @Override
    public void contextUpdated() {
    }

}
