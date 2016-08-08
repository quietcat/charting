package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.ViewController;

public interface Drawable {

    int getRank();

    void setRank(int rank);

    void draw();

    void modelUpdated();

    void contextUpdated();

    void setViewController(ViewController viewController);

    void setViewContext(ViewContext viewContext);
}