package com.example.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesPanel extends JPanel {
    // Define the accent color
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    // Constructor for the SalesPanel
    public SalesPanel() {
        // Set the layout for this panel
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE); // Set the background color to white
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel

        // Create a panel for the title and search components
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Sales Transactions", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Set font and size
        titleLabel.setForeground(ACCENT_COLOR); // Set text color to the accent color
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR)); // Add bottom border

        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        // Create the search field and column selector
        JTextField searchField = new JTextField(20);
        String[] columns = {"Transaction ID", "Customer ID", "Date", "Time", "Salesperson ID", "List of Purchased Items", "Total Amount"};
        JComboBox<String> columnSelector = new JComboBox<>(columns);

        // Add the search field and column selector to the search panel
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel(" in "));
        searchPanel.add(columnSelector);

        // Add the title label and search panel to the top panel
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Add the top panel to the top of the main panel
        add(topPanel, BorderLayout.NORTH);

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
        transactionTable.setFont(new Font("Serif", Font.PLAIN, 16));
        transactionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        transactionTable.getTableHeader().setBackground(ACCENT_COLOR);
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setGridColor(ACCENT_COLOR);
        transactionTable.setShowGrid(true);
        transactionTable.setSelectionBackground(ACCENT_COLOR);
        transactionTable.setSelectionForeground(Color.WHITE);

        // Create a scroll pane for the table
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        transactionScrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));

        // Display the transaction table
        add(transactionScrollPane, BorderLayout.CENTER);

        // Create a row sorter for the table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(sorter);

        // Add action listener to the search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchField.getText();
                int columnIndex = columnSelector.getSelectedIndex();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
                }
            }
        });

        // Create a panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Add padding at the top

        // Create action buttons for refreshing and exporting sales reports
        JButton refreshButton = new JButton("Refresh");
        JButton exportButton = new JButton("Export");

        // Set button colors to the accent color and text color to white
        refreshButton.setBackground(ACCENT_COLOR);
        refreshButton.setForeground(Color.WHITE);
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);

        // Add action buttons to the button panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);

        // Add the button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners for buttons
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
