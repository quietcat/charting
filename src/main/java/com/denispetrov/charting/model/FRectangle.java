package com.denispetrov.charting.model;

public class FRectangle {

    public double x, y;
    public double w, h;

    public FRectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public FRectangle(FRectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.w = rect.w;
        this.h = rect.h;
    }

    public static void copyRect(FRectangle src, FRectangle dst) {
        dst.x = src.x;
        dst.y = src.y;
        dst.w = src.w;
        dst.h = src.h;
    }

    public boolean contains(double fx, double fy) {
        return (fx >= x && fx < x + w && fy >= y && fy < y + h);
    }
}