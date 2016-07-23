package com.denispetrov.graphics;


public class ExampleModelRectDrawer extends ModelDrawerBase<ExampleModel> {

    private DrawParameters dp = new DrawParameters();

    private static final int W = 100;
    private static final int H = 100;

    private int x = 100;
    private int y = 100;

    @Override
    public void draw() {
        vc.drawRectangle(x, y, W, H);
        vc.drawLine(x, y + H / 2, x + W, y + H / 2);
        vc.drawText("Default", x + W, y + H, dp);
        dp.yAnchor = YAnchor.MIDDLE;
        vc.drawText("YAnchor MIDDLE", x + W, y + H / 2, dp);
        dp.yAnchor = YAnchor.TOP;
        vc.drawText("YAnchor TOP", x + W, y, dp);
        dp.xAnchor = XAnchor.RIGHT;
        dp.yAnchor = YAnchor.BOTTOM;
        vc.drawText("YAnchor BOTTOM", x, y + H, dp);
        dp.yAnchor = YAnchor.MIDDLE;
        vc.drawText("YAnchor MIDDLE", x, y + H / 2, dp);
        dp.yAnchor = YAnchor.TOP;
        vc.drawText("YAnchor TOP", x, y, dp);
    }

}
