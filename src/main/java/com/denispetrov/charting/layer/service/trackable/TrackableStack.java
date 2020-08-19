package com.denispetrov.charting.layer.service.trackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.denispetrov.charting.layer.TrackableLayer;
import com.denispetrov.charting.layer.TrackableObject;

public class TrackableStack {

    private List<TrackableStackEntry> stack = new ArrayList<>();
    private Map<TrackableLayer, TrackableStackEntry> lookup = new HashMap<>();

    public TrackableStackEntry addEntry(TrackableLayer layer) {
        TrackableStackEntry entry = new TrackableStackEntry(layer);
        stack.add(0, entry); // most recent at the top
        lookup.put(layer, entry);
        return entry;
    }

    public TrackableStackEntry lookupEntry(TrackableLayer layer) {
        return lookup.get(layer);
    }

    public void addTrackableObject(TrackableLayer trackable, TrackableObject trackableObject) {
        TrackableStackEntry entry = lookupEntry(trackable);
        entry.add(trackableObject);
    }

    public List<TrackableStackEntry> getEntries() {
        return stack;
    }
}
