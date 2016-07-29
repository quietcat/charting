package com.denispetrov.graphics;


public class ViewportYAxisDrawer extends ViewportDrawerBase {

    public static int Y_AXIS_RANK = 1000000;
    public ViewportYAxisDrawer() {
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
