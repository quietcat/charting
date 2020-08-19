package com.denispetrov.charting.view;

import java.util.ArrayList;
import java.util.List;

import com.denispetrov.charting.plugin.ModelAwarePlugin;

public class ModelAwareView<M> extends View {

    private List<ModelAwarePlugin<M>> modelAwarePlugins = new ArrayList<>();

    public void addModelAwarePlugin(ModelAwarePlugin<M> plugin) {
        super.addPlugin(plugin);
        modelAwarePlugins.add(plugin);
    }

    /**
     * Trigger complete update of internal structures of all plugins and drawables
     */
    public void modelUpdated(M model) {
        for (ModelAwarePlugin<M> viewPlugin : modelAwarePlugins) {
            viewPlugin.modelUpdated(model);
        }
        super.getCanvas().redraw();
    }
}
