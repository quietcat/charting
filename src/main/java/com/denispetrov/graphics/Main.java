package com.denispetrov.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

    protected Shell shell;
    private GraphingController<ExampleModel> controller;

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
        ExampleModel model = new ExampleModel();
        GraphingContext grc = new GraphingContext();
        controller = new GraphingController<>();
        Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
        controller.setCanvas(canvas);
        controller.setGrc(grc);
        controller.setModel(model);
    }
}
