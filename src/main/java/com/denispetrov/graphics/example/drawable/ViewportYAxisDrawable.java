package com.denispetrov.graphics.example.drawable;

import com.denispetrov.graphics.drawable.impl.DrawableBase;

public class ViewportYAxisDrawable extends DrawableBase {

    @Override
    public void draw() {
        switch (viewContext.getXAxisRange()) {
        case FULL:
            viewContext.drawLine(
                    viewContext.getBaseX(),
                    viewContext.getBaseY(),
                    viewContext.getBaseX(),
                    viewContext.getBaseY() + viewContext.getHeight());
            viewContext.drawLine(
                    viewContext.getBaseX() + viewContext.getWidth(),
                    viewContext.getBaseY(),
                    viewContext.getBaseX() + viewContext.getWidth(),
                    viewContext.getBaseY() + viewContext.getHeight());
            break;
        case POSITIVE_ONLY:
            viewContext.drawLine(
                    viewContext.getBaseX(),
                    viewContext.getBaseY(),
                    viewContext.getBaseX(),
                    viewContext.getBaseY() + viewContext.getHeight());
            break;
        case NEGATIVE_ONLY:
            viewContext.drawLine(
                    viewContext.getBaseX() + viewContext.getWidth(),
                    viewContext.getBaseY(),
                    viewContext.getBaseX() + viewContext.getWidth(),
                    viewContext.getBaseY() + viewContext.getHeight());
            break;
        }
    }
}
