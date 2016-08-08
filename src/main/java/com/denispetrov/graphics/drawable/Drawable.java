package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewController;

public interface Drawable {

    int getRank();

    void setRank(int rank);

    void draw();

    void modelUpdated();

    void contextUpdated();

    void setViewController(ViewController<?> viewController);
}