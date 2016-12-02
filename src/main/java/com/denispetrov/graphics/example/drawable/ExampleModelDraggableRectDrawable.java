package com.denispetrov.graphics.example.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import com.denispetrov.graphics.drawable.DrawableBase;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.example.plugin.SimpleTrackableObject;
import com.denispetrov.graphics.model.FPoint;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;
import com.denispetrov.graphics.plugin.Draggable;
import com.denispetrov.graphics.plugin.Trackable;
import com.denispetrov.graphics.plugin.TrackableObject;
import com.denispetrov.graphics.plugin.TrackerViewPlugin;
import com.denispetrov.graphics.view.View;

public class ExampleModelDraggableRectDrawable extends DrawableBase implements Trackable, Draggable {

    private TrackerViewPlugin trackerViewPlugin;
    private Cursor cursor;

    @Override
    public void draw() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        for (FRectangle rect : model.getDraggableRectangles()) {
            viewContext.drawRectangle(rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void modelUpdated() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        trackerViewPlugin.clearTrackingObjects(this);
        for (FRectangle rect : model.getDraggableRectangles()) {
            TrackableObject draggableObject = new SimpleTrackableObject();
            draggableObject.setTarget(rect);
            draggableObject.setFRect(new FRectangle(rect));
            draggableObject.setXPadding(1);
            draggableObject.setYPadding(1);
            draggableObject.setXReference(Reference.CHART);
            draggableObject.setYReference(Reference.CHART);
            trackerViewPlugin.addTrackingObject(this,draggableObject);
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void setOrigin(Object object, FPoint origin) {
        FRectangle target = (FRectangle) object;
        target.x = origin.x;
        target.y = origin.y;
    }

    @Override
    public FPoint getOrigin(Object object) {
        FRectangle target = (FRectangle) object;
        return new FPoint(target.x, target.y);
    }

}
