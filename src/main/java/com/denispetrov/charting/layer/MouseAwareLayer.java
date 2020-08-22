package com.denispetrov.charting.layer;

import org.eclipse.swt.events.MouseEvent;

public interface MouseAwareLayer {
    boolean mouseEvent(MouseEventType eventType, MouseEvent e);
}
