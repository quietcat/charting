package com.denispetrov.graphics;

import java.util.HashSet;
import java.util.Set;


public abstract class ModelDrawerBase<T> implements ModelDrawable<T> {

    protected ViewContext<T> vc;
    protected int rank = 0;
    protected Set<TrackingObject> trackingObjects = new HashSet<>();

    @Override
    public void setViewContext(ViewContext<T> vc) {
        this.vc = vc;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public Set<TrackingObject> getTrackingObjects() {
        return trackingObjects;
    }

}
