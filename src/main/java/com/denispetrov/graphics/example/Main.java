package com.denispetrov.graphics.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.denispetrov.graphics.drawable.impl.ViewportBackgroundDrawable;
import com.denispetrov.graphics.example.drawable.*;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.example.model.Label;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.plugin.impl.*;
import com.denispetrov.graphics.view.View;
import com.denispetrov.graphics.view.ViewContext;

public class Main {

    protected Shell shell;
    private Canvas canvas;

    private void run() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));
        createContents();
        shell.open();
        shell.layout();

        createView();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * Create contents of the window.
     */
    protected void createContents()
    {
        canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED|SWT.NO_BACKGROUND);
    }

    protected void createView() {
        View view = new View();
        view.setCanvas(canvas);

        view.addViewPlugin(new TrackerViewPlugin());
        view.addViewPlugin(new PanViewPlugin());
        ZoomViewPlugin zoomViewPlugin = new ZoomViewPlugin();
        zoomViewPlugin.setStickyX(true);
        view.addViewPlugin(zoomViewPlugin);
        view.addViewPlugin(new ClickerViewPlugin());
        view.addViewPlugin(new DraggerViewPlugin());

        view.addDrawable(new ViewportBackgroundDrawable());
        view.addDrawable(new ViewportXAxisDrawable());
        view.addDrawable(new ViewportYAxisDrawable());
        view.addDrawable(new ViewportZeroMarkDrawable());

        view.addDrawable(new ExampleModelRectDrawable());
        view.addDrawable(new ExampleModelDraggableRectDrawable());
        view.addDrawable(new ExampleModelLabelDrawable());

        view.init();

        ViewContext viewContext = new ViewContext();
        viewContext.setMargin(20);
        viewContext.setXAxisRange(ViewContext.AxisRange.NEGATIVE_ONLY);
        viewContext.setYAxisRange(ViewContext.AxisRange.FULL);
        view.setViewContext(viewContext);

        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(-100,100,100,100));
        model.getRectangles().add(new FRectangle(-300,300,100,100));
        model.getDraggableRectangles().add(new FRectangle(-300, 100, 100, 100));
        model.getDraggableRectangles().add(new FRectangle(-100, 300, 100, 100));
        model.getLabels().add(new Label("Label 1", -500.0, 100.0));
        model.getLabels().add(new Label("Label 2", -500.0, 200.0));
        view.setModel(model);
    }
}
