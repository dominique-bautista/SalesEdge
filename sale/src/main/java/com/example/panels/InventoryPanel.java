package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPanel extends JPanel {
    // Define the accent color
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    // Constructor for the InventoryPanel
    public InventoryPanel() {
        // Set the layout for this panel
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE); // Set the background color to white
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel

        // Create a panel for the title and search components
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Inventory Information", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24)); // Set font and size
        titleLabel.setForeground(ACCENT_COLOR); // Set text color to the accent color
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR)); // Add bottom border

        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        // Create the search field and column selector
        JTextField searchField = new JTextField(20);
        String[] columns = {"Product Name", "Stock Levels", "Low Stock Alert", "Supplier Information"};
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
        table.setFont(new Font("Lato", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(ACCENT_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2)); // Add border
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the panel

        // Create a row sorter for the table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

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
