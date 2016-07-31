package com.denispetrov.graphics.plugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Cursor;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;

public class ZoomViewPlugin extends ViewPluginBase implements MouseMoveListener, MouseListener, MouseWheelListener {

    private static enum MouseFn {
        NONE, X_SCALING, Y_SCALING, ZOOMING
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Cursor cursorDefault;

    private Cursor cursorTrackX, cursorTrackY;

    @Override
    public void init(ViewController<?> controller) {
        super.init(controller);
        controller.getCanvas().addMouseListener(this);
        controller.getCanvas().addMouseWheelListener(this);
        controller.getCanvas().addMouseMoveListener(this);
        cursorTrackX = controller.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE);
        cursorTrackY = controller.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZENS);
        cursorDefault = controller.getCanvas().getCursor();
    }

    @Override
    public void modelUpdated() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void contextUpdated() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDown(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseUp(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext<?> viewContext = controller.getViewContext();
        if (e.x >= viewContext.getMarginLeft() && e.x < viewContext.getCanvasWidth() - viewContext.getMarginRight() 
                && e.y >= viewContext.getCanvasHeight() - viewContext.getMarginBottom() && e.y < viewContext.getCanvasHeight()) {
            if (mouseFn != MouseFn.X_SCALING) {
                controller.getCanvas().setCursor(cursorTrackX);
                mouseFn = MouseFn.X_SCALING;
            }
        } else if (e.x >= 0 && e.x < viewContext.getMarginLeft() && e.y >= viewContext.getMarginTop() && e.y < viewContext.getCanvasHeight() - viewContext.getMarginBottom()) {
            if (mouseFn != MouseFn.Y_SCALING) {
                controller.getCanvas().setCursor(cursorTrackY);
                mouseFn = MouseFn.Y_SCALING;
            }
        } else if (viewContext.getMainAreaRectangle().contains(e.x, e.y)) {
            if (mouseFn != MouseFn.ZOOMING) {
                controller.getCanvas().setCursor(cursorDefault);
                mouseFn = MouseFn.ZOOMING;
            }
        } else {
            if (mouseFn != MouseFn.NONE) {
                controller.getCanvas().setCursor(cursorDefault);
                mouseFn = MouseFn.NONE;
            }
        }
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        if (mouseFn != MouseFn.NONE) {
            ViewContext<?> viewContext = controller.getViewContext();
            double x = viewContext.x(e.x);
            double y = viewContext.y(e.y);
            double m = 1.1;
            if (e.count < 0) {
                m = 1 / m;
            }
            switch (mouseFn) {
            case X_SCALING:
                scaleX(viewContext, m, x);
                controller.contextUpdated();
                break;
            case Y_SCALING:
                scaleY(viewContext, m, y);
                controller.contextUpdated();
                break;
            case ZOOMING:
                scaleX(viewContext, m, x);
                scaleY(viewContext, m, y);
                controller.contextUpdated();
                break;
            default:
                break;
            }
        }
    }

    private void scaleX(ViewContext<?> viewContext, double m, double x) {
        double newBaseX = (x * (m - 1) + viewContext.getBaseX()) / m;
        viewContext.setScaleX(viewContext.getScaleX() * m);
        viewContext.setBaseX(newBaseX);
    }

    private void scaleY(ViewContext<?> viewContext, double m, double y) {
        double newBaseY = (y * (m - 1) + viewContext.getBaseY()) / m;
        viewContext.setScaleY(viewContext.getScaleY() * m);
        viewContext.setBaseY(newBaseY);
    }
}
