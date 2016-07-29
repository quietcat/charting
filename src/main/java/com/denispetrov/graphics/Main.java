package com.denispetrov.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

    protected Shell shell;
    private ViewController<ExampleModel> controller;

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
        Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
        controller = new ViewController<>();
        controller.setCanvas(canvas);
        ObjectTrackerViewPlugin otvp = new ObjectTrackerViewPlugin();
        controller.addViewPlugin(otvp);
        PannerViewPlugin pvp = new PannerViewPlugin();
        controller.addViewPlugin(pvp);
        controller.addViewportDrawer(new ViewportXAxisDrawer());
        controller.addViewportDrawer(new ViewportYAxisDrawer());
        ExampleModelRectDrawer emrd = new ExampleModelRectDrawer();
        emrd.setObjectTracker(otvp);
        controller.addModelDrawer(emrd);
        controller.addViewportDrawer(new ViewportZeroMarkDrawer());
        controller.init();
        ViewContext<ExampleModel> viewContext = new ViewContext<>();
        controller.setViewContext(viewContext);
        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(100,100,100,100));
        model.getRectangles().add(new FRectangle(300,300,100,100));
        controller.setModel(model);
    }
}
