package com.example.panels.report;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class DashboardReport extends JPanel {

    public static class DatabaseConnection {

        private static final String jdbcURL = "jdbc:mysql://localhost:3306/salesedge";
        private static final String dbUser = "root";
        private static final String dbPassword = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        }

        public static void close(Connection connection) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public DashboardReport() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns

        // Create and add charts to the panel
        add(createChartPanel(createCustomerDemographicChart()));
        add(createChartPanel(createProductsByCategoryChart()));
        add(createChartPanel(createGenderDistributionChart()));
        add(createChartPanel(createCurrentStockLevelsChart()));
    }

    // Method to execute SQL queries and return ResultSet
    private ResultSet executeQuery(String sql) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    @SuppressWarnings("rawtypes")
    private JFreeChart createCustomerDemographicChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        try {
            // Example query to fetch customer age ranges
            String query = "SELECT COUNT(*) AS count FROM customer WHERE age >= 18 AND age <= 24";
            ResultSet resultSet = executeQuery(query);
            if (resultSet.next()) {
                dataset.setValue("18-24", resultSet.getInt("count"));
            }

            query = "SELECT COUNT(*) AS count FROM customer WHERE age >= 25 AND age <= 34";
            resultSet = executeQuery(query);
            if (resultSet.next()) {
                dataset.setValue("25-34", resultSet.getInt("count"));
            }

            query = "SELECT COUNT(*) AS count FROM customer WHERE age >= 35 AND age <= 44";
            resultSet = executeQuery(query);
            if (resultSet.next()) {
                dataset.setValue("35-44", resultSet.getInt("count"));
            }

            query = "SELECT COUNT(*) AS count FROM customer WHERE age >= 45";
            resultSet = executeQuery(query);
            if (resultSet.next()) {
                dataset.setValue("45+", resultSet.getInt("count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(null);
        }

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

        try {
            // Example query to fetch number of products by category
            String query = "SELECT category, COUNT(*) AS count FROM product_inventory GROUP BY category";
            ResultSet resultSet = executeQuery(query);
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                int count = resultSet.getInt("count");
                dataset.addValue(count, "Products", category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    private JFreeChart createGenderDistributionChart() {
        //noinspection rawtypes
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Male", genderPercentage("Male"));
        dataset.setValue("Female", genderPercentage("Female"));

        JFreeChart chart = ChartFactory.createPieChart(
                "Customer Gender Distribution",
                dataset,
                true,
                true,
                false
        );

        // Customizing colors for the Pie Chart
        //noinspection rawtypes
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Male", Color.BLUE); // Set color for a Male section
        plot.setSectionPaint("Female", Color.PINK); // Set color for a Female section

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

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Enable zooming
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Larger chart size
        return chartPanel;
    }
}
