package com.denispetrov.graphics.example.model;

import java.util.LinkedList;
import java.util.List;

import com.denispetrov.graphics.model.FRectangle;

public class ExampleModel {

    private List<FRectangle> rectangles = new LinkedList<>();
    private List<DraggableRectangle> draggableRectangles = new LinkedList<>();

    public List<FRectangle> getRectangles() {
        return rectangles;
    }

    public List<DraggableRectangle> getDraggableRectangles() {
        return draggableRectangles;
    }
}
