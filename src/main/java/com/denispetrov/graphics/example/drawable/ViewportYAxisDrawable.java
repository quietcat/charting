package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.ViewportDrawableBase;



public class ViewportYAxisDrawable extends ViewportDrawableBase {

    public static int Y_AXIS_RANK = 1000000;
    public ViewportYAxisDrawable() {
        super();
        setRank(Y_AXIS_RANK);
    }

    @Override
    public void draw() {
        vc.drawLine(vc.getBaseX(), vc.getBaseY(), vc.getBaseX(), vc.getBaseY() + vc.getHeight());
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
