package com.denispetrov.graphics;

import java.util.ArrayList;
import java.util.Comparator;
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

public class ViewController<T> implements MouseListener, MouseMoveListener, MouseTrackListener,
        MouseWheelListener, PaintListener, ControlListener, DisposeListener {

    enum MouseFn {
        MOVE, NONE, TRACK
    }

    private Canvas canvas;

    private Cursor cursorDefault;

    private Cursor cursorTrack;

    private ViewContext<T> vc;
    private List<ModelDrawable<?>> modelDrawers = new ArrayList<>();
    private List<ViewportDrawer> viewportDrawers = new ArrayList<>();
    private List<Drawable> rankedDrawables = new ArrayList<>();
    MouseFn mouseFn = MouseFn.NONE;

    Object mouseObject = null;

    private Set<TrackingObject> objectsUnderMouse = new HashSet<>();

    public void addModelDrawer(ModelDrawable<T> handler) {
        modelDrawers.add(handler);
        handler.setViewContext(vc);
    }

    public void addViewportDrawer(ViewportDrawer handler) {
        viewportDrawers.add(handler);
        handler.setViewContext(vc);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public ViewContext<T> getGrc() {
        return vc;
    }

    public void init() {
        cursorTrack = canvas.getDisplay().getSystemCursor(SWT.CURSOR_HAND);
        cursorDefault = canvas.getCursor();
        rankedDrawables.addAll(modelDrawers);
        rankedDrawables.addAll(viewportDrawers);
        rankedDrawables.sort(new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Integer.compare(o1.getRank(), o2.getRank());
            }
        });
    }

    public Set<TrackingObject> isTracking(int x, int y) {
        objectsUnderMouse.clear();
        for (ModelDrawable<?> h : modelDrawers) {
            for (TrackingObject t : h.getTrackingObjects()) {
                switch (t.xReference) {
                case GRAPH:
                    if (vc.x(x + t.xPadding) < t.fRect.x || vc.x(x - t.xPadding) >= t.fRect.x + t.fRect.w) {
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
                    if (vc.y(y + t.yPadding) < t.fRect.y || vc.y(y - t.yPadding) >= t.fRect.y + t.fRect.h) {
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
        vc.setGC(gc);
        for (Drawable h : rankedDrawables) {
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
        vc.setCanvasWidth(bounds.width);
        vc.setCanvasHeight(bounds.height);
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

    public void setViewContext(ViewContext<T> grc) {
        this.vc = grc;
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
