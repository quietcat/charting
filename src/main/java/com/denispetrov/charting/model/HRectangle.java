package com.denispetrov.charting.model;

public class HRectangle {

    public double x, y;
    public int w, h;

    public HRectangle(double x, double y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public HRectangle(HRectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.w = rect.w;
        this.h = rect.h;
    }
}
