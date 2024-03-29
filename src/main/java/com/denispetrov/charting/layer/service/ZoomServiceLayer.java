package com.denispetrov.charting.layer.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.layer.MouseAwareLayer;
import com.denispetrov.charting.layer.MouseEventType;
import com.denispetrov.charting.layer.adapters.LayerAdapter;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class ZoomServiceLayer extends LayerAdapter implements MouseAwareLayer {
    private static final Logger LOG = LoggerFactory.getLogger(ZoomServiceLayer.class);

    private static enum MouseFn {
        NONE, X_SCALING, Y_SCALING, ZOOMING
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Cursor cursorDefault;

    private Cursor cursorTrackX, cursorTrackY;

    private boolean stickyX = false, stickyY = false;

    @Override
    public void setView(View view) {
        LOG.trace("Set view");
        super.setView(view);
        cursorTrackX = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE);
        cursorTrackY = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZENS);
        cursorDefault = view.getCanvas().getCursor();
    }

    @Override
    public boolean mouseEvent(MouseEventType eventType, MouseEvent e) {
        ViewContext viewContext = view.getViewContext();
        switch (eventType) {
        case SCROLL:
            if (mouseFn != MouseFn.NONE) {
                double x = viewContext.x(e.x);
                double y = viewContext.y(e.y);
                double m = 1.1;
                if (e.count < 0) {
                    m = 1 / m;
                }
                switch (mouseFn) {
                case X_SCALING:
                    stickyScaleX(viewContext, m, x);
                    view.contextUpdated();
                    return true;
                case Y_SCALING:
                    stickyScaleY(viewContext, m, y);
                    view.contextUpdated();
                    return true;
                case ZOOMING:
                    stickyScaleX(viewContext, m, x);
                    stickyScaleY(viewContext, m, y);
                    view.contextUpdated();
                    return true;
                default:;
                }
            }
            break;
        case MOVE:
            if (e.x >= viewContext.getMarginLeft() && e.x < view.getCanvasWidth() - viewContext.getMarginRight()
                    && e.y >= view.getCanvasHeight() - viewContext.getMarginBottom() && e.y < view.getCanvasHeight()) {
                if (mouseFn != MouseFn.X_SCALING) {
                    view.getCanvas().setCursor(cursorTrackX);
                    mouseFn = MouseFn.X_SCALING;
                }
            } else if (e.x >= 0 && e.x < viewContext.getMarginLeft() && e.y >= viewContext.getMarginTop()
                    && e.y < view.getCanvasHeight() - viewContext.getMarginBottom()) {
                if (mouseFn != MouseFn.Y_SCALING) {
                    view.getCanvas().setCursor(cursorTrackY);
                    mouseFn = MouseFn.Y_SCALING;
                }
            } else if (view.getMainAreaRectangle().contains(e.x, e.y)) {
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
            break;
        default:;
        }
        return false;
    }

    /**
     * Wrapper around {@link #scaleY(ViewContext, double, double)} that takes into account context Y axis range and stickyX flag.
     * When stickyX is set to true, horizontal scaling will use 0.0 as center of scaling 
     * when xAxisRange is either POSITIVE_ONLY or NEGATIVE_ONLY and the view zero is aligned 
     * correspondingly with the left or the right edge of the view at the start of scaling action.
     * @param viewContext View context
     * @param m Scale factor
     * @param x Center of scaling
     */
    private void stickyScaleX(ViewContext viewContext, double m, double x) {
        if (stickyX) {
            switch(viewContext.getXAxisRange()) {
            case FULL:
                scaleX(viewContext, m, x);
                break;
            case POSITIVE_ONLY:
                if (viewContext.getBaseX() == 0.0) {
                    scaleX(viewContext, m, 0.0);
                } else {
                    scaleX(viewContext, m, x);
                }
                break;
            case NEGATIVE_ONLY:
                double scaledMainAreaWidth = viewContext.w(view.getMainAreaRectangle().width);
                if (Math.abs(viewContext.getBaseX() + scaledMainAreaWidth) < Math.abs(viewContext.getBaseX()) / 1000000.0) {
                    scaleX(viewContext, m, 0.0);
                } else {
                    scaleX(viewContext, m, x);
                }
                break;
            }
        } else {
            scaleX(viewContext, m, x);
        }
    }

    /**
     * Wrapper around {@link #scaleY(ViewContext, double, double)} that takes into account context Y axis range and stickyY flag.
     * @param viewContext View context
     * @param m Scale factor
     * @param y Center of scaling
     */
    private void stickyScaleY(ViewContext viewContext, double m, double y) {
        if (stickyY) {
            switch (viewContext.getYAxisRange()) {
            case FULL:
                scaleY(viewContext, m, y);
                break;
            case POSITIVE_ONLY:
                if (viewContext.getBaseY() == 0.0) {
                    scaleY(viewContext, m, 0.0);
                } else {
                    scaleY(viewContext, m, y);
                }
                break;
            case NEGATIVE_ONLY:
                double scaledMainAreaHeight = viewContext.h(view.getMainAreaRectangle().height);
                if (Math.abs(viewContext.getBaseY() + scaledMainAreaHeight) < Math.abs(viewContext.getBaseY()) / 1000000.0) {
                    scaleY(viewContext, m, 0.0);
                } else {
                    scaleY(viewContext, m, y);
                }
                break;
            }
        } else {
            scaleY(viewContext, m, y);
        }
    }

    /**
     * Adjust x scale in the context. baseX will also be adjusted so that center of scaling does not move
     * relative to view
     * @param viewContext View context
     * @param m Scale factor
     * @param x Center of scaling
     */
    private void scaleX(ViewContext viewContext, double m, double x) {
        double newBaseX = (x * (m - 1) + viewContext.getBaseX()) / m;
        viewContext.setScaleX(viewContext.getScaleX() * m);
        viewContext.setBaseX(newBaseX);
    }

    /**
     * Adjust y scale in the context. baseY will also be adjusted so that center of scaling does not move
     * relative to view
     * @param viewContext View context
     * @param m Scale factor
     * @param y Center of scaling
     */
    private void scaleY(ViewContext viewContext, double m, double y) {
        double newBaseY = (y * (m - 1) + viewContext.getBaseY()) / m;
        viewContext.setScaleY(viewContext.getScaleY() * m);
        viewContext.setBaseY(newBaseY);
    }

    public boolean isStickyX() {
        return stickyX;
    }

    public ZoomServiceLayer setStickyX(boolean stickyX) {
        this.stickyX = stickyX;
        return this;
    }

    public boolean isStickyY() {
        return stickyY;
    }

    public ZoomServiceLayer setStickyY(boolean stickyY) {
        this.stickyY = stickyY;
        return this;
    }
}
