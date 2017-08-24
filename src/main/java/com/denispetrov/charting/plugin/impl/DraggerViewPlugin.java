package com.denispetrov.charting.plugin.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.*;

import com.denispetrov.charting.model.FPoint;
import com.denispetrov.charting.plugin.*;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class DraggerViewPlugin extends ViewPluginBase implements MouseMoveListener {

    private enum MouseFn {
        NONE, MAYBE_DRAGGING, DRAGGING
    }

    private MouseFn mouseFn = MouseFn.NONE;
    private Point mouseXY = new Point(0,0);
    private FPoint mouseOrigin = new FPoint(0, 0);
    private FPoint objectOrigin = new FPoint(0, 0);
    private FPoint trackableObjectOrigin = new FPoint(0, 0);
    private TrackableObject trackableObject;
    private Draggable draggable;

    private Cursor cursorHidden;
    private Cursor saveCursor;

    @Override
    public void setView(View view) {
        super.setView(view);
        view.getCanvas().addMouseMoveListener(this);

        // create a cursor with a transparent image
        Color colorWhite = view.getCanvas().getDisplay().getSystemColor(SWT.COLOR_WHITE);
        Color colorBlack = view.getCanvas().getDisplay().getSystemColor(SWT.COLOR_BLACK);
        PaletteData palette = new PaletteData(new RGB[] { colorWhite.getRGB(),
                colorBlack.getRGB() });
        ImageData sourceData = new ImageData(16, 16, 1, palette);
        sourceData.transparentPixel = 0;
        cursorHidden = new Cursor(view.getCanvas().getDisplay(), sourceData, 0, 0);

    }

    @Override
    public void mouseMove(MouseEvent e) {
        ViewContext viewContext;
        switch (mouseFn) {
        case MAYBE_DRAGGING:
            viewContext = view.getViewContext();
            if (Math.abs(e.x - mouseOrigin.x) >= viewContext.getDragThreshold()
                    || Math.abs(e.y - mouseOrigin.y) >= viewContext.getDragThreshold()) {
                mouseFn = MouseFn.DRAGGING;
                viewContext.getCanvas().setCursor(cursorHidden);
            }
            break;
        case DRAGGING:
            viewContext = view.getViewContext();
            FPoint origin = draggable.getOrigin(trackableObject.getTarget());
            origin.x = objectOrigin.x + (viewContext.x(e.x) - mouseOrigin.x);
            origin.y = objectOrigin.y + (viewContext.y(e.y) - mouseOrigin.y);
            draggable.setOrigin(trackableObject.getTarget(), origin);
            view.modelUpdated(draggable, trackableObject);
            break;
        case NONE:
            break;
        }
        mouseXY.x = e.x;
        mouseXY.y = e.y;
    }

    public void beginDrag(Draggable draggable, TrackableObject trackableObject) {
        this.draggable = draggable;
        this.trackableObject = trackableObject;
        ViewContext viewContext = view.getViewContext();
        mouseOrigin.x = viewContext.x(mouseXY.x);
        mouseOrigin.y = viewContext.y(mouseXY.y);
        objectOrigin = this.draggable.getOrigin(this.trackableObject.getTarget());
        trackableObjectOrigin.x = trackableObject.getFRect().x;
        trackableObjectOrigin.y = trackableObject.getFRect().y;
        saveCursor = viewContext.getCanvas().getCursor();
        mouseFn = MouseFn.MAYBE_DRAGGING;
    }

    public void endDrag() {
        FPoint origin = draggable.getOrigin(trackableObject.getTarget());
        trackableObject.getFRect().x = trackableObjectOrigin.x + (origin.x - objectOrigin.x);
        trackableObject.getFRect().y = trackableObjectOrigin.y + (origin.y - objectOrigin.y);
        trackableObject.setIRect(view.getViewContext().rectangle(trackableObject.getFRect()));
        view.getCanvas().setCursor(saveCursor);
        mouseFn = MouseFn.NONE;
        this.draggable = null;
        this.trackableObject = null;
    }

    @Override
    protected void finalize() throws Throwable {
        cursorHidden.dispose();
        super.finalize();
    }

}
