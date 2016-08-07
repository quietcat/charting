package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.ViewportDrawableBase;



public class ViewportXAxisDrawable extends ViewportDrawableBase {

    @Override
    public void draw() {
        viewContext.drawLine(viewContext.getBaseX(), viewContext.getBaseY(), viewContext.getBaseX() + viewContext.getWidth(), viewContext.getBaseY());
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
