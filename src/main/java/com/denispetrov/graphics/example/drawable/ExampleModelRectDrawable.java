package com.denispetrov.graphics.example.drawable;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.graphics.drawable.DrawParameters;
import com.denispetrov.graphics.drawable.impl.DrawableBase;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.example.plugin.SimpleTrackableObject;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.XAnchor;
import com.denispetrov.graphics.model.YAnchor;
import com.denispetrov.graphics.plugin.Clickable;
import com.denispetrov.graphics.plugin.Trackable;
import com.denispetrov.graphics.plugin.TrackableObject;
import com.denispetrov.graphics.plugin.impl.TrackerViewPlugin;
import com.denispetrov.graphics.view.View;

public class ExampleModelRectDrawable extends DrawableBase implements Trackable, Clickable {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleModelRectDrawable.class);

    private TrackerViewPlugin trackerViewPlugin;
    private DrawParameters dp = new DrawParameters();
    private Cursor cursor;

    @Override
    public void draw() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        for (FRectangle rect : model.getRectangles()) {
            viewContext.drawRectangle(rect.x, rect.y, rect.w, rect.h);
            viewContext.drawLine(rect.x, rect.y + rect.h / 2, rect.x + rect.w, rect.y + rect.h / 2);
            dp.xAnchor = XAnchor.LEFT;
            dp.yAnchor = YAnchor.BOTTOM;
            viewContext.drawText("Default", rect.x + rect.w, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            viewContext.drawText("YAnchor MIDDLE", rect.x + rect.w, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            viewContext.drawText("YAnchor TOP", rect.x + rect.w, rect.y, dp);
            dp.xAnchor = XAnchor.RIGHT;
            dp.yAnchor = YAnchor.BOTTOM;
            viewContext.drawText("YAnchor BOTTOM", rect.x, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            viewContext.drawText("YAnchor MIDDLE", rect.x, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            viewContext.drawText("YAnchor TOP", rect.x, rect.y, dp);
        }
    }

    @Override
    public void modelUpdated() {
        ExampleModel model = (ExampleModel) this.viewContext.getModel();
        trackerViewPlugin.clearTrackingObjects(this);
        for (FRectangle rect : model.getRectangles()) {
            SimpleTrackableObject trackingObject = new SimpleTrackableObject();
            trackingObject.setTarget(rect);
            trackingObject.setFRect(new FRectangle(rect));
            trackingObject.setXPadding(1);
            trackingObject.setYPadding(1);
            trackerViewPlugin.addTrackingObject(this,trackingObject);
        }
    }

    @Override
    public void objectClicked(Set<TrackableObject> objects, int button) {
        for (TrackableObject o : objects) {
            FRectangle rect = (FRectangle) o.getTarget();
            LOG.debug("Rectangle {} {} {} {} clicked", rect.x, rect.y, rect.w, rect.h);
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
        this.cursor = view.getCanvas().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

}
