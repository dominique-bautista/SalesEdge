package com.example.panels;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

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
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR)); // Add a bottom border

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
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class; // Assuming "Stock Levels" is at index 1
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable all cells from being editable
            }
        };
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Stocks");
        tableModel.addColumn("Low Stock");
        tableModel.addColumn("Supplier Name");

        // Populate a table model with data from the database
        try (Connection connection = getConnection()) {
            String query = "SELECT product_name, stock_level, supplier FROM product_inventory";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String productName = resultSet.getString("product_name");
                        int stockLevel = resultSet.getInt("stock_level");
                        String supplier = resultSet.getString("supplier");
                        String lowStockAlert = stockLevel <= 10 ? "Yes" : "No"; // Check condition for low stock
                        tableModel.addRow(new Object[]{productName, stockLevel, lowStockAlert, supplier});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }

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

        // Customize alignment for "Stocks" column
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT); // Set left alignment
        table.getColumnModel().getColumn(1).setCellRenderer(renderer); // Assuming "Stocks" is at index 1

        // Disable row selection
        table.setRowSelectionAllowed(false);

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Customize the scroll bar
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0xF47130); // Set the color of the scrollbar thumb
            }
        });

        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the panel

        // Create a row sorter for the table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Add action listener to the search field
        searchField.addActionListener(e -> {
            String text = searchField.getText();
            int columnIndex = columnSelector.getSelectedIndex();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
            }
        });
    }

    // Method to establish a database connection
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/salesedge";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }
}
