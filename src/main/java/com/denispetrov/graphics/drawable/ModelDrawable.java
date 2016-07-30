package com.denispetrov.graphics.drawable;

import com.denispetrov.graphics.ViewContext;


public interface ModelDrawable<T> extends Drawable {

    void setViewContext(ViewContext<T> vc);

}
