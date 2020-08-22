package com.denispetrov.charting.layer;

import com.denispetrov.charting.model.FRectangle;

/**
 * Provides a representation of model objects for {@link com.denispetrov.charting.layer.service.TrackerServiceLayer}
 */
public interface TrackableObject {

    FRectangle getFRect();

    Object getTarget();
}