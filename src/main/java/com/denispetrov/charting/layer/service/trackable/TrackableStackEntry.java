package com.denispetrov.charting.layer.service.trackable;

import java.util.ArrayList;
import java.util.List;

import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;

public class TrackableStackEntry {
    private TrackableLayer trackable;
    private List<TrackableObject> trackableObjects = new ArrayList<>();
    public TrackableStackEntry(TrackableLayer trackable) {
        this.trackable = trackable;
    }
    public TrackableLayer getTrackable() {
        return trackable;
    }
    public void clear() {
        trackableObjects.clear();
    }
    public void add(TrackableObject trackableObject) {
        trackableObjects.add(0, trackableObject);
    }
    public List<TrackableObject> getTrackableObjects() {
        return trackableObjects;
    }
}