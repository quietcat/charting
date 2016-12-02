package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.impl.DrawableBase;

public class ViewportXAxisDrawable extends DrawableBase {

    @Override
    public void draw() {
        viewContext.drawLine(viewContext.getBaseX(), viewContext.getBaseY(), viewContext.getBaseX() + viewContext.getWidth(), viewContext.getBaseY());
    }
}
