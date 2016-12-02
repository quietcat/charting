package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.impl.DrawableBase;

public class ViewportYAxisDrawable extends DrawableBase {

    @Override
    public void draw() {
        viewContext.drawLine(viewContext.getBaseX(), viewContext.getBaseY(), viewContext.getBaseX(), viewContext.getBaseY() + viewContext.getHeight());
    }
}
