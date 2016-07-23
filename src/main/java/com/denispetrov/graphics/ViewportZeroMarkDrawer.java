package com.denispetrov.graphics;


public class ViewportZeroMarkDrawer extends ViewportDrawerBase {

    public static int ZERO_MARK_RANK = 1100000;
    public ViewportZeroMarkDrawer() {
        super();
        setRank(ZERO_MARK_RANK);
    }

    @Override
    public void draw() {
        vc.drawLine(-10, -10, 10, 10);
        vc.drawLine(10, -10, -10, 10);
    }

}
