package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;

public interface ViewportDrawable extends Drawable {

    void setViewContext(ViewContext<?> viewContext);

}
