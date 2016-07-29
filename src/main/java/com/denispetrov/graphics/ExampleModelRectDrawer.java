package com.denispetrov.graphics;

import java.util.Set;

public class ExampleModelRectDrawer extends ModelDrawerBase<ExampleModel> implements Trackable {

    ObjectTrackerViewPlugin objectTracker;
    private DrawParameters dp = new DrawParameters();

    @Override
    public void modelUpdated() {
        objectTracker.clearTrackingObjects(this);
        for (FRectangle rect : this.vc.getModel().getRectangles()) {
            TrackingObject trackingObject = new TrackingObject();
            trackingObject.target = rect;
            trackingObject.fRect = new FRectangle(rect);
            trackingObject.xPadding = 1;
            trackingObject.yPadding = 1;
            trackingObject.xReference = Reference.GRAPH;
            trackingObject.yReference = Reference.GRAPH;
            objectTracker.addTrackingObject(this,trackingObject);
        }
    }

    @Override
    public void draw() {
        for (FRectangle rect : this.vc.getModel().getRectangles()) {
            vc.drawRectangle(rect.x, rect.y, rect.w, rect.h);
            vc.drawLine(rect.x, rect.y + rect.h / 2, rect.x + rect.w, rect.y + rect.h / 2);
            dp.xAnchor = XAnchor.LEFT;
            dp.yAnchor = YAnchor.BOTTOM;
            vc.drawText("Default", rect.x + rect.w, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            vc.drawText("YAnchor MIDDLE", rect.x + rect.w, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            vc.drawText("YAnchor TOP", rect.x + rect.w, rect.y, dp);
            dp.xAnchor = XAnchor.RIGHT;
            dp.yAnchor = YAnchor.BOTTOM;
            vc.drawText("YAnchor BOTTOM", rect.x, rect.y + rect.h, dp);
            dp.yAnchor = YAnchor.MIDDLE;
            vc.drawText("YAnchor MIDDLE", rect.x, rect.y + rect.h / 2, dp);
            dp.yAnchor = YAnchor.TOP;
            vc.drawText("YAnchor TOP", rect.x, rect.y, dp);
        }
    }

    @Override
    public void setObjectTracker(ObjectTrackerViewPlugin objectTracker) {
        this.objectTracker = objectTracker;
    }

    @Override
    public void contextUpdated() {
    }

    @Override
    public void objectClicked(Set<TrackingObject> objects) {
        for (TrackingObject o : objects) {
            System.out.println(o);
        }
    }

}
