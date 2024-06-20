package com.example.panels;

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;

public class ProductPanel extends JPanel {
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a top panel with search functionality
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Create a table and populate it with data from the database
        JTable table = createTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Create button panel for Add, Edit, and Delete operations
        JPanel buttonPanel = createButtonPanel(table);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Product Information", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(20);
        JComboBox<String> columnSelector = new JComboBox<>(new String[]{"Product ID", "Product Name", "Description", "Category", "Price"});

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel(" in "));
        searchPanel.add(columnSelector);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JTable createTable() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { // Price column
                    return BigDecimal.class;
                } else if (columnIndex == 0) { // Product ID column
                    return Integer.class; // Assuming Product ID is numeric
                } else {
                    return String.class;
                }
            }
        };

        tableModel.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Description", "Category", "Price"});

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM product_inventory";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("product_id"), // Convert to Integer
                            resultSet.getString("product_name"),
                            resultSet.getString("description"),
                            resultSet.getString("category"),
                            resultSet.getBigDecimal("price")
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Lato", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(ACCENT_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Custom cell renderer to align numeric columns to the right
        TableCellRenderer rightAlignRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(column == 0 || column == 4 ? SwingConstants.RIGHT : SwingConstants.LEFT);
                return cellComponent;
            }
        };

        // Custom cell renderer to align numeric columns to the left
        TableCellRenderer leftAlignRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(column == 0 || column == 4 ? SwingConstants.LEFT : SwingConstants.LEFT);
                return cellComponent;
            }
        };

        // Apply the custom renderer to the numeric columns
        table.getColumnModel().getColumn(0).setCellRenderer(leftAlignRenderer); // Product ID column
        table.getColumnModel().getColumn(4).setCellRenderer(rightAlignRenderer); // Price column

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JTextField searchField = (JTextField) ((JPanel) ((JPanel) this.getComponent(0)).getComponent(1)).getComponent(1);
        JComboBox<String> columnSelector = (JComboBox<String>) ((JPanel) ((JPanel) this.getComponent(0)).getComponent(1)).getComponent(3);
        searchField.addActionListener(e -> {
            String text = searchField.getText();
            int columnIndex = columnSelector.getSelectedIndex();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
            }
        });

        return table;
    }

    private JPanel createButtonPanel(JTable table) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = new JButton("Add");
        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);

        JButton editButton = new JButton("Edit");
        editButton.setBackground(ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> createProduct(this));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String productId = (String) table.getValueAt(selectedRow, 0);
                updateProduct(this, productId);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a product to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this product?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String productId = (String) model.getValueAt(selectedRow, 0);
                    deleteProduct(productId);
                    model.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        return buttonPanel;
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

        JTextField productName = createLabeledTextField("Product Name:", panel, labelFont, textFieldFont);
        JTextField desc = createLabeledTextField("Description:", panel, labelFont, textFieldFont);
        JTextField category = createLabeledTextField("Category:", panel, labelFont, textFieldFont);
        JTextField price = createLabeledTextField("Price:", panel, labelFont, textFieldFont);
        JTextField supplier = createLabeledTextField("Supplier:", panel, labelFont, textFieldFont);
        JTextField stock = createLabeledTextField("Stock:", panel, labelFont, textFieldFont);

