package proxyproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class DynamicChart extends ApplicationFrame implements ActionListener {

    private TimeSeries series;
    private TimeSeries series1;
    private TimeSeries series2;
    private TimeSeries series3;
    private TimeSeries series4;
    
//    private double lastValue = 100;
//    private int lastValue = Threads.dataTransfer;

    private Timer timer = new Timer(1000, this);

    public DynamicChart(final String title) {

        super(title);
        this.series = new TimeSeries("Data summary", Millisecond.class);
        this.series1 = new TimeSeries("Text data", Millisecond.class);
        this.series2 = new TimeSeries("App Data", Millisecond.class);
        this.series3 = new TimeSeries("Image Data", Millisecond.class);
        this.series4 = new TimeSeries("Video Data", Millisecond.class);

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        
        final JFreeChart chart = createChart(dataset);

        timer.setInitialDelay(1000);
        chart.setBackgroundPaint(Color.LIGHT_GRAY);

        final JPanel content = new JPanel(new BorderLayout());
        final ChartPanel chartPanel = new ChartPanel(chart);

        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        setContentPane(content);
        timer.start();

    }

    private JFreeChart createChart(final XYDataset dataset) {
        
        final JFreeChart result = ChartFactory.createTimeSeriesChart("Web Statistics", "Time [s]", "Data [bytes]", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        
        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);

        ValueAxis xaxis = plot.getDomainAxis();
        xaxis.setAutoRange(true);
        
        xaxis.setFixedAutoRange(60000.0);  // 60 seconds
        xaxis.setVerticalTickLabels(true);

        ValueAxis yaxis = plot.getRangeAxis();
//        yaxis.setRange(0.0, 300.0);
        yaxis.setAutoRange(true);
        
        return result;
        
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        int sumValue = Threads.transferSum;
//        int sumTransferValue = Threads.dataTransfer;
        int textTransferValue = Threads.textTransfer;
        int appTransferValue = Threads.appTransfer;
        int imageTranferValue = Threads.imageTransfer;
        int videoTransferValue = Threads.videoTransfer;

        final Millisecond now = new Millisecond();
        this.series.add(new Millisecond(), sumValue);
        this.series1.add(new Millisecond(), textTransferValue);
        this.series2.add(new Millisecond(), appTransferValue);
        this.series3.add(new Millisecond(), imageTranferValue);
        this.series4.add(new Millisecond(), videoTransferValue);
//        this.series.addOrUpdate(new Millisecond(), value);
//        System.out.println("Current Time : " + now.toString() + ", Current Value : " + sumTransferValue);
    }

}  

