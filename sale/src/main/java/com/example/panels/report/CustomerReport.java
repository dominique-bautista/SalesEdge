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
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import java.awt.Color;

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
        dataset.setValue("Male", genderPercentage("Male"));
        dataset.setValue("Female", genderPercentage("Female"));

        JFreeChart chart = ChartFactory.createPieChart(
                "Gender Distribution",
                dataset,
                true,
                true,
                false
        );

        // Customizing colors for the Pie Chart
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Male", Color.BLUE); // Set color for Male section
        plot.setSectionPaint("Female", Color.PINK); // Set color for Female section

        return chart;
    }

    private int genderPercentage(String gender) {
        int totalCount = 0;
        int genderCount = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "")) { // Adjust connection details
            // Query to get the total count of records
            String totalCountQuery = "SELECT COUNT(*) FROM customer";
            try (PreparedStatement totalCountStatement = con.prepareStatement(totalCountQuery);
                 ResultSet totalCountResultSet = totalCountStatement.executeQuery()) {
                if (totalCountResultSet.next()) {
                    totalCount = totalCountResultSet.getInt(1);
                }
            }

            // Query to get the count of records matching the specified gender
            String genderCountQuery = "SELECT COUNT(*) FROM customer WHERE gender =?";
            try (PreparedStatement genderCountStatement = con.prepareStatement(genderCountQuery)) {
                genderCountStatement.setString(1, gender);
                try (ResultSet genderCountResultSet = genderCountStatement.executeQuery()) {
                    if (genderCountResultSet.next()) {
                        genderCount = genderCountResultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 or another error indicator on failure
        }

        // Calculate the percentage
        if (totalCount > 0) {
            double percentage = ((double) genderCount / totalCount) * 100;
            return (int) percentage; // Truncates to int. Consider returning double for precision.
        } else {
            return 0; // Avoid division by zero
        }
    }

    private JFreeChart createAgeDistributionChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(countAgeOfRange(18, 24), "Age Group", "18-24");
        dataset.addValue(countAgeOfRange(25, 24), "Age Group", "25-34");
        dataset.addValue(countAgeOfRange(35, 24), "Age Group", "35-44");
        dataset.addValue(countAgeOfRange(45, 200), "Age Group", "45+");

        JFreeChart chart = ChartFactory.createBarChart(
                "Age Distribution",
                "Age Group",
                "Number of Customers",
                dataset
        );

        // Customizing colors for the Bar Chart
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.ORANGE); // Set color for the bars

        return chart;
    }

    public static int countAgeOfRange(int start, int end) {
        int count = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "")) { // Adjust connection details as necessary
            String query = "SELECT COUNT(*) FROM customer WHERE age >=? AND age <=?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, start);
                preparedStatement.setInt(2, end);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL exception appropriately
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection exception appropriately
        }
        return count;
    }
}
