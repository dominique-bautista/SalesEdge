package com.example.panels.report;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

        // Fetch real product prices from database
        List<Double> prices = fetchProductPricesFromDatabase();

        // Convert list to array for histogram dataset
        double[] priceArray = prices.stream().mapToDouble(Double::doubleValue).toArray();

        // Adjust bin width based on data range
        double minPrice = prices.isEmpty() ? 0 : prices.stream().min(Double::compare).orElse(0.0);
        double maxPrice = prices.isEmpty() ? 0 : prices.stream().max(Double::compare).orElse(0.0);
        int numBins = Math.min(prices.size(), 10); // Adjust the number of bins as needed

        dataset.addSeries("Product Prices", priceArray, numBins);

        // Create histogram chart with specific color
        JFreeChart chart = ChartFactory.createHistogram(
                "Product Price Distribution",
                "Price",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Include tooltips
                false // Suppress URLs
        );

        // Set custom color and shiny appearance for the bars (histogram bins)
        XYPlot plot = (XYPlot) chart.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 128, 128)); // Teal color for bars

        return chart;
    }

    private List<Double> fetchProductPricesFromDatabase() {
        List<Double> prices = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "");

            // Execute query to fetch product prices
            String query = "SELECT price FROM product_inventory";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Iterate through result set and add prices to list
            while (resultSet.next()) {
                double price = resultSet.getDouble("price");
                prices.add(price);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in finally block
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return prices;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Product Price Distribution Report");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ProductReport productReport = new ProductReport();
            frame.add(productReport);

            frame.pack(); // Adjusts frame size to fit its contents
            frame.setLocationRelativeTo(null); // Centers frame on screen
            frame.setVisible(true);
        });
    }
}