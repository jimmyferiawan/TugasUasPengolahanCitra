package pengolahancitra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SELECTED_KEY;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;

public class Coba {

    private static final int BINS = 256;
    private BufferedImage image;
    private BufferedImage imageGrayscale;
    private HistogramDataset dataset;
    private XYBarRenderer renderer;
    private XYPlot plot;
    private JFreeChart chart;
    
    private String imagePath;
    private final double maxKeabuan = 2.5;
    private double pembagiKeabuan;
    private final String outputFile = "/home/jimmyferiawan/saved.png";
    private int[] keabuan;
    private double[] keabuanNormalisasi;
    private double mean;
    private double variance;
    private double skewness;
    private int kurtosis;
    private int entropy;
    
    private JFrame f;
    private JPanel controlPanel;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel pnl;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel labelMean;
    private final String textMean = "Mean: ";
    private JLabel labelVariance;
    private final String textVariance = "Variance: ";
    private JLabel labelSkewness;
    private final String textSkewness = "Skewness: ";
    private JLabel labelKurtosis;
    private final String textKurtosis = "Kurtosis: ";
    private JLabel labelEntropy;
    private final String textEntropy = "Entropy: ";
    private JButton jButton1;
    private JButton jButton2;
    private ImageIcon gambar;
    private ImageIcon gambar2;
    private JFileChooser jfc;
    private ChartPanel chartPanel;

    public Coba() {
        keabuan = new int[257];
        keabuanNormalisasi = new double[257];
        mean = 0;
        variance = 0;
        skewness = 0;
        kurtosis = 0;
        entropy = 0;
        
        image = getImage();
        gambar = new ImageIcon(image);
        gambar2 = new ImageIcon();
        jLabel1 = new JLabel(gambar);
        jLabel2 = new JLabel(gambar2);
        labelMean = new JLabel(textMean, SwingConstants.CENTER);
        labelVariance = new JLabel(textVariance, SwingConstants.CENTER);
        labelSkewness = new JLabel(textSkewness, SwingConstants.CENTER);
        labelKurtosis = new JLabel(textKurtosis, SwingConstants.CENTER);
        labelEntropy = new JLabel(textEntropy, SwingConstants.CENTER);
        jButton1 = new JButton("Pilih gambar");
        jButton2 = new JButton("Proses");
        controlPanel = createControlPanel();
        jPanel1 = new JPanel(new GridLayout(0,1));
        jPanel2 = new JPanel(new GridLayout(0,2));
        
        int _w = 0, _h = 0;
        int w = image.getWidth();
        int h = image.getHeight();
        pembagiKeabuan = w*h/2.5;
        imageGrayscale = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
                        
        if(w > 300 && h > 300) {
            if(w>h) {
                _w = 300;
                _h = h/(w/300);
            } else {
                _w = w/(h/300);
                _h = 300;
            }
        } else {
            _w = w;
            _h = h;
        }
        grayscale(w, h, _w, _h);
        chartPanel = createChartPanel(imageGrayscale);
        display();
        pnl.add(chartPanel);
    }
    
