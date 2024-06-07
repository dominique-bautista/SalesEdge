package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Supplier;

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
        String[] columns = { "Product ID", "Product Name", "Description", "Category", "Price" };
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

        tableModel.addRow(new Object[] { "1", "T-Shirt", "Cotton T-shirt", "Clothing", "20.99", "url1", "150" });
        tableModel.addRow(new Object[] { "2", "Laptop", "Gaming laptop", "Electronics", "999.99", "url2", "30" });
        tableModel.addRow(new Object[] { "3", "Book", "Fiction novel", "Books", "12.99", "url3", "200" });
        tableModel.addRow(new Object[] { "4", "Toy", "Action figure", "Toys", "14.99",
                "https://m.media-amazon.com/images/I/61cBV1v26jL._AC_SL1002_.jpg", "75" });

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

                        JOptionPane.showMessageDialog(ProductPanel.this, scrollPane, "Product Details",
                                JOptionPane.INFORMATION_MESSAGE);
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

        addButton.addActionListener(e -> createProduct(this));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            selectedRow++;
            if (selectedRow != 0) {
                updateProduct(this, String.valueOf(selectedRow));
                
            } else {
                JOptionPane.showMessageDialog(null, "Please select a product to edit.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            selectedRow++;
            if (selectedRow != 0) {
                Manager.delete("product_inventory", "product_id", selectedRow);
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

    // private void createProduct(DefaultTableModel tableModel) {
    // JPanel panel = new JPanel(new GridBagLayout());
    // panel.setBackground(Color.WHITE);
    // panel.setPreferredSize(new Dimension(500, 400));
    // GridBagConstraints gbc = new GridBagConstraints();
    // gbc.insets = new Insets(10, 10, 10, 10);
    // gbc.fill = GridBagConstraints.HORIZONTAL;

    // Font font = new Font("Serif", Font.PLAIN, 18);

    // JTextField productIdField = new JTextField(20);
    // JTextField productNameField = new JTextField(20);
    // JTextField descriptionField = new JTextField(20);
    // JTextField categoryField = new JTextField(20);
    // JTextField priceField = new JTextField(20);
    // JTextField supplierField = new JTextField(20);
    // JTextField imageUrlField = new JTextField(20);
    // JTextField stockField = new JTextField(20);

    // productIdField.setFont(font);
    // productNameField.setFont(font);
    // descriptionField.setFont(font);
    // categoryField.setFont(font);
    // priceField.setFont(font);
    // supplierField.setFont(font);
    // imageUrlField.setFont(font);
    // stockField.setFont(font);

    // addFocusListeners(productIdField);
    // addFocusListeners(productNameField);
    // addFocusListeners(descriptionField);
    // addFocusListeners(categoryField);
    // addFocusListeners(priceField);
    // addFocusListeners(supplierField);
    // addFocusListeners(imageUrlField);
    // addFocusListeners(stockField);

    // addFieldToPanel(panel, "Product ID:", productIdField, gbc, 0, font);
    // addFieldToPanel(panel, "Product Name:", productNameField, gbc, 1, font);
    // addFieldToPanel(panel, "Description:", descriptionField, gbc, 2, font);
    // addFieldToPanel(panel, "Category:", categoryField, gbc, 3, font);
    // addFieldToPanel(panel, "Price:", priceField, gbc, 4, font);
    // addFieldToPanel(panel, "Supplier:", supplierField, gbc, 5, font);
    // addFieldToPanel(panel, "Image URL:", imageUrlField, gbc, 6, font);
    // addFieldToPanel(panel, "Stock:", stockField, gbc, 7, font);

    // int option = JOptionPane.showConfirmDialog(this, panel, "Create New Product",
    // JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    // if (option == JOptionPane.OK_OPTION) {
    // tableModel.addRow(new Object[]{
    // productIdField.getText(),
    // productNameField.getText(),
    // descriptionField.getText(),
    // categoryField.getText(),
    // priceField.getText(),
    // imageUrlField.getText(),
    // stockField.getText()
    // });
    // }
    // }

    // private void addFieldToPanel(JPanel panel, String labelText, JTextField
    // textField, GridBagConstraints gbc, int yPos, Font font) {
    // JLabel label = new JLabel(labelText);
    // label.setFont(font);
    // gbc.gridx = 0;
    // gbc.gridy = yPos;
    // panel.add(label, gbc);
    // gbc.gridx = 1;
    // gbc.gridy = yPos;
    // panel.add(textField, gbc);
    // }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Product Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ProductPanel());
        frame.setVisible(true);
    }

    public void createProduct(JComponent pan) {
        JFrame createFrame = new JFrame("Create Product");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(500, 600);
        createFrame.setLocationRelativeTo(null);
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        Font labelFont = new Font("Lato", Font.PLAIN, 20);
        Font textFieldFont = new Font("Lato", Font.PLAIN, 15);
    
        JTextField productName = Manager.createLabeledTextField("Product Name:", panel, labelFont, textFieldFont);
        JTextField desc = Manager.createLabeledTextField("Description:", panel, labelFont, textFieldFont);
        JTextField category = Manager.createLabeledTextField("Category:", panel, labelFont, textFieldFont);
        JTextField price = Manager.createLabeledTextField("Price:", panel, labelFont, textFieldFont);
        JTextField supplier = Manager.createLabeledTextField("Supplier:", panel, labelFont, textFieldFont);
        JTextField imageUrl = Manager.createLabeledTextField("Image URL:", panel, labelFont, textFieldFont);
        JTextField stock = Manager.createLabeledTextField("Stock:", panel, labelFont, textFieldFont);
    
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(0xF47130));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Lato", Font.BOLD, 14));
    
        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = Arrays.stream(new JTextField[]{productName, desc, category, price, supplier, stock, imageUrl})
                                 .allMatch(field ->!field.getText().trim().isEmpty());
    
            if (!allFieldsFilled) {
                JOptionPane.showMessageDialog(createFrame, "All fields must be filled out.");
                return;
            }
    
            try (Connection connection = Manager.getConnection()) {
                String sqlInsert = "INSERT INTO product_inventory (product_name, description, category, price, supplier, stock_level, image_url, low_stock_alert) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                preparedStatement.setString(1, productName.getText());
                preparedStatement.setString(2, desc.getText());
                preparedStatement.setString(3, category.getText());
                preparedStatement.setBigDecimal(4, new BigDecimal(price.getText()));
                preparedStatement.setString(5, supplier.getText());
                preparedStatement.setInt(6, Integer.parseInt(stock.getText()));
                preparedStatement.setString(7, imageUrl.getText());
    
                // Calculate lowStockAlert here, right before it's used
                int lowStockAlert = stock.getText().trim().isEmpty() || Integer.parseInt(stock.getText()) <= 10? 0 : 1;
                preparedStatement.setInt(8, lowStockAlert);
    
                preparedStatement.executeUpdate(); // Execute the statement
                createFrame.dispose();
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + ex.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(null, "Product created successfully.");
            pan.revalidate();
            pan.repaint();
        });
    
        panel.add(saveButton);
    
        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    public void updateProduct(JComponent pan, String cus) {
        JFrame createFrame = new JFrame("Update Product");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(500, 600);
        createFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Lato", Font.PLAIN, 20);
        Font textFieldFont = new Font("Lato", Font.PLAIN, 15);

        JTextField productName = Manager.updateLabeledTextField("Product Name:", panel, labelFont, textFieldFont,"product_inventory", "product_name", cus,"product_id");
        JTextField desc = Manager.updateLabeledTextField("Description:", panel, labelFont, textFieldFont,"product_inventory", "description", cus,"product_id");
        JTextField category = Manager.updateLabeledTextField("Category:", panel, labelFont, textFieldFont,"product_inventory", "category", cus,"product_id");
        JTextField price = Manager.updateLabeledTextField("Price:", panel, labelFont, textFieldFont,"product_inventory", "price", cus,"product_id");
        JTextField supplier = Manager.updateLabeledTextField("Supplier:", panel, labelFont, textFieldFont,"product_inventory", "supplier", cus,"product_id");
        JTextField imageUrl = Manager.updateLabeledTextField("Image URL:", panel, labelFont, textFieldFont,"product_inventory", "image_url", cus,"product_id");
        JTextField stock = Manager.updateLabeledTextField("Stock:", panel, labelFont, textFieldFont,"product_inventory", "stock_level", cus,"product_id");
    
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(0xF47130));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Lato", Font.BOLD, 14));

        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = true;
            JTextField[] textFields = { productName, desc, category,price,supplier, imageUrl , stock};
            for (JTextField textField : textFields) {
                if (textField.getText().trim().isEmpty()) {
                    allFieldsFilled = false;
                    break;
                }
            }
            if (!allFieldsFilled) {
                JOptionPane.showMessageDialog(createFrame, "All fields must be filled out.");
                return;
            }
            try (Connection connection = Manager.getConnection()) {
                String sqlInsert = "UPDATE `product_inventory` SET `product_name` =?, `description` =?, `category` =?, `price` =?, `supplier` =?, `stock_level` =?, `image_url` =?, `low_stock_alert` =? WHERE `product_id` =?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                preparedStatement.setString(1, productName.getText());
                preparedStatement.setString(2, desc.getText());
                preparedStatement.setString(3, category.getText());
                preparedStatement.setBigDecimal(4, new BigDecimal(price.getText()));
                preparedStatement.setString(5, supplier.getText());
                preparedStatement.setInt(6, Integer.parseInt(stock.getText()));
                preparedStatement.setString(7, imageUrl.getText());
    
                // Calculate lowStockAlert here, right before it's used
                int lowStockAlert = stock.getText().trim().isEmpty() || Integer.parseInt(stock.getText()) <= 10? 0 : 1;
                preparedStatement.setInt(8, lowStockAlert);
    
                preparedStatement.setString(9, cus); // product_id
                preparedStatement.executeUpdate(); // Execute the statement
                createFrame.dispose();

            } catch (SQLException c) {
                c.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + c.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(null, "Product Updated successfully.");
            pan.revalidate(); // Revalidate the
            pan.repaint();
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }
}
