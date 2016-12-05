package com.denispetrov.graphics.plugin.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Cursor;

import com.denispetrov.graphics.view.View;
import com.denispetrov.graphics.view.ViewContext;

public class ZoomViewPlugin extends ViewPluginBase implements MouseMoveListener, MouseListener, MouseWheelListener {

    private static enum MouseFn {
        NONE, X_SCALING, Y_SCALING, ZOOMING
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Cursor cursorDefault;

    private Cursor cursorTrackX, cursorTrackY;

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseListener(this);
        view.getCanvas().addMouseWheelListener(this);
        view.getCanvas().addMouseMoveListener(this);
        cursorTrackX = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE);
        cursorTrackY = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZENS);
        cursorDefault = view.getCanvas().getCursor();
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
        ViewContext viewContext = view.getViewContext();
        if (e.x >= viewContext.getMarginLeft() && e.x < viewContext.getCanvasWidth() - viewContext.getMarginRight() 
                && e.y >= viewContext.getCanvasHeight() - viewContext.getMarginBottom() && e.y < viewContext.getCanvasHeight()) {
            if (mouseFn != MouseFn.X_SCALING) {
                view.getCanvas().setCursor(cursorTrackX);
                mouseFn = MouseFn.X_SCALING;
            }
        } else if (e.x >= 0 && e.x < viewContext.getMarginLeft() && e.y >= viewContext.getMarginTop() && e.y < viewContext.getCanvasHeight() - viewContext.getMarginBottom()) {
            if (mouseFn != MouseFn.Y_SCALING) {
                view.getCanvas().setCursor(cursorTrackY);
                mouseFn = MouseFn.Y_SCALING;
            }
        } else if (viewContext.getMainAreaRectangle().contains(e.x, e.y)) {
            if (mouseFn != MouseFn.ZOOMING) {
                view.getCanvas().setCursor(cursorDefault);
                mouseFn = MouseFn.ZOOMING;
            }
        } else {
            if (mouseFn != MouseFn.NONE) {
                view.getCanvas().setCursor(cursorDefault);
                mouseFn = MouseFn.NONE;
            }
        }
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        if (mouseFn != MouseFn.NONE) {
            ViewContext viewContext = view.getViewContext();
            double x = viewContext.x(e.x);
            double y = viewContext.y(e.y);
            double m = 1.1;
            if (e.count < 0) {
                m = 1 / m;
            }
            switch (mouseFn) {
            case X_SCALING:
                if (!viewContext.isAllowNegativeBaseX() && viewContext.getBaseX() == 0.0) {
                    scaleX(viewContext, m, 0.0);
                } else {
                    scaleX(viewContext, m, x);
                }
                view.contextUpdated();
                break;
            case Y_SCALING:
                if (!viewContext.isAllowNegativeBaseY() && viewContext.getBaseY() == 0.0) {
                    scaleY(viewContext, m, 0.0);
                } else {
                    scaleY(viewContext, m, y);
                }
                view.contextUpdated();
                break;
            case ZOOMING:
                if (!viewContext.isAllowNegativeBaseX() && viewContext.getBaseX() == 0.0) {
                    scaleX(viewContext, m, 0.0);
                } else {
                    scaleX(viewContext, m, x);
                }
                if (!viewContext.isAllowNegativeBaseY() && viewContext.getBaseY() == 0.0) {
                    scaleY(viewContext, m, 0.0);
                } else {
                    scaleY(viewContext, m, y);
                }
                view.contextUpdated();
                break;
            default:
                break;
            }
        }
    }

    private void scaleX(ViewContext viewContext, double m, double x) {
        double newBaseX = (x * (m - 1) + viewContext.getBaseX()) / m;
        viewContext.setScaleX(viewContext.getScaleX() * m);
        viewContext.setBaseX(newBaseX);
    }

    private void scaleY(ViewContext viewContext, double m, double y) {
        double newBaseY = (y * (m - 1) + viewContext.getBaseY()) / m;
        viewContext.setScaleY(viewContext.getScaleY() * m);
        viewContext.setBaseY(newBaseY);
    }
}