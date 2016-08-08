package com.denispetrov.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.denispetrov.graphics.drawable.ViewportBackgroundDrawable;
import com.denispetrov.graphics.example.drawable.ExampleModelRectDrawable;
import com.denispetrov.graphics.example.drawable.ViewportXAxisDrawable;
import com.denispetrov.graphics.example.drawable.ViewportYAxisDrawable;
import com.denispetrov.graphics.example.drawable.ViewportZeroMarkDrawable;
import com.denispetrov.graphics.example.model.ExampleModel;
import com.denispetrov.graphics.example.plugin.DraggerViewPlugin;
import com.denispetrov.graphics.model.FRectangle;
import com.denispetrov.graphics.plugin.ClickerViewPlugin;
import com.denispetrov.graphics.plugin.PanViewPlugin;
import com.denispetrov.graphics.plugin.TrackerViewPlugin;
import com.denispetrov.graphics.plugin.ZoomViewPlugin;

public class Main {

    protected Shell shell;
    private ViewController viewController;

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
        viewController = new ViewController();
        viewController.setCanvas(canvas);

        viewController.addViewPlugin(new TrackerViewPlugin());
        viewController.addViewPlugin(new PanViewPlugin());
        viewController.addViewPlugin(new ZoomViewPlugin());
        viewController.addViewPlugin(new ClickerViewPlugin());
        viewController.addViewPlugin(new DraggerViewPlugin());

        viewController.addDrawable(new ViewportBackgroundDrawable());
        viewController.addDrawable(new ViewportXAxisDrawable());
        viewController.addDrawable(new ViewportYAxisDrawable());
        viewController.addDrawable(new ViewportZeroMarkDrawable());

        viewController.addDrawable(new ExampleModelRectDrawable());

        viewController.init();

        ViewContext viewContext = new ViewContext();
        viewContext.setMargin(20);
        viewContext.setAllowNegativeBaseX(false);
        viewContext.setAllowNegativeBaseY(false);
        viewController.setViewContext(viewContext);

        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(100,100,100,100));
        model.getRectangles().add(new FRectangle(300,300,100,100));
        viewController.setModel(model);
    }
}
