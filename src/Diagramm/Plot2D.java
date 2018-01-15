package Diagramm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class Plot2D {
    private static Vector<PlotWindow> figures;
    private static PlotWindow currentFigure;

    public static void main(String[] args) {
        plot(new double[]{1, 3, 9, 8, 4}, new double[]{7, 2, 3, 5, 9});
    }

    public static void plot(double[] y) {
        double[] x = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        plot(x, y);
    }

    public static void plot(double[] x, double[] y) {
        if (currentFigure == null) {
            figures = new Vector<>();
            currentFigure = new PlotWindow("Figure1");
            figures.add(currentFigure);
        }
        currentFigure.setArrays(x, y);
        currentFigure.draw();
    }

    public static void setFigure(String figureName) {
        currentFigure = findFigure(figureName);
    }

    private static PlotWindow findFigure(String figureName) {
        for (PlotWindow figure : figures) {
            if (figure.getName() == figureName) return figure;
        }
        int n = figures.size();
        PlotWindow pw = new PlotWindow(figureName);
        figures.add(pw);
        return pw;
    }

    static void figureClose(PlotWindow figure) {
        if (currentFigure == figure) currentFigure = figures.firstElement();
        figures.remove(figure);
    }
}

class PlotWindow extends JFrame {
    private Plot2D parent;
    private PlotArea panel;
    private double[] axes;
    private double[] axis;
    private double[] xArray;
    private double[] yArray;
    private double xmin, xmax, ymin, ymax;
    private boolean isExist = false;

    PlotWindow(String name) {
        super(name);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        panel = new PlotArea();
        panel.setPreferredSize(new Dimension(400, 300));
        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                callClose();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isExist) {
                    draw();
                }
                repaint();
            }
        });
        pack();
        setVisible(true);
    }

    public void setArrays(double[] xArray, double[] yArray) {
        this.xArray = xArray;
        this.yArray = yArray;
        isExist = true;
    }

    public void draw() {
        int lenx = xArray.length;
        int leny = yArray.length;
        int len = lenx;
        if (lenx < leny) len = leny;
        for (int i = 0; i < len; i++) {
            double x = xArray[i];
            double y = yArray[i];
            if (x > xmax) xmax = x;
            if (x < xmin) xmin = x;
            if (y > ymax) ymax = y;
            if (y < ymin) ymin = y;
        }
        int[] pxArray = new int[len];
        int[] pyArray = new int[len];
        for (int i = 0; i < len; i++) {
            double x = xArray[i];
            double y = yArray[i];
            pxArray[i] = (int) Math.floor((x - xmin) / (xmax - xmin) * (double) panel.getWidth());
            pyArray[i] = (int) Math.floor((double) panel.getHeight() - (y - ymin) / (ymax - ymin) * (double) panel.getHeight());
        }
        panel.setArrays(pxArray, pyArray);
    }

    private void callClose() {
        Plot2D.figureClose(this);
        dispose();
    }
}

class PlotArea extends JPanel {
    private int[] xArray;
    private int[] yArray;

    public void setArrays(int[] xArray, int[] yArray) {
        this.xArray = xArray;
        this.yArray = yArray;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xlast = xArray[0];
        int ylast = yArray[0];
        for (int i = 1; i < xArray.length; i++) {
            int x = xArray[i];
            int y = yArray[i];
            g.drawLine(xlast, ylast, x, y);
            xlast = x;
            ylast = y;
        }
    }
}