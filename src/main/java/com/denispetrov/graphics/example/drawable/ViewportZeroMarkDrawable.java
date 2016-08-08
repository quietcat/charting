package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.ViewportDrawableBase;



public class ViewportZeroMarkDrawable extends ViewportDrawableBase {

    @Override
    public void draw() {
        viewContext.drawLine(-10, -10, 10, 10);
        viewContext.drawLine(10, -10, -10, 10);
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
