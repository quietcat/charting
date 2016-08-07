package com.denispetrov.graphics.example.drawable;

import java.util.Set;

import com.denispetrov.graphics.ViewController;
import com.denispetrov.graphics.drawable.DrawParameters;
import com.denispetrov.graphics.drawable.ModelDrawableBase;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;
import com.denispetrov.graphics.model.XAnchor;
import com.denispetrov.graphics.model.YAnchor;
import com.denispetrov.graphics.plugin.TrackerViewPlugin;
import com.denispetrov.graphics.plugin.Trackable;
import com.denispetrov.graphics.plugin.SimpleTrackableObject;
import com.denispetrov.graphics.plugin.TrackableObject;

public class ExampleModelRectDrawable extends ModelDrawableBase<ExampleModel> implements Trackable {

    private TrackerViewPlugin trackerViewPlugin;
    private DrawParameters dp = new DrawParameters();

    @Override
    public void draw() {
        for (FRectangle rect : this.viewContext.getModel().getRectangles()) {
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
        trackerViewPlugin.clearTrackingObjects(this);
        for (FRectangle rect : this.viewContext.getModel().getRectangles()) {
            SimpleTrackableObject trackingObject = new SimpleTrackableObject();
            trackingObject.setTarget(rect);
            trackingObject.setFRect(new FRectangle(rect));
            trackingObject.setXPadding(1);
            trackingObject.setYPadding(1);
            trackingObject.setXReference(Reference.CHART);
            trackingObject.setYReference(Reference.CHART);
            trackerViewPlugin.addTrackingObject(this,trackingObject);
        }
    }

    @Override
    public void contextUpdated() {
    }

    @Override
    public void objectClicked(Set<TrackableObject> objects, int button) {
        for (TrackableObject o : objects) {
            System.out.println(o);
        }
    }

    @Override
    public void setViewController(ViewController<?> viewController) {
        super.setViewController(viewController);
        trackerViewPlugin = viewController.findPlugin(TrackerViewPlugin.class);
    }

}
