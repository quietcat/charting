package com.denispetrov.graphics;

public class FPoint {
    public double x, y;

    public FPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "FPoint(" + x + ", " + y + ")";
    }
}