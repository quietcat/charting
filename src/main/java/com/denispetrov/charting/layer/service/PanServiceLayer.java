package com.denispetrov.charting.layer.service;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import com.denispetrov.charting.layer.MouseAwareLayer;
import com.denispetrov.charting.layer.MouseEventType;
import com.denispetrov.charting.layer.adapters.LayerAdapter;
import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.view.ViewContext;

public class PanServiceLayer extends LayerAdapter implements MouseAwareLayer {

    private enum MouseFn {
        NONE, MAYBE_PAN, PAN
    }

    private MouseFn mouseFn = MouseFn.NONE;

    private Point mouseOrigin = new Point(0, 0);
    private FPoint contextOrigin = new FPoint(0, 0);

    @Override
    public boolean mouseEvent(MouseEventType eventType, MouseEvent e) {
        ViewContext viewContext = view.getViewContext();
        switch (eventType) {
        case BUTTON_DOWN:
            if (e.button == 3) {
                mouseFn = MouseFn.MAYBE_PAN;
                mouseOrigin = new Point(e.x, e.y);
                contextOrigin = new FPoint(viewContext.getBaseX(), viewContext.getBaseY());
            }
            break;
        case BUTTON_UP:
            if (e.button == 3) {
                mouseFn = MouseFn.NONE;
            }
            break;
        case DOUBLE_CLICK:
            break;
        case MOVE:
            switch (mouseFn) {
            case MAYBE_PAN:
                if (Math.abs(e.x - mouseOrigin.x) >= viewContext.getDragThreshold()
                        || Math.abs(e.y - mouseOrigin.y) >= viewContext.getDragThreshold()) {
                    mouseFn = MouseFn.PAN;
                }
                break;
            case PAN:
                viewContext.setBaseX(contextOrigin.x + viewContext.w(mouseOrigin.x-e.x));
                viewContext.setBaseY(contextOrigin.y + viewContext.h(e.y-mouseOrigin.y));
                view.contextUpdated();
                break;
            default:
                break;
            }
            break;
        case SCROLL:
            break;
        default:;
        }
        return false;
    }
}
