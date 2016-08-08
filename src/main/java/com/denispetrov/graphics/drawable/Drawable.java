package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;
import com.denispetrov.graphics.View;

public interface Drawable {

    int getRank();

    void setRank(int rank);

    void draw();

    void modelUpdated();

    void contextUpdated();

    void setView(View view);

    void setViewContext(ViewContext viewContext);
}