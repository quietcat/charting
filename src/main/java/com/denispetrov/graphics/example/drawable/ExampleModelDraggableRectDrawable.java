package com.denispetrov.graphics.example.drawable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import com.denispetrov.graphics.View;
import com.denispetrov.graphics.drawable.DrawableBase;
import com.denispetrov.graphics.example.model.DraggableRectangle;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.model.FPoint;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;
import com.denispetrov.graphics.plugin.SimpleDraggableObject;
import com.denispetrov.graphics.plugin.Trackable;
import com.denispetrov.graphics.plugin.TrackerViewPlugin;

public class ExampleModelDraggableRectDrawable extends DrawableBase implements Trackable {

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
        for (DraggableRectangle rect : model.getDraggableRectangles()) {
            SimpleDraggableObject trackingObject = new SimpleDraggableObject();
            trackingObject.setTarget(rect);
            trackingObject.setFRect(new FRectangle(rect));
            trackingObject.setXPadding(1);
            trackingObject.setYPadding(1);
            trackingObject.setXReference(Reference.CHART);
            trackingObject.setYReference(Reference.CHART);
            trackingObject.setOrigin(new FPoint(trackingObject.getFRect().x,trackingObject.getFRect().y));
            trackerViewPlugin.addTrackingObject(this,trackingObject);
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL);
    }

    @Override
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

}
