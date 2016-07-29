package com.denispetrov.graphics;

public interface Drawable {

    int getRank();

    void setRank(int rank);

    void draw();

    void modelUpdated();

    void contextUpdated();
}