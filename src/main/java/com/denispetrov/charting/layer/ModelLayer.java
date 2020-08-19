package com.denispetrov.charting.layer;

public interface ModelLayer<M> extends Layer {

    void modelUpdated(M model);

}
