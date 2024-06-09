package com.example.panels.report;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

public class SalesReport extends JPanel {

    public SalesReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the sales report panel
        add(createChartPanel(createSalesByCategoryChart()));
        add(createChartPanel(createSalesByStaffChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
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

    private JFreeChart createSalesByStaffChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Staff A", 50);
        dataset.setValue("Staff B", 30);
        dataset.setValue("Staff C", 20);
        return ChartFactory.createPieChart(
                "Sales by Staff",
                dataset,
                true,
                true,
                false
        );
    }
}
