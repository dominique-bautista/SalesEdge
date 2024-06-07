package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class CustomerReport extends JPanel {

    public CustomerReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the customer report panel
        add(createChartPanel(createGenderDistributionChart()));
        add(createChartPanel(createAgeDistributionChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createGenderDistributionChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Male", 60);
        dataset.setValue("Female", 40);
        return ChartFactory.createPieChart(
                "Gender Distribution",
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart createAgeDistributionChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(30, "Age Group", "18-24");
        dataset.addValue(40, "Age Group", "25-34");
        dataset.addValue(20, "Age Group", "35-44");
        dataset.addValue(10, "Age Group", "45+");
        return ChartFactory.createBarChart(
                "Age Distribution",
                "Age Group",
                "Number of Customers",
                dataset
        );
    }
}
