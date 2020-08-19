package com.denispetrov.charting.view;

import java.util.ArrayList;
import java.util.List;

import com.denispetrov.charting.layer.ModelLayer;

public class ModelAwareView<M> extends View {

    private List<ModelLayer<M>> modelLayers = new ArrayList<>();

    public void addModelLayer(ModelLayer<M> layer) {
        super.addLayer(layer);
        modelLayers.add(layer);
    }

    /**
     * Trigger complete update of internal structures of all model layers
     */
    public void modelUpdated(M model) {
        for (ModelLayer<M> layer : modelLayers) {
            layer.modelUpdated(model);
        }
        super.getCanvas().redraw();
    }
}
