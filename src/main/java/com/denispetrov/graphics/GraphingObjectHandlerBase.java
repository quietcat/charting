package com.denispetrov.graphics;


public abstract class GraphingObjectHandlerBase<T> implements GraphingObjectHandler<T> {

    protected GraphingContext grc;

    @Override
    public void setGraphingContext(GraphingContext grc) {
        this.grc = grc;
    }
}
