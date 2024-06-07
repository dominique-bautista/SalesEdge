package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ProductPanel extends JPanel {
    private static final Color ACCENT_COLOR = new Color(0xF47130);
    private static final Color DEFAULT_BORDER_COLOR = Color.GRAY;

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Product Information", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(20);
        String[] columns = {"Product ID", "Product Name", "Description", "Category", "Price"};
        JComboBox<String> columnSelector = new JComboBox<>(columns);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel(" in "));
        searchPanel.add(columnSelector);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("Image URL");
        tableModel.addColumn("Stock");

        tableModel.addRow(new Object[]{"1", "T-Shirt", "Cotton T-shirt", "Clothing", "20.99", "url1", "150"});
        tableModel.addRow(new Object[]{"2", "Laptop", "Gaming laptop", "Electronics", "999.99", "url2", "30"});
        tableModel.addRow(new Object[]{"3", "Book", "Fiction novel", "Books", "12.99", "url3", "200"});
        tableModel.addRow(new Object[]{"4", "Toy", "Action figure", "Toys", "14.99", "https://m.media-amazon.com/images/I/61cBV1v26jL._AC_SL1002_.jpg", "75"});

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(ACCENT_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);

        table.removeColumn(table.getColumnModel().getColumn(5));
        table.removeColumn(table.getColumnModel().getColumn(5));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        searchField.addActionListener(e -> {
            String text = searchField.getText();
            int columnIndex = columnSelector.getSelectedIndex();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String productId = table.getModel().getValueAt(row, 0).toString();
                        String productName = table.getModel().getValueAt(row, 1).toString();
                        String description = table.getModel().getValueAt(row, 2).toString();
                        String category = table.getModel().getValueAt(row, 3).toString();
                        String price = table.getModel().getValueAt(row, 4).toString();
                        String imageUrl = table.getModel().getValueAt(row, 5).toString();
                        String stock = table.getModel().getValueAt(row, 6).toString();
                        String supplier = "Default Supplier";

                        JPanel productDetailsPanel = new JPanel();
                        productDetailsPanel.setLayout(new BoxLayout(productDetailsPanel, BoxLayout.Y_AXIS));
                        productDetailsPanel.add(new JLabel("Product ID: " + productId));
                        productDetailsPanel.add(new JLabel("Product Name: " + productName));
                        productDetailsPanel.add(new JLabel("Description: " + description));
                        productDetailsPanel.add(new JLabel("Category: " + category));
                        productDetailsPanel.add(new JLabel("Price: " + price));
                        productDetailsPanel.add(new JLabel("Supplier: " + supplier));
                        productDetailsPanel.add(Box.createVerticalStrut(10));

                        try {
                            ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                            Image image = imageIcon.getImage();
                            Image scaledImage = image.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                            imageIcon = new ImageIcon(scaledImage);
                            JLabel imageLabel = new JLabel(imageIcon);
                            imageLabel.setPreferredSize(new Dimension(400, 400));
                            productDetailsPanel.add(imageLabel);
                        } catch (Exception ex) {
                            JLabel noImageLabel = new JLabel("No image available");
                            noImageLabel.setPreferredSize(new Dimension(400, 400));
                            noImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            noImageLabel.setVerticalAlignment(SwingConstants.CENTER);
                            productDetailsPanel.add(noImageLabel);
                        }

                        productDetailsPanel.add(new JLabel("Stock: " + stock));

                        JScrollPane scrollPane = new JScrollPane(productDetailsPanel);
                        scrollPane.setPreferredSize(new Dimension(450, 600));

                        JOptionPane.showMessageDialog(ProductPanel.this, scrollPane, "Product Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });




        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> createProduct(tableModel));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                String productId = tableModel.getValueAt(modelRow, 0).toString();
                String productName = tableModel.getValueAt(modelRow, 1).toString();
                String description = tableModel.getValueAt(modelRow, 2).toString();
                String category = tableModel.getValueAt(modelRow, 3).toString();
                String price = tableModel.getValueAt(modelRow, 4).toString();
                String imageUrl = tableModel.getValueAt(modelRow, 5).toString();
                String stock = tableModel.getValueAt(modelRow, 6).toString();

                JTextField productIdField = new JTextField(productId);
                JTextField productNameField = new JTextField(productName);
                JTextField descriptionField = new JTextField(description);
                JTextField categoryField = new JTextField(category);
                JTextField priceField = new JTextField(price);
                JTextField imageUrlField = new JTextField(imageUrl);
                JTextField stockField = new JTextField(stock);

                addFocusListeners(productIdField);
                addFocusListeners(productNameField);
                addFocusListeners(descriptionField);
                addFocusListeners(categoryField);
                addFocusListeners(priceField);
                addFocusListeners(imageUrlField);
                addFocusListeners(stockField);

                Object[] message = {
                        "Product ID:", productIdField,
                        "Product Name:", productNameField,
                        "Description:", descriptionField,
                        "Category:", categoryField,
                        "Price:", priceField,
                        "Image URL:", imageUrlField,
                        "Stock:", stockField,
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt(productIdField.getText(), modelRow, 0);
                    tableModel.setValueAt(productNameField.getText(), modelRow, 1);
                    tableModel.setValueAt(descriptionField.getText(), modelRow, 2);
                    tableModel.setValueAt(categoryField.getText(), modelRow, 3);
                    tableModel.setValueAt(priceField.getText(), modelRow, 4);
                    tableModel.setValueAt(imageUrlField.getText(), modelRow, 5);
                    tableModel.setValueAt(stockField.getText(), modelRow, 6);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a product to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                tableModel.removeRow(modelRow);
            }
        });
    }

    private void addFocusListeners(JTextField textField) {
        textField.setBorder(BorderFactory.createLineBorder(DEFAULT_BORDER_COLOR, 1));
        textField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(DEFAULT_BORDER_COLOR, 1));
            }
        });
    }

    private void createProduct(DefaultTableModel tableModel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(500, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Serif", Font.PLAIN, 18);

        JTextField productIdField = new JTextField(20);
        JTextField productNameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField supplierField = new JTextField(20);
        JTextField imageUrlField = new JTextField(20);
        JTextField stockField = new JTextField(20);

        productIdField.setFont(font);
        productNameField.setFont(font);
        descriptionField.setFont(font);
        categoryField.setFont(font);
        priceField.setFont(font);
        supplierField.setFont(font);
        imageUrlField.setFont(font);
        stockField.setFont(font);

        addFocusListeners(productIdField);
        addFocusListeners(productNameField);
        addFocusListeners(descriptionField);
        addFocusListeners(categoryField);
        addFocusListeners(priceField);
        addFocusListeners(supplierField);
        addFocusListeners(imageUrlField);
        addFocusListeners(stockField);

        addFieldToPanel(panel, "Product ID:", productIdField, gbc, 0, font);
        addFieldToPanel(panel, "Product Name:", productNameField, gbc, 1, font);
        addFieldToPanel(panel, "Description:", descriptionField, gbc, 2, font);
        addFieldToPanel(panel, "Category:", categoryField, gbc, 3, font);
        addFieldToPanel(panel, "Price:", priceField, gbc, 4, font);
        addFieldToPanel(panel, "Supplier:", supplierField, gbc, 5, font);
        addFieldToPanel(panel, "Image URL:", imageUrlField, gbc, 6, font);
        addFieldToPanel(panel, "Stock:", stockField, gbc, 7, font);

        int option = JOptionPane.showConfirmDialog(this, panel, "Create New Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            tableModel.addRow(new Object[]{
                    productIdField.getText(),
                    productNameField.getText(),
                    descriptionField.getText(),
                    categoryField.getText(),
                    priceField.getText(),
                    imageUrlField.getText(),
                    stockField.getText()
            });
        }
    }

    private void addFieldToPanel(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc, int yPos, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        panel.add(textField, gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Product Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ProductPanel());
        frame.setVisible(true);
    }
}
