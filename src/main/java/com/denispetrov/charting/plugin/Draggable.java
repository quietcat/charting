package com.denispetrov.charting.plugin;

import com.denispetrov.charting.model.FPoint;

/**
 * Classes implementing this interface are responsible for updating coordinates on objects that are being dragged.
 * The intention is that a class implementing {@link com.denispetrov.charting.plugin.DrawablePlugin} that deal with
 * drawing the instances of Object and inherently know the actual type of the object and how to update it.
 */
public interface Draggable {

    /**
     * Called by DraggerViewPlugin to provide new position of the object being dragged. Called on each mouse move event,
     * so implementations should minimize the amount of work performed in the implementing method.
     * @param object Object instance to update origin on
     * @param origin Updated origin
     */
    void setOrigin(Object object, FPoint origin);

    /**
     * Called by DraggerViewPlugin to obtain current position of the object being dragged. Called on each mouse move event,
     * so implementations should minimize the amount of work performed in the implementing method.
     * @param object Object instance to return origin information for
     * @return FPoint Object origin
     */
    FPoint getOrigin(Object object);
}
