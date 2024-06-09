package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class ProductReport extends JPanel {

    public ProductReport() {
        setLayout(new GridLayout(1, 1)); // 1 row, 1 column

        // Create and add chart to the product report panel
        add(createChartPanel(createProductPriceDistributionChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(500, 400)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createProductPriceDistributionChart() {
        HistogramDataset dataset = new HistogramDataset();

        // Example data - you should replace these with real data
        double[] prices = {10, 20, 20, 30, 30, 30, 40, 40, 40, 40, 50, 50};
        dataset.addSeries("Product Prices", prices, 5); // Number of bins (groups)

        return ChartFactory.createHistogram(
                "Product Price Distribution",
                "Price",
                "Frequency",
                dataset
        );
    }
}
