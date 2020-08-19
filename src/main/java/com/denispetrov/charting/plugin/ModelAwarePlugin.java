package com.denispetrov.charting.plugin;

public interface ModelAwarePlugin<M> extends Plugin {

    void modelUpdated(M model);

}
