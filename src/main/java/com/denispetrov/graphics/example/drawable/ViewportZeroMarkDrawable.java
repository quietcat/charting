package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.ViewportDrawableBase;



public class ViewportZeroMarkDrawable extends ViewportDrawableBase {

    public static int ZERO_MARK_RANK = 1100000;
    public ViewportZeroMarkDrawable() {
        super();
        setRank(ZERO_MARK_RANK);
    }

    @Override
    public void draw() {
        vc.drawLine(-10, -10, 10, 10);
        vc.drawLine(10, -10, -10, 10);
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
