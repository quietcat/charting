package com.denispetrov.graphics.example.plugin;

import org.eclipse.swt.graphics.Rectangle;

import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.model.Reference;
import com.denispetrov.graphics.plugin.TrackableObject;

public class SimpleTrackableObject implements TrackableObject {

    private FRectangle fRect;
    private Rectangle iRect;
    private Object target;
    private int xPadding, yPadding;
    private Reference xReference, yReference;

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getfRect()
     */
    @Override
    public FRectangle getFRect() {
        return fRect;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setfRect(com.denispetrov.graphics.model.FRectangle)
     */
    @Override
    public void setFRect(FRectangle fRect) {
        this.fRect = fRect;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getiRect()
     */
    @Override
    public Rectangle getIRect() {
        return iRect;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setiRect(org.eclipse.swt.graphics.Rectangle)
     */
    @Override
    public void setIRect(Rectangle iRect) {
        this.iRect = iRect;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getTarget()
     */
    @Override
    public Object getTarget() {
        return target;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setTarget(java.lang.Object)
     */
    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getxPadding()
     */
    @Override
    public int getxPadding() {
        return xPadding;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setxPadding(int)
     */
    @Override
    public void setXPadding(int xPadding) {
        this.xPadding = xPadding;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getyPadding()
     */
    @Override
    public int getyPadding() {
        return yPadding;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setyPadding(int)
     */
    @Override
    public void setYPadding(int yPadding) {
        this.yPadding = yPadding;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getxReference()
     */
    @Override
    public Reference getXReference() {
        return xReference;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setxReference(com.denispetrov.graphics.model.Reference)
     */
    @Override
    public void setXReference(Reference xReference) {
        this.xReference = xReference;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#getyReference()
     */
    @Override
    public Reference getYReference() {
        return yReference;
    }

    /* (non-Javadoc)
     * @see com.denispetrov.graphics.plugin.TrackingObject#setyReference(com.denispetrov.graphics.model.Reference)
     */
    @Override
    public void setYReference(Reference yReference) {
        this.yReference = yReference;
    }

}