    private BufferedImage getImage() {
        try {
            return ImageIO.read(new File("/home/jimmyferiawan/data1.png"));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    private void grayscale(int width, int height, int _width, int _height){
        int p,R,G,B,A,gs;
        for(int baris=0;baris<height;baris++){
            for(int kolom=0;kolom<width;kolom++){
                p = image.getRGB(kolom, baris);
                A = (p>>24) & 0xff;
                R = (((p>>16) & 0xff)*5)/10;
                G = (((p>>8) & 0xff)*8)/10;
                B = ((p & 0xff)*3)/10;
                gs = ((R+G+B)*10)/16;
                
                imageGrayscale.setRGB(kolom, baris, ((gs << 16)+(gs << 8)+gs));
            }
        }
        File outputfile = new File(outputFile);
        try {
            ImageIO.write(imageGrayscale, "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(PengolahanCitra.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            imageGrayscale = null;
            imageGrayscale = ImageIO.read(new File("/home/jimmyferiawan/saved.png"));
        } catch (IOException ex) {
            imageGrayscale = null;
        }
        
        Image dimg1 = imageGrayscale.getScaledInstance(_width, _height, Image.SCALE_REPLICATE);
        jLabel2.setIcon(new ImageIcon(dimg1));
    }
    
    private double getStdev(double[] data){
        int sigma1 = 0;
        long sigma2 = 0;
        double hasil = 0.0;
        for(int i=1;i < data.length;i++) {
            sigma1 = sigma1 + i;
        }
        for(int i=1;i < data.length;i++) {
            sigma2 = sigma2 + (i*i);
            
        }
        hasil = Math.sqrt(((data.length-1)*sigma2-Math.pow(sigma1, 2))/ ((data.length-1)*(data.length-2)));
        return hasil;
    }
    
    private void refreshChart() {
        pnl.remove(chartPanel);
        pnl.repaint();
        pnl.revalidate();
                        
        chartPanel = createChartPanel(imageGrayscale);
        pnl.add(chartPanel);
        pnl.repaint();
        chartPanel.repaint();
        chartPanel.revalidate();
    }
    
    private CategoryDataset createDataset(int[] abu) {
        double[][] data;
        CategoryDataset dataset;
        String[] x;
        
        x = new String[abu.length];
        data = new double[1][abu.length];
        for(int i=0;i<abu.length;i++) {
            x[i] = String.valueOf(i);
        }
        for(int i=1;i<abu.length;i++) {
            data[0][i] = abu[i];
        }
        dataset = DatasetUtilities.createCategoryDataset(new String[]{"Gray"}, x, data);
        
        return dataset;
    }
    
    private JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createAreaChart(
                "Oil consumption",
                "Time",
                "Thousands bbl/day",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                true
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setForegroundAlpha(0.3f);

        AreaRenderer renderer = (AreaRenderer) plot.getRenderer();
        renderer.setEndType(AreaRendererEndType.LEVEL);

        chart.setTitle(new TextTitle("Oil consumption",
                new Font("Serif", java.awt.Font.BOLD, 18))
        );

        return chart;
    }
    
    private ChartPanel createChartPanel(BufferedImage img) {
        // dataset
        keabuan = new int[257];
        dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        Raster raster = img.getRaster();
        final int w = img.getWidth();
        final int h = img.getHeight();
        double[] r = new double[w * h];
        int indicat = 0;
        for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                int result = raster.getSample(x, y, 0);
//                System.out.println(result);
                result++; // untuk mengubah nilai intensitas menjadi 1-256
                r[indicat] = result;
                keabuan[result]++;
                indicat++;
            }
        }
        keabuanNormalisasi = new double[257];
        for(int i=1;i<keabuanNormalisasi.length;i++) {
             
            keabuanNormalisasi[i] = Double.valueOf(keabuan[i]) / Double.valueOf((w*h));
            System.out.println(keabuanNormalisasi[i]);
        }
        
        dataset.addSeries("Gray", r, BINS, -10, 255);
        
        // chart
        chart = ChartFactory.createHistogram("Histogram", "Value", "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        plot = (XYPlot) chart.getPlot();
        renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
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
        panel.setBackground(Color.white);
        return panel;
        //        IntervalXYDataset dataset1;
//        JFreeChart chart1;
//        ChartPanel chartPanel1;
//        XYSeries series1 = new XYSeries("Gray");
//        for(int i=0;i<keabuanNormalisasi.length;i++) {
////            System.err.println(keabuanNormalisasi[i]);
//            series1.add(i, keabuanNormalisasi[i]);
//        }
//        XYSeriesCollection dataset2 = new XYSeriesCollection(series1);
//        dataset.addSeries("Gray", r, BINS, -10, 255);
//        chart1 = ChartFactory.createXYBarChart("Color Intensity   Histogram","X",false,"Y",dataset2,PlotOrientation.VERTICAL,true,true,false);
//        XYPlot plot1 = (XYPlot) chart1.getPlot();
//        NumberAxis xAxis = (NumberAxis) plot1.getDomainAxis();
//        xAxis.setLowerBound(0);
//
//        //To change the lower bound of Y-axis       
//        NumberAxis yAxis = (NumberAxis) plot1.getRangeAxis();
//        yAxis.setLowerBound(0);
//
//        // To change the color
//        XYItemRenderer renderer = plot1.getRenderer();
//        renderer.setSeriesPaint(0, Color.green);
//        panel = new ChartPanel(chart1);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(0,5));
        panel.add(labelMean);
        panel.add(labelVariance);
        panel.add(labelSkewness);
        panel.add(labelKurtosis);
        panel.add(labelEntropy);
        return panel;
    }

    
    void display() {
        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        
        jPanel2.add(jButton1);
        jPanel2.add(jButton2);
        jPanel1.add(jLabel1);
        jPanel1.add(jLabel2);
        
        jfc.setDialogTitle("Pilih Gambar");
        jfc.setDialogType(JFileChooser.FILES_ONLY);
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "png", "jpg", "jpeg");
	jfc.addChoosableFileFilter(filter);
        
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    imagePath = jfc.getSelectedFile().getPath();
                    
                    try {
                        image = ImageIO.read(new File(imagePath));
                        int _w=0, _h=0;
                        int w = image.getWidth();
                        int h = image.getHeight();
                        pembagiKeabuan = (w*h)/2.5;
                        imageGrayscale = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
                        
                        if(w > 300 && h > 300) {
                            if(w>h) {
                                _w = 300;
                                _h = h/(w/300);
                            } else {
                                _w = w/(h/300);
                                _h = 300;
                            }
                        } else {
                            _w = w;
                            _h = h;
                        }
                        grayscale(w, h, _w, _h);
                        Image dimg = image.getScaledInstance(_w, _h, Image.SCALE_REPLICATE);
                        jLabel1.setIcon(new ImageIcon(dimg));
                        refreshChart();
                        
                    } catch (IOException ex) {
                        image = null;
                    }
                }
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                mean = 0;
                variance = 0;
                for(int i=1;i < keabuanNormalisasi.length;i++){
                    mean = mean + (i*keabuanNormalisasi[i]);
                }
                
                for(int i=1;i < keabuanNormalisasi.length;i++){
                    variance = variance + (Math.pow(i-mean,2)*keabuanNormalisasi[i]);
                }
                labelMean.setText(textMean+String.valueOf(mean));
                labelVariance.setText(textVariance + String.valueOf(variance));
                System.out.println("Stdev: " + getStdev(keabuanNormalisasi));
            }
        });
        pnl = new JPanel(new BorderLayout());
        
        JScrollPane jscp = new JScrollPane(pnl);
        pnl.add(jPanel2, BorderLayout.NORTH);
        pnl.add(controlPanel, BorderLayout.SOUTH);
        pnl.add(jPanel1, BorderLayout.WEST);
        f = new JFrame("Histogram");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(jscp);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Coba coba = new Coba();
        });
    }
}