//        JLabel imageLabel = new JLabel();
//        imageLabel.setPreferredSize(new Dimension(240, 240));
//        imageLabel.setMaximumSize(new Dimension(240, 240));
//        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        panel.add(new JLabel("Image Preview:"));
//        panel.add(imageLabel);
//
//        JButton uploadButton = new JButton("Upload Image");
//        uploadButton.setBackground(new Color(0xF47130));
//        uploadButton.setForeground(Color.WHITE);
//        panel.add(new JLabel());
//        panel.add(uploadButton);
//
//        uploadButton.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            int result = fileChooser.showOpenDialog(createFrame);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                try {
//                    BufferedImage img = ImageIO.read(selectedFile);
//                    Image scaledImg = img.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
//                    ImageIcon icon = new ImageIcon(scaledImg);
//                    imageLabel.setIcon(icon);
//                    imageLabel.setText(selectedFile.getAbsolutePath());
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(createFrame, "Error loading image: " + ex.getMessage());
//                }
//            }
//        });

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(ACCENT_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Lato", Font.BOLD, 14));

        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = Arrays.stream(new JTextField[]{productName, desc, category, price, supplier, stock})
                    .noneMatch(field -> field.getText().trim().isEmpty());

//            if (!allFieldsFilled || imageLabel.getIcon() == null) {
//                JOptionPane.showMessageDialog(createFrame, "All fields and image must be filled out.");
//                return;
//            }

            try (Connection connection = getConnection()) {
                String sqlInsert = "INSERT INTO product_inventory (product_name, description, category, price, supplier, stock_level, image_url, low_stock_alert) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                preparedStatement.setString(1, productName.getText());
                preparedStatement.setString(2, desc.getText());
                preparedStatement.setString(3, category.getText());
                preparedStatement.setBigDecimal(4, new BigDecimal(price.getText()));
                preparedStatement.setString(5, supplier.getText());
                preparedStatement.setInt(6, Integer.parseInt(stock.getText()));
//                preparedStatement.setString(7, imageLabel.getText());

                int lowStockAlert = Integer.parseInt(stock.getText()) <= 10 ? 0 : 1;
                preparedStatement.setInt(8, lowStockAlert);

                preparedStatement.executeUpdate();
                createFrame.dispose();

            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + ex.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(null, "Product created successfully.");
            pan.revalidate();
            pan.repaint();
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    public void updateProduct(JComponent pan, String productId) {
        JFrame createFrame = new JFrame("Update Product");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(500, 600);
        createFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Lato", Font.PLAIN, 20);
        Font textFieldFont = new Font("Lato", Font.PLAIN, 15);

        JTextField productName = updateLabeledTextField("Product Name:", panel, labelFont, textFieldFont, "product_name", productId);
        JTextField desc = updateLabeledTextField("Description:", panel, labelFont, textFieldFont, "description", productId);
        JTextField category = updateLabeledTextField("Category:", panel, labelFont, textFieldFont, "category", productId);
        JTextField price = updateLabeledTextField("Price:", panel, labelFont, textFieldFont, "price", productId);
        JTextField supplier = updateLabeledTextField("Supplier:", panel, labelFont, textFieldFont, "supplier", productId);
        JTextField imageUrl = updateLabeledTextField("Image URL:", panel, labelFont, textFieldFont, "image_url", productId);
        JTextField stock = updateLabeledTextField("Stock:", panel, labelFont, textFieldFont, "stock_level", productId);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(ACCENT_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Lato", Font.BOLD, 14));

        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = true;
            JTextField[] textFields = {productName, desc, category, price, supplier, imageUrl, stock};
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
            try (Connection connection = getConnection()) {
                String sqlUpdate = "UPDATE product_inventory SET product_name = ?, description = ?, category = ?, price = ?, supplier = ?, stock_level = ?, low_stock_alert = ? WHERE product_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
                preparedStatement.setString(1, productName.getText());
                preparedStatement.setString(2, desc.getText());
                preparedStatement.setString(3, category.getText());
                preparedStatement.setBigDecimal(4, new BigDecimal(price.getText()));
                preparedStatement.setString(5, supplier.getText());
                preparedStatement.setInt(6, Integer.parseInt(stock.getText()));
//                preparedStatement.setString(7, imageUrl.getText());

                int lowStockAlert = Integer.parseInt(stock.getText()) <= 10 ? 0 : 1;
                preparedStatement.setInt(8, lowStockAlert);

//                preparedStatement.setString(9, productId);
                preparedStatement.executeUpdate();
                createFrame.dispose();

            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + ex.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(null, "Product updated successfully.");
            pan.revalidate();
            pan.repaint();
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    private void deleteProduct(String productId) {
        try (Connection connection = getConnection()) {
            String sqlDelete = "DELETE FROM product_inventory WHERE product_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
                preparedStatement.setString(1, productId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JTextField createLabeledTextField(String labelText, JPanel panel, Font labelFont, Font textFieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        JTextField textField = new JTextField();
        textField.setFont(textFieldFont);
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    private static JTextField updateLabeledTextField(String labelText, JPanel panel, Font labelFont, Font textFieldFont, String columnName, String keyValue) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        JTextField textField = new JTextField();
        textField.setFont(textFieldFont);

        try (Connection connection = getConnection()) {
            String query = String.format("SELECT %s FROM %s WHERE %s = ?", columnName, "product_inventory", "product_id");
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, keyValue);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        textField.setText(resultSet.getString(columnName));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(label);
        panel.add(textField);
        return textField;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Product Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new ProductPanel());
            frame.setVisible(true);
        });
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/salesedge";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }
}
