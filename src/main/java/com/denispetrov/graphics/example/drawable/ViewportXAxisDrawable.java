package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.ViewportDrawableBase;



public class ViewportXAxisDrawable extends ViewportDrawableBase {

    public static int X_AXIS_RANK = 1010000;
    public ViewportXAxisDrawable() {
        super();
        setRank(X_AXIS_RANK);
    }

    @Override
    public void draw() {
        vc.drawLine(vc.getBaseX(), vc.getBaseY(), vc.getBaseX() + vc.getWidth(), vc.getBaseY());
    }

    @Override
    public void modelUpdated() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void contextUpdated() {
        // TODO Auto-generated method stub
        
    }
}
