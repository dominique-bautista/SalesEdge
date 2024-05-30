package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPanel extends JPanel {
    // Define the accent color
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    // Constructor for the InventoryPanel
    public InventoryPanel() {
        // Set the layout for this panel
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Set the background color to white

        // Title Label
        JLabel titleLabel = new JLabel("Inventory Section", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Set font and size
        titleLabel.setForeground(ACCENT_COLOR); // Set text color to the accent color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        add(titleLabel, BorderLayout.NORTH); // Add the title label to the top of the panel

        // Create the table model for inventory items
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Stock Levels");
        tableModel.addColumn("Low Stock Alert");
        tableModel.addColumn("Supplier Information");

        // Add some sample data to the table model
        tableModel.addRow(new Object[]{"Product 1", 50, "No", "Supplier A"});
        tableModel.addRow(new Object[]{"Product 2", 10, "Yes", "Supplier B"});
        tableModel.addRow(new Object[]{"Product 3", 5, "Yes", "Supplier C"});
        tableModel.addRow(new Object[]{"Product 4", 100, "No", "Supplier D"});

        // Create the table using the table model
        JTable table = new JTable(tableModel);
        table.setRowHeight(30); // Set the height of each row

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the panel

        // Create a panel for search and filter options
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchFilterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Search field
        JTextField searchField = new JTextField(20); // Create a text field for search input
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(ACCENT_COLOR); // Set background color to the accent color
        searchButton.setForeground(Color.WHITE); // Set text color to white

        // Filter dropdowns for category, stock level, and supplier
        JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"All Categories", "Clothing", "Electronics"});
        JComboBox<String> stockFilter = new JComboBox<>(new String[]{"All Stock Levels", "Low Stock", "In Stock"});
        JComboBox<String> supplierFilter = new JComboBox<>(new String[]{"All Suppliers", "Supplier A", "Supplier B", "Supplier C", "Supplier D"});

        // Add components to the search/filter panel
        searchFilterPanel.add(new JLabel("Search:"));
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(searchButton);
        searchFilterPanel.add(new JLabel("Category:"));
        searchFilterPanel.add(categoryFilter);
        searchFilterPanel.add(new JLabel("Stock Level:"));
        searchFilterPanel.add(stockFilter);
        searchFilterPanel.add(new JLabel("Supplier:"));
        searchFilterPanel.add(supplierFilter);

        // Add the search/filter panel to the top of the main panel
        add(searchFilterPanel, BorderLayout.NORTH);

        // Create a panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create action buttons for adding, editing, and deleting inventory items
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Set button colors to the accent color and text color to white
        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);

        // Add action buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add the button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to add a new product (to be implemented)
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to edit the selected product (to be implemented)
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to delete the selected product (to be implemented)
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to search products based on searchField input (to be implemented)
            }
        });

        categoryFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to filter products by category (to be implemented)
            }
        });

        stockFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to filter products by stock level (to be implemented)
            }
        });

        supplierFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to filter products by supplier (to be implemented)
            }
        });

        // Add a MouseListener to the table to handle clicks on the supplier column
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = table.columnAtPoint(evt.getPoint());
                int row = table.rowAtPoint(evt.getPoint());
                if (column == 3) { // Check if the clicked column is the Supplier Information column
                    String supplier = tableModel.getValueAt(row, column).toString();
                    showSupplierDetails(supplier);
                }
            }
        });
    }

    // Method to show supplier details
    private void showSupplierDetails(String supplier) {
        // Sample supplier information
        String supplierInfo = switch (supplier) {
            case "Supplier A" -> "Name: Supplier A\nProducts Supplied: Product 1\nContact Information: supplierA@example.com, 123-456-7890\nAddress: 123 Main St, City A, State A, 12345, Country A";
            case "Supplier B" -> "Name: Supplier B\nProducts Supplied: Product 2\nContact Information: supplierB@example.com, 234-567-8901\nAddress: 234 Oak St, City B, State B, 23456, Country B";
            case "Supplier C" -> "Name: Supplier C\nProducts Supplied: Product 3\nContact Information: supplierC@example.com, 345-678-9012\nAddress: 345 Pine St, City C, State C, 34567, Country C";
            case "Supplier D" -> "Name: Supplier D\nProducts Supplied: Product 4\nContact Information: supplierD@example.com, 456-789-0123\nAddress: 456 Elm St, City D, State D, 45678, Country D";
            default -> "Supplier information not available";
        };

        // Display the supplier information in a dialog
        JOptionPane.showMessageDialog(this, supplierInfo, "Supplier Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
