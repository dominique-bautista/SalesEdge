package com.example.panels.report;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SalesReport extends JPanel {

    public SalesReport() {
        setLayout(new BorderLayout()); // Use BorderLayout to center the chart

        // Create the chart panel
        ChartPanel chartPanel = new ChartPanel(createSalesByCategoryChart());
        chartPanel.setMouseWheelEnabled(true); // Enable zooming

        // Add chart panel to the center of this panel
        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createSalesByCategoryChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "");

            String query = "SELECT category, COUNT(*) AS count FROM product_inventory GROUP BY category";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                int count = resultSet.getInt("count");
                dataset.setValue(category, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ChartFactory.createPieChart(
                "Sales by Category",
                dataset,
                true,
                true,
                false
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sales Report");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SalesReport salesReport = new SalesReport();
            frame.add(salesReport);

            frame.pack(); // Adjusts frame size to fit its contents
            frame.setLocationRelativeTo(null); // Centers frame on screen
            frame.setVisible(true);
        });
    }
}
