package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.ViewportDrawableBase;



public class ViewportZeroMarkDrawable extends ViewportDrawableBase {

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
