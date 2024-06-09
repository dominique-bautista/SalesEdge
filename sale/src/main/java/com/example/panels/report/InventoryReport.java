package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class InventoryReport extends JPanel {

    public InventoryReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the inventory report panel
        add(createChartPanel(createCurrentStockLevelsChart()));
        add(createChartPanel(createLowStockAlertChart()));
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }

    private JFreeChart createCurrentStockLevelsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Fetch product names and stock levels from the database
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "")) { // Adjust connection details as necessary
            String query = "SELECT product_name, stock_level FROM product_inventory";
            try (PreparedStatement preparedStatement = con.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String productName = resultSet.getString("product_name");
                    int stockLevel = resultSet.getInt("stock_level");
                    dataset.addValue(stockLevel, "Current Stock", productName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL exception appropriately
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection exception appropriately
        }

        return ChartFactory.createBarChart(
                "Current Stock Levels",
                "Product",
                "Stock Level",
                dataset
        );
    }

    private JFreeChart createLowStockAlertChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int threshold = 100;// input low stock alerts
        // Fetch product names and stock levels from the database
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "")) { // Adjust connection details as necessary
            String query = "SELECT product_name, stock_level FROM product_inventory where stock_level < " + threshold;
            try (PreparedStatement preparedStatement = con.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String productName = resultSet.getString("product_name");
                    int stockLevel = resultSet.getInt("stock_level");
                    dataset.addValue(stockLevel, "Low Stock", productName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL exception appropriately
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection exception appropriately
        }

        return ChartFactory.createBarChart(
                "Low Stock Alert",
                "Product",
                "Quantity",
                dataset
        );
    }
}
