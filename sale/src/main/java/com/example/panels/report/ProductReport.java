package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ProductReport extends JPanel {

    public ProductReport() {
        setLayout(new GridLayout(1, 1)); // 1 row, 1 column

        // Create and add chart to the product report panel
        add(createChartPanel(createPriceVsStockChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createPriceVsStockChart() {
        XYSeries series = new XYSeries("Price vs Stock");
        series.add(10, 100);
        series.add(20, 80);
        series.add(30, 60);
        series.add(40, 40);
        series.add(50, 20);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return ChartFactory.createXYLineChart(
                "Price vs Stock Levels",
                "Price",
                "Stock Level",
                dataset
        );
    }
}
