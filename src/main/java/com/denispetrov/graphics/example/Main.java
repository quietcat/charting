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
import com.denispetrov.graphics.view.ViewContext.AxisRange;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Scale;

public class Main {

    protected Shell shell;
    private Canvas canvas;
    private View view;

    private void run() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setSize(612, 427);
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
    protected void createContents() {

        TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

        TabItem tbtmFull = new TabItem(tabFolder, SWT.NONE);
        tbtmFull.setText("Zooming");

        Composite composite_1 = new Composite(tabFolder, SWT.NONE);
        tbtmFull.setControl(composite_1);
        composite_1.setLayout(new GridLayout(1, false));
        canvas = new Canvas(composite_1, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite composite_2 = new Composite(composite_1, SWT.NONE);
        composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        composite_2.setLayout(new GridLayout(2, false));

        Group grpXAxisRange = new Group(composite_2, SWT.NONE);
        grpXAxisRange.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        grpXAxisRange.setText("X Axis Range");
        grpXAxisRange.setBounds(0, 0, 78, 78);
        grpXAxisRange.setLayout(new FillLayout(SWT.VERTICAL));

        Button btnFull = new Button(grpXAxisRange, SWT.RADIO);
        btnFull.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setXAxisRange(AxisRange.FULL);
            }
        });
        btnFull.setSelection(true);
        btnFull.setText("Full");

        Button btnPositive = new Button(grpXAxisRange, SWT.RADIO);
        btnPositive.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setXAxisRange(AxisRange.POSITIVE_ONLY);
                view.getViewContext().setBaseX(view.getViewContext().getBaseX());
                view.contextUpdated();
            }
        });
        btnPositive.setText("Positive");

        Button btnNegative = new Button(grpXAxisRange, SWT.RADIO);
        btnNegative.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setXAxisRange(AxisRange.NEGATIVE_ONLY);
                view.getViewContext().setBaseX(view.getViewContext().getBaseX());
                view.contextUpdated();
            }
        });
        btnNegative.setText("Negative");

        Button btnStickyZero = new Button(grpXAxisRange, SWT.CHECK);
        btnStickyZero.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ZoomViewPlugin zvp = view.findPlugin(ZoomViewPlugin.class);
                zvp.setStickyX(btnStickyZero.getSelection());
            }
        });
        btnStickyZero.setText("Sticky Zero");
        
        Scale scale = new Scale(grpXAxisRange, SWT.NONE);
        scale.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                double resizeCenterX = (double)scale.getSelection() / 100.0;
                view.getViewContext().setResizeCenterX(resizeCenterX);
                view.contextUpdated();
            }
        });
        scale.setIncrement(1);
        scale.setMaximum(0);
        scale.setMinimum(100);
        scale.setSelection(0);

        Group grpYAxisRange = new Group(composite_2, SWT.NONE);
        grpYAxisRange.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        grpYAxisRange.setText("Y Axis Range");
        grpYAxisRange.setLayout(new FillLayout(SWT.VERTICAL));

        Button btnFull_1 = new Button(grpYAxisRange, SWT.RADIO);
        btnFull_1.setSelection(true);
        btnFull_1.setText("Full");

        Button btnPositive_1 = new Button(grpYAxisRange, SWT.RADIO);
        btnPositive_1.setText("Positive");

        Button btnNegative_1 = new Button(grpYAxisRange, SWT.RADIO);
        btnNegative_1.setText("Negative");

        Button btnStickyZero_1 = new Button(grpYAxisRange, SWT.CHECK);
        btnStickyZero_1.setText("Sticky Zero");
        
        Scale scale_1 = new Scale(grpYAxisRange, SWT.NONE);
        scale_1.setMaximum(0);
        scale_1.setMinimum(100);
        scale_1.setSelection(0);
        scale_1.setIncrement(1);

    }

    protected void createView() {
        view = new View();
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
        view.setViewContext(viewContext);

        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(-100, 100, 100, 100));
        model.getRectangles().add(new FRectangle(-300, 300, 100, 100));
        model.getDraggableRectangles().add(new FRectangle(-300, 100, 100, 100));
        model.getDraggableRectangles().add(new FRectangle(-100, 300, 100, 100));
        model.getLabels().add(new Label("Label 1", -500.0, 100.0));
        model.getLabels().add(new Label("Label 2", -500.0, 200.0));
        view.setModel(model);
    }
}
