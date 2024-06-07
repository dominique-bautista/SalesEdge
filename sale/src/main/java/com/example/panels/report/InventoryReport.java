package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class InventoryReport extends JPanel {

    public InventoryReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the inventory report panel
        add(createChartPanel(createCurrentStockLevelsChart()));
        add(createChartPanel(createLowStockAlertChart()));
        add(createChartPanel(createStockLevelsOverTimeChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createCurrentStockLevelsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Current Stock", "Product A");
        dataset.addValue(150, "Current Stock", "Product B");
        dataset.addValue(200, "Current Stock", "Product C");
        return ChartFactory.createBarChart(
                "Current Stock Levels",
                "Product",
                "Stock Level",
                dataset
        );
    }

    private JFreeChart createLowStockAlertChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5, "Low Stock", "Product A");
        dataset.addValue(2, "Low Stock", "Product B");
        dataset.addValue(1, "Low Stock", "Product C");
        return ChartFactory.createBarChart(
                "Low Stock Alert",
                "Product",
                "Quantity",
                dataset
        );
    }

    private JFreeChart createStockLevelsOverTimeChart() {
        XYSeries series = new XYSeries("Stock Levels Over Time");
        series.add(1, 50);
        series.add(2, 70);
        series.add(3, 60);
        series.add(4, 80);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return ChartFactory.createXYLineChart(
                "Stock Levels Over Time",
                "Time",
                "Stock Level",
                dataset
        );
    }
}
