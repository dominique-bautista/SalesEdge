package com.example.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesPanel extends JPanel {
    // Define the accent color
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    // Constructor for the SalesPanel
    public SalesPanel() {
        // Set the layout for this panel
        setLayout(new BorderLayout());
        setBackground(Color.lightGray); // Set the background color to white

        // Title Label
        JLabel titleLabel = new JLabel("Sales Transactions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Set font and size
        titleLabel.setForeground(ACCENT_COLOR); // Set text color to the accent color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        add(titleLabel, BorderLayout.NORTH); // Add the title label to the top of the panel

        // Create the table model for sales transactions
        DefaultTableModel transactionTableModel = new DefaultTableModel();
        transactionTableModel.addColumn("Transaction ID");
        transactionTableModel.addColumn("Customer ID");
        transactionTableModel.addColumn("Date");
        transactionTableModel.addColumn("Time");
        transactionTableModel.addColumn("Salesperson ID");
        transactionTableModel.addColumn("List of Purchased Items");
        transactionTableModel.addColumn("Total Amount");

        // Add some sample data to the table model
        transactionTableModel.addRow(new Object[]{"T001", "C001", "2023-05-01", "10:30 AM", "S001", "P001, 2, $300.00", "$600.00"});
        transactionTableModel.addRow(new Object[]{"T002", "C002", "2023-05-02", "11:00 AM", "S002", "P002, 1, $45.00", "$45.00"});
        transactionTableModel.addRow(new Object[]{"T003", "C003", "2023-05-03", "01:45 PM", "S003", "P003, 5, $30.00", "$150.00"});

        // Create the table using the transaction table model
        JTable transactionTable = new JTable(transactionTableModel);
        transactionTable.setRowHeight(30); // Set the height of each row

        // Create the table model for sales reports
        DefaultTableModel reportTableModel = new DefaultTableModel();
        reportTableModel.addColumn("Product ID");
        reportTableModel.addColumn("Category");
        reportTableModel.addColumn("Salesperson");
        reportTableModel.addColumn("Date");
        reportTableModel.addColumn("Quantity");
        reportTableModel.addColumn("Price");

        // Add some sample data to the table model
        reportTableModel.addRow(new Object[]{"P001", "Electronics", "John", "2023-05-01", 5, 300.00});
        reportTableModel.addRow(new Object[]{"P002", "Books", "Mary", "2023-05-02", 3, 45.00});
        reportTableModel.addRow(new Object[]{"P003", "Clothing", "Alice", "2023-05-03", 10, 150.00});

        // Create the table using the report table model
        JTable reportTable = new JTable(reportTableModel);
        reportTable.setRowHeight(30); // Set the height of each row

        // Create scroll panes for both tables
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        JScrollPane reportScrollPane = new JScrollPane(reportTable);

        // Initially display the transaction table
        add(transactionScrollPane, BorderLayout.CENTER);

        // Create a panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create action buttons for toggling views, refreshing, and exporting sales reports
        JButton toggleButton = new JButton("View Sales Reports");
        JButton refreshButton = new JButton("Refresh");
        JButton exportButton = new JButton("Export");

        // Set button colors to the accent color and text color to white
        toggleButton.setBackground(ACCENT_COLOR);
        toggleButton.setForeground(Color.WHITE);
        refreshButton.setBackground(ACCENT_COLOR);
        refreshButton.setForeground(Color.WHITE);
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);

        // Add action buttons to the button panel
        buttonPanel.add(toggleButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);

        // Add the button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners for buttons
        toggleButton.addActionListener(new ActionListener() {
            private boolean showingTransactions = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showingTransactions) {
                    remove(transactionScrollPane);
                    add(reportScrollPane, BorderLayout.CENTER);
                    toggleButton.setText("View Sales Transactions");
                } else {
                    remove(reportScrollPane);
                    add(transactionScrollPane, BorderLayout.CENTER);
                    toggleButton.setText("View Sales Reports");
                }
                showingTransactions = !showingTransactions;
                revalidate();
                repaint();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to refresh sales reports (to be implemented)
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to export sales reports (to be implemented)
            }
        });
    }
}
