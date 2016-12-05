package com.denispetrov.graphics.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
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

public class Main implements ShellListener {

    protected Shell shell;
    private Canvas canvas;

    private void run() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));
        createContents();
        shell.open();
        shell.layout();
        shell.addShellListener(this);

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
        view.addViewPlugin(new ZoomViewPlugin());
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
        viewContext.setAllowNegativeBaseX(false);
        viewContext.setAllowNegativeBaseY(false);
        view.setViewContext(viewContext);

        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(100,100,100,100));
        model.getRectangles().add(new FRectangle(300,300,100,100));
        model.getDraggableRectangles().add(new FRectangle(300, 100, 100, 100));
        model.getDraggableRectangles().add(new FRectangle(100, 300, 100, 100));
        model.getLabels().add(new Label("Label 1", 100.0, 500.0));
        model.getLabels().add(new Label("Label 2", 200.0, 500.0));
        view.setModel(model);
    }

    @Override
    public void shellActivated(ShellEvent e) {
        createView();
    }

    @Override
    public void shellClosed(ShellEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shellDeactivated(ShellEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shellDeiconified(ShellEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shellIconified(ShellEvent e) {
        // TODO Auto-generated method stub
        
    }
}
