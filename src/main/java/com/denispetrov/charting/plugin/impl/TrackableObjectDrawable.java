package com.denispetrov.charting.plugin.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.denispetrov.charting.drawable.impl.DrawableBase;
import com.denispetrov.charting.plugin.TrackableObject;
import com.denispetrov.charting.view.View;
import com.denispetrov.charting.view.ViewContext;

public class TrackableObjectDrawable extends DrawableBase {

	private TrackerViewPlugin trackerViewPlugin;
	private Color rectColor;
	
	public TrackableObjectDrawable() {
		super();
	}
	
	@Override
	public void setView(View view) {
		super.setView(view);
		this.trackerViewPlugin = view.findPlugin(TrackerViewPlugin.class);
	}
	
	@Override
	public void setViewContext(ViewContext viewContext) {
		super.setViewContext(viewContext);
		rectColor = viewContext.getCanvas().getDisplay().getSystemColor(SWT.COLOR_RED);
	}
	
	@Override
	public void draw() {
		GC gc = viewContext.getGC();
		Color saveColor = gc.getBackground();
		gc.setBackground(rectColor);
		for (TrackableObject to : trackerViewPlugin.getObjects()) {
			viewContext.fillRectangle(to.getFRect());
		}
		gc.setBackground(saveColor);
	}

}
