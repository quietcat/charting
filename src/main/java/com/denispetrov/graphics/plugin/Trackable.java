package com.denispetrov.graphics.plugin;

import org.eclipse.swt.graphics.Cursor;

public interface Trackable {

    void setCursor(Cursor cursor);

    Cursor getCursor();
}