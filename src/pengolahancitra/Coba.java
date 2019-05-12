package pengolahancitra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SELECTED_KEY;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

public class Coba {
    
    private static final int BINS = 256;
    private final BufferedImage image = getImage();
    private HistogramDataset dataset;
    private XYBarRenderer renderer;

    private BufferedImage getImage() {
        try {
//            return ImageIO.read(new URL(
//                "http://i.imgur.com/kxXhIH1.jpg"));
               return ImageIO.read(new File("/home/jimmyferiawan/Desktop/saved.jpg"));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    private ChartPanel createChartPanel() {
        // dataset
        dataset = new HistogramDataset();
        Raster raster = image.getRaster();
        final int w = image.getWidth();
        final int h = image.getHeight();
        double[] r = new double[w * h];
        int indicat = 0;
        for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                r[indicat] = raster.getSample(x, y, 0);
                indicat++;
            }
        }
//        r = raster.getSamples(0, 0, w, h, 0, r);
        System.out.println(r[0]);
        dataset.addSeries("Gray", r, BINS);
//        r = raster.getSamples(0, 0, w, h, 1, r);
//        dataset.addSeries("Green", r, BINS);
//        r = raster.getSamples(0, 0, w, h, 2, r);
//        dataset.addSeries("Blue", r, BINS);
        // chart
        
        JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
            "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        
        XYPlot plot = (XYPlot) chart.getPlot();
        
        renderer = (XYBarRenderer) plot.getRenderer();
        
        renderer.setBarPainter(new StandardXYBarPainter());
        // translucent red, green & blue
        System.out.println(plot.getDomainAxisCount());
        System.out.println(plot.getRangeAxisCount());
        Paint[] paintArray = {
            new Color(0x8003575e, true),
        };
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
            paintArray,
            DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox(new VisibleAction(0)));
        return panel;
    }

    private class VisibleAction extends AbstractAction {

        private final int i;

        public VisibleAction(int i) {
            this.i = i;
            this.putValue(NAME, (String) dataset.getSeriesKey(i));
            this.putValue(SELECTED_KEY, true);
            renderer.setSeriesVisible(i, true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
        }
    }

    void display() {
        JFrame f = new JFrame("Histogram");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(createChartPanel());
        f.add(createControlPanel(), BorderLayout.SOUTH);
        f.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Coba().display();
        });
    }
}