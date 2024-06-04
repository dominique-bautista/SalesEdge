package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductPanel extends JPanel {
    // Define the accent color
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    // Constructor for the ProductPanel
    public ProductPanel() {
        // Set the layout for this panel
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE); // Set the background color to white
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel

        // Create a panel for the title and search components
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Product Information", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Set font and size
        titleLabel.setForeground(ACCENT_COLOR); // Set text color to the accent color
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR)); // Add bottom border

        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        // Create the search field and column selector
        JTextField searchField = new JTextField(20);
        String[] columns = {"Product ID", "Product Name", "Description", "Category", "Price", "Stock Level", "Image URL", "Brand", "Color", "Size"};
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

        // Create the table model for product information
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("Stock Level");
        tableModel.addColumn("Image URL");
        tableModel.addColumn("Brand");
        tableModel.addColumn("Color");
        tableModel.addColumn("Size");

        // Add some sample data to the table model
        tableModel.addRow(new Object[]{"1", "T-Shirt", "Cotton T-shirt", "Clothing", "20.99", "150", "url1", "BrandA", "Red", "M"});
        tableModel.addRow(new Object[]{"2", "Laptop", "Gaming laptop", "Electronics", "999.99", "30", "url2", "BrandB", "Black", "15 inch"});
        tableModel.addRow(new Object[]{"3", "Book", "Fiction novel", "Books", "12.99", "200", "url3", "BrandC", "N/A", "N/A"});
        tableModel.addRow(new Object[]{"4", "Toy", "Action figure", "Toys", "14.99", "75", "url4", "BrandD", "Multicolor", "Standard"});

        // Create the table using the table model
        JTable table = new JTable(tableModel);
        table.setRowHeight(30); // Set the height of each row
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(ACCENT_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2)); // Add border to the scroll pane
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
    }
}
