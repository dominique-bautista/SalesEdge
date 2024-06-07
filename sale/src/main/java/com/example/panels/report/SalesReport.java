package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;

public class SalesReport extends JPanel {

    public SalesReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the sales report panel
        add(createChartPanel(createSalesOverTimeChart()));
        add(createChartPanel(createSalesByCategoryChart()));
        add(createChartPanel(createSalesByProductChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createSalesOverTimeChart() {
        XYSeries series = new XYSeries("Sales Over Time");
        series.add(1, 500);
        series.add(2, 700);
        series.add(3, 300);
        series.add(4, 900);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return ChartFactory.createXYLineChart(
                "Sales Over Time",
                "Time",
                "Sales",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private JFreeChart createSalesByCategoryChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Category A", 40);
        dataset.setValue("Category B", 30);
        dataset.setValue("Category C", 20);
        dataset.setValue("Category D", 10);
        return ChartFactory.createPieChart(
                "Sales by Category",
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart createSalesByProductChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(200, "Sales", "Product A");
        dataset.addValue(400, "Sales", "Product B");
        dataset.addValue(600, "Sales", "Product C");
        return ChartFactory.createBarChart(
                "Sales by Product",
                "Product",
                "Sales",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }
}
