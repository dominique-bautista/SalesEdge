package com.example.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePanel extends JPanel {

    private static final Color ACCENT_COLOR = new Color(0xF47130);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;

    public HomePanel() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20)); // Add padding between components

        // Create a wrapper panel for a top section to add extra padding
        JPanel topWrapperPanel = new JPanel(new BorderLayout());
        topWrapperPanel.setBackground(BACKGROUND_COLOR);
        topWrapperPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around the wrapper panel
        JPanel totalSalesPanel = createRoundedPanel("Total Sales", getTotalSales(), new Font("Roboto", Font.BOLD, 48));
        topWrapperPanel.add(totalSalesPanel, BorderLayout.CENTER);
        add(topWrapperPanel, BorderLayout.NORTH);

        // Create a wrapper panel for a middle section to add extra padding
        JPanel middleWrapperPanel = new JPanel(new BorderLayout());
        middleWrapperPanel.setBackground(BACKGROUND_COLOR);
        middleWrapperPanel.setBorder(new EmptyBorder(0, 20, 0, 20)); // Add padding around the wrapper panel
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        middlePanel.setBackground(BACKGROUND_COLOR);
        middlePanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove padding around the inner panel
        middlePanel.add(createRoundedPanel("Customers", getCustomerCount(), new Font("Roboto", Font.BOLD, 24)));
        middlePanel.add(createRoundedPanel("Products", getLowStockCount(), new Font("Roboto", Font.BOLD, 24)));
        middleWrapperPanel.add(middlePanel, BorderLayout.CENTER);
        add(middleWrapperPanel, BorderLayout.CENTER);

        // Create a wrapper panel for a bottom section to add extra padding
        JPanel bottomWrapperPanel = new JPanel(new BorderLayout());
        bottomWrapperPanel.setBackground(BACKGROUND_COLOR);
        bottomWrapperPanel.setBorder(new EmptyBorder(0, 20, 20, 20)); // Add padding around the wrapper panel

        add(bottomWrapperPanel, BorderLayout.SOUTH);
    }

    // Method to create a rounded panel for summary
    private JPanel createRoundedPanel(String title, int value, Font valueFont) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BACKGROUND_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }

            @Override
            protected void paintBorder(Graphics g) {
                super.paintBorder(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 0)); // Set the background to be transparent

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel valueLabel = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        valueLabel.setFont(valueFont);
        valueLabel.setForeground(TEXT_COLOR);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }


    // Mock methods to get data (replace with actual data fetching logic)
    private int getTotalSales() {
        int totalAmount = 0;
        try (Connection connection = Manager.getConnection()) {
            int maxCount = 0;
            String count = "SELECT count(*) FROM TransactionItems";
            PreparedStatement pStmt = connection.prepareStatement(count);
            ResultSet max = pStmt.executeQuery();

            if (max.next()) {
                maxCount = max.getInt(1);
            }

            String totalQuery = "SELECT * FROM TransactionItems WHERE TransactionID = ?";

            for (int i = 1; i <= maxCount; i++) {
                PreparedStatement tStmt = connection.prepareStatement(totalQuery);
                tStmt.setInt(1, i);
                ResultSet RS = tStmt.executeQuery();

                while (RS.next()) {
                    int price = RS.getInt("Price");
                    int qty = RS.getInt("Quantity");
                    totalAmount += price * qty;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalAmount;
    }

    private int getCustomerCount() {
        int maxCount = 0;
        try (Connection connection = Manager.getConnection()) {
            String count = "SELECT count(*) FROM customer";
            PreparedStatement pStmt = connection.prepareStatement(count);
            ResultSet max = pStmt.executeQuery();

            if (max.next()) {
                maxCount = max.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return maxCount;
    }

    private int getLowStockCount() {
        int maxCount = 0;
        try (Connection connection = Manager.getConnection()) {
            String count = "SELECT count(*) FROM product_inventory";
            PreparedStatement pStmt = connection.prepareStatement(count);
            ResultSet max = pStmt.executeQuery();

            if (max.next()) {
                maxCount = max.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return maxCount;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Home Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HomePanel());
        frame.setVisible(true);
    }
}
