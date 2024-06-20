package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class DashboardReport extends JPanel {

    public DashboardReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the panel
        add(createChartPanel(createCustomerDemographicChart()));
        add(createChartPanel(createProductsByCategoryChart()));
        add(createChartPanel(createSalesByStaffChart()));
        add(createChartPanel(createInventoryLevelsChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createCustomerDemographicChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("18-24", CustomerReport.countAgeOfRange(18,24));
        dataset.setValue("25-34", CustomerReport.countAgeOfRange(25,34));
        dataset.setValue("35-44", CustomerReport.countAgeOfRange(35,44));
        dataset.setValue("45+", CustomerReport.countAgeOfRange(45,200));
        return ChartFactory.createPieChart(
                "Customer Demographic",
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart createProductsByCategoryChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Products", "Category A");
        dataset.addValue(200, "Products", "Category B");
        dataset.addValue(150, "Products", "Category C");
        return ChartFactory.createBarChart(
                "Products by Category",
                "Category",
                "Number of Products",
                dataset,
                PlotOrientation.VERTICAL,
                false,
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

    private JFreeChart createInventoryLevelsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(50, "Inventory Level", "Warehouse 1");
        dataset.addValue(30, "Inventory Level", "Warehouse 2");
        dataset.addValue(40, "Inventory Level", "Warehouse 3");
        return ChartFactory.createBarChart(
                "Inventory Levels",
                "Warehouse",
                "Inventory Level",
                dataset,
                PlotOrientation.HORIZONTAL,
                false,
                true,
                false
        );
    }
}
