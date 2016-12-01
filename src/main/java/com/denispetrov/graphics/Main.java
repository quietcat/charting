package com.denispetrov.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.denispetrov.graphics.drawable.ViewportBackgroundDrawable;
import com.denispetrov.graphics.example.drawable.*;
import com.denispetrov.graphics.example.model.DraggableRectangle;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.example.plugin.DraggerViewPlugin;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.plugin.ClickerViewPlugin;
import com.denispetrov.graphics.plugin.PanViewPlugin;
import com.denispetrov.graphics.plugin.TrackerViewPlugin;
import com.denispetrov.graphics.plugin.ZoomViewPlugin;

public class Main {

    protected Shell shell;

    private void run() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));
        createContents();
        shell.open();
        shell.layout();

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
        Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED|SWT.NO_BACKGROUND);
        View view = new View();
        view.setCanvas(canvas);

        view.addViewPlugin(new TrackerViewPlugin());
        view.addViewPlugin(new PanViewPlugin());
        view.addViewPlugin(new ZoomViewPlugin());
        view.addViewPlugin(new ClickerViewPlugin());
        view.addViewPlugin(new DraggerViewPlugin());

        view.addDrawable(new ViewportBackgroundDrawable());
        view.addDrawable(new ViewportXAxisDrawable());
        view.addDrawable(new ViewportYAxisDrawable());
        view.addDrawable(new ViewportZeroMarkDrawable());

        view.addDrawable(new ExampleModelRectDrawable());
        view.addDrawable(new ExampleModelDraggableRectDrawable());

        view.init();

        ViewContext viewContext = new ViewContext();
        viewContext.setMargin(20);
        viewContext.setAllowNegativeBaseX(false);
        viewContext.setAllowNegativeBaseY(false);
        view.setViewContext(viewContext);

        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(100,100,100,100));
        model.getRectangles().add(new FRectangle(300,300,100,100));
        model.getDraggableRectangles().add(new DraggableRectangle(300, 100, 100, 100));
        model.getDraggableRectangles().add(new DraggableRectangle(100, 300, 100, 100));
        view.setModel(model);
    }
}
