package com.denispetrov.charting.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denispetrov.charting.model.FRectangle;

public class ViewContextTest {
    private static final Logger LOG = LoggerFactory.getLogger(ViewContextTest.class);

    private Shell shell;
    private Canvas canvas;
    private View<Object> view;
    private ViewContext viewContext;
    private static final int CANVAS_WIDTH = 400;
    private static final int CANVAS_HEIGHT = 300;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));
        shell.layout(false);
        canvas = new Canvas(shell, SWT.NONE);
        canvas.setSize(CANVAS_WIDTH,CANVAS_HEIGHT);
        LOG.info("Canvas size {}", canvas.getSize());
        view = new View<>();
        view.setCanvas(canvas);
        view.init();
        viewContext = new ViewContext();
        viewContext.setView(view);
    }

    @After
    public void tearDown() throws Exception {
        shell.dispose();
    }

    private static final int MARGIN_15 = 15;
    private static final int MARGIN_16 = 16;
    private static final int MARGIN_17 = 17;
    private static final int MARGIN_18 = 18;
    private static final int MARGIN_19 = 19;
    @Test
    public void testMarginGettersSetters() {
        assertNotEquals(MARGIN_19, viewContext.getMarginTop());
        assertNotEquals(MARGIN_19, viewContext.getMarginLeft());
        assertNotEquals(MARGIN_19, viewContext.getMarginRight());
        assertNotEquals(MARGIN_19, viewContext.getMarginBottom());
        viewContext.setMargin(MARGIN_19);
        assertEquals(MARGIN_19, viewContext.getMarginTop());
        assertEquals(MARGIN_19, viewContext.getMarginLeft());
        assertEquals(MARGIN_19, viewContext.getMarginRight());
        assertEquals(MARGIN_19, viewContext.getMarginBottom());
        viewContext.setMarginTop(MARGIN_15);
        assertEquals(MARGIN_15, viewContext.getMarginTop());
        assertNotEquals(MARGIN_15, viewContext.getMarginLeft());
        assertNotEquals(MARGIN_15, viewContext.getMarginRight());
        assertNotEquals(MARGIN_15, viewContext.getMarginBottom());
        viewContext.setMarginLeft(MARGIN_16);
        assertEquals(MARGIN_16, viewContext.getMarginLeft());
        assertNotEquals(MARGIN_16, viewContext.getMarginTop());
        assertNotEquals(MARGIN_16, viewContext.getMarginRight());
        assertNotEquals(MARGIN_16, viewContext.getMarginBottom());
        viewContext.setMarginRight(MARGIN_17);
        assertEquals(MARGIN_17, viewContext.getMarginRight());
        assertNotEquals(MARGIN_17, viewContext.getMarginTop());
        assertNotEquals(MARGIN_17, viewContext.getMarginLeft());
        assertNotEquals(MARGIN_17, viewContext.getMarginBottom());
        viewContext.setMarginBottom(MARGIN_18);
        assertEquals(MARGIN_18, viewContext.getMarginBottom());
        assertNotEquals(MARGIN_18, viewContext.getMarginTop());
        assertNotEquals(MARGIN_18, viewContext.getMarginLeft());
        assertNotEquals(MARGIN_18, viewContext.getMarginRight());

//        public int getCanvasWidth() {
//            return canvas.getBounds().width;
//        }
//
//        public double getWidth() {
//            return w(getCanvasWidth() - leftMargin - rightMargin);
//        }
//
//        public int getCanvasHeight() {
//            return canvas.getBounds().height;
//        }
//
//        public double getHeight() {
//            return h(getCanvasHeight() - topMargin - bottomMargin);
//        }
//
//        public GC getGC() {
//            return gc;
//        }
//
//        public void setGC(GC gc) {
//            this.gc = gc;
//        }
//
//        public double getScaleX() {
//            return scaleX;
//        }
//
//        public void setScaleX(double scaleX) {
//            this.scaleX = scaleX;
//        }
//
//        public double getScaleY() {
//            return scaleY;
//        }
//
//        public void setScaleY(double scaleY) {
//            this.scaleY = scaleY;
//        }
//
//        public double getBaseX() {
//            return baseX;
//        }
//
//        public void setBaseX(double baseX) {
//            switch (xAxisRange) {
//            default:
//                this.baseX = baseX;
//                break;
//            case POSITIVE_ONLY:
//                this.baseX = Math.max(0.0, baseX);
//                break;
//            case NEGATIVE_ONLY:
//                Rectangle mainAreaRectangle = getMainAreaRectangle();
//                double maxBaseX = -w(mainAreaRectangle.width);
//                this.baseX = Math.min(baseX, maxBaseX);
//                LOG.debug("maxBaseX = {}, baseX = {}", maxBaseX, baseX);
//                break;
//            }
//        }
//
//        public double getBaseY() {
//            return baseY;
//        }
//
//        public void setBaseY(double baseY) {
//            switch (yAxisRange) {
//            default:
//                this.baseY = baseY;
//                break;
//            case POSITIVE_ONLY:
//                this.baseY = Math.max(0.0, baseY);
//                break;
//            case NEGATIVE_ONLY:
//                Rectangle mainAreaRectangle = getMainAreaRectangle();
//                double maxBaseY = -h(mainAreaRectangle.height);
//                this.baseY = Math.min(baseY, maxBaseY);
//                break;
//            }
//        }
//
//        public Object getModel() {
//            return this.model;
//        }
//
//        public void setModel(Object model) {
//            this.model = model;
//        }
//
//        public Canvas getCanvas() {
//            return canvas;
//        }
//
//        public void setView(View view) {
//            if (this.canvas != null) {
//                this.canvas.removeControlListener(this);
//            }
//            this.view = view;
//            this.canvas = view.getCanvas();
//            this.canvas.addControlListener(this);
//            calculateMainAreaRectangle();
//            validateBase();
//            if (backgroundColor == null) {
//                backgroundColor = canvas.getDisplay().getSystemColor(DEFAULT_BACKGROUND_COLOR);
//            }
//            if (foregroundColor == null) {
//                foregroundColor = canvas.getDisplay().getSystemColor(DEFAULT_FOREGROUND_COLOR);
//            }
//        }
//
//        public Rectangle getMainAreaRectangle() {
//            return mainAreaRectangle;
//        }
//
//        public int getDragThreshold() {
//            return dragThreshold;
//        }
//
//        public void setDragThreshold(int dragThreshold) {
//            this.dragThreshold = dragThreshold;
//        }
//
//        public Color getBackgroundColor() {
//            return backgroundColor;
//        }
//
//        public Color getForegroundColor() {
//            return foregroundColor;
//        }
//
//        public void setBackgroundColor(Color backgroundColor) {
//            this.backgroundColor = backgroundColor;
//        }
//
//        public void setForegroundColor(Color foregroundColor) {
//            this.foregroundColor = foregroundColor;
//        }
//
//        public AxisRange getXAxisRange() {
//            return xAxisRange;
//        }
//
//        public void setXAxisRange(AxisRange xAxisRange) {
//            this.xAxisRange = xAxisRange;
//        }
//
//        public AxisRange getYAxisRange() {
//            return yAxisRange;
//        }
//
//        public void setYAxisRange(AxisRange yAxisRange) {
//            this.yAxisRange = yAxisRange;
//        }
//
//        public double getResizeCenterX() {
//            return resizeCenterX;
//        }
//
//        public void setResizeCenterX(double resizeCenterX) {
//            this.resizeCenterX = resizeCenterX;
//        }
//
//        public double getResizeCenterY() {
//            return resizeCenterY;
//        }
//
//        public void setResizeCenterY(double resizeCenterY) {
//            this.resizeCenterY = resizeCenterY;
//        }


    }

    @Test
    public void testRectangle() {
        FRectangle fRect = new FRectangle(
                180.0,
                100.0,
                20.0,
                20.0
                );
        Rectangle rect = viewContext.rectangle(fRect);
        LOG.info("{}", rect);
//        assertEquals(190,rect.x);
//        assertEquals(190,rect.y);
//        assertEquals(20,rect.width);
//        assertEquals(20,rect.height);
        FRectangle fRectOut = viewContext.rectangle(rect);
        assertEquals(fRect.x, fRectOut.x, 0.000001);
        assertEquals(fRect.y, fRectOut.y, 0.000001);
        assertEquals(fRect.w, fRectOut.w, 0.000001);
        assertEquals(fRect.h, fRectOut.h, 0.000001);
    }

    @Test
    public void testDimensions() {
        assertEquals(CANVAS_WIDTH, view.getCanvasWidth());
        assertEquals(CANVAS_HEIGHT, view.getCanvasHeight());
        assertEquals(CANVAS_WIDTH-ViewContext.DEFAULT_MARGIN*2,view.getWidth(), 0.000001);
        assertEquals(CANVAS_HEIGHT-ViewContext.DEFAULT_MARGIN*2,view.getHeight(), 0.000001);
    }
}
