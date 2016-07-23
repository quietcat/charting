package com.denispetrov.graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

import com.denispetrov.graphics.GraphingContext.FRectangle;

public class GraphingController<T> implements MouseListener, MouseMoveListener, MouseTrackListener,
        MouseWheelListener, PaintListener, ControlListener, DisposeListener {

    enum MouseFn {
        MOVE, NONE, TRACK
    }

    public static class TrackingObject {
        public enum Reference {
            CANVAS, GRAPH
        }

        public FRectangle fRect;
        public Rectangle iRect;
        public Object target;
        public int xPadding, yPadding;
        public Reference xReference, yReference;
    }

    private Canvas canvas;

    private Cursor cursorDefault;

    private Cursor cursorTrack;

    private GraphingContext grc;
    private List<GraphingObjectHandler<T>> handlers = new ArrayList<>();
    T model;

    MouseFn mouseFn = MouseFn.NONE;

    Object mouseObject = null;

    private Set<TrackingObject> objectsUnderMouse = new HashSet<>();

    public void addHandler(GraphingObjectHandler<T> handler) {
        handlers.add(handler);
        handler.setGraphingContext(grc);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphingContext getGrc() {
        return grc;
    }

    public T getModel() {
        return this.model;
    }

    public void init() {
        cursorTrack = canvas.getDisplay().getSystemCursor(SWT.CURSOR_HAND);
        cursorDefault = canvas.getCursor();
    }

    public Set<TrackingObject> isTracking(int x, int y) {
        objectsUnderMouse.clear();
        for (GraphingObjectHandler<?> h : handlers) {
            for (TrackingObject t : h.getTrackingObjects()) {
                switch (t.xReference) {
                case GRAPH:
                    if (grc.x(x + t.xPadding) < t.fRect.x || grc.x(x - t.xPadding) >= t.fRect.x + t.fRect.w) {
                        continue;
                    }
                    break;
                case CANVAS:
                    if (x + t.xPadding < t.iRect.x || x - t.xPadding >= t.iRect.x + t.iRect.width) {
                        continue;
                    }
                    break;
                }
                switch (t.yReference) {
                case GRAPH:
                    if (grc.y(y + t.yPadding) < t.fRect.y || grc.y(y - t.yPadding) >= t.fRect.y + t.fRect.h) {
                        continue;
                    }
                    break;
                case CANVAS:
                    if (y + t.yPadding < t.fRect.y || y - t.yPadding >= t.fRect.y + t.fRect.h) {
                        continue;
                    }
                    break;
                }
                objectsUnderMouse.add(t);
            }
        }
        return objectsUnderMouse;
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
    public void mouseEnter(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExit(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseHover(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMove(MouseEvent e) {
        Set<TrackingObject> objectsUnderMouse = isTracking(e.x, e.y);
        if (objectsUnderMouse.size() > 0) {
            canvas.setCursor(cursorTrack);
        } else {
            canvas.setCursor(cursorDefault);
        }
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseUp(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void paint(GC gc) {
        grc.setGC(gc);
        for (GraphingObjectHandler<?> h : handlers) {
            h.draw();
        }
        //        dp.reset();
        //        drawAxes(gc);
        //        drawZero(gc);
        //        Color savedBackground = e.gc.getBackground();
        //        gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
        //        draw1(gc, 200, 100);
        //        e.gc.setBackground(savedBackground);
        //        dp.reset();
        //        dp.xMargin = 5;
        //        dp.yMargin = 5;
        //        draw1(gc, 200, 300);
        //        dp.reset();
        //        dp.isTransparent = true;
        //        dp.xAnchor = XAnchor.MIDDLE;
        //        dp.yAnchor = YAnchor.MIDDLE;
        //        grc.drawLine(200, 400, 300, 400);
        //        grc.drawText("Transparent", 250, 400, dp);
        //        grc.drawImage(image, 200, 200, null);
    }

    public void setBounds(Rectangle bounds) {
        grc.setCanvasWidth(bounds.width);
        grc.setCanvasHeight(bounds.height);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.addPaintListener(this);
        this.canvas.addControlListener(this);
        this.canvas.addDisposeListener(this);
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMoveListener(this);
        this.canvas.addMouseTrackListener(this);
        this.canvas.addMouseWheelListener(this);
    }

    public void setGrc(GraphingContext grc) {
        this.grc = grc;
    }

    public void setModel(T model) {
        this.model = model;
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controlMoved(ControlEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controlResized(ControlEvent e) {
        setBounds(canvas.getBounds());
    }

    @Override
    public void paintControl(PaintEvent e) {
        paint(e.gc);
    }
}
