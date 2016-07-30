package com.denispetrov.graphics.drawable;

public interface Drawable {

    int getRank();

    void setRank(int rank);

    void draw();

    void modelUpdated();

    void contextUpdated();
}