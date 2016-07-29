package com.denispetrov.graphics;


public interface ModelDrawable<T> extends Drawable {

    void setViewContext(ViewContext<T> vc);

}
