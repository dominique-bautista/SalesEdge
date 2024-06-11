package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
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
        };
        tableModel.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Description", "Category", "Price"});

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM product_inventory";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getString("product_id"),
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
        table.removeColumn(table.getColumnModel().getColumn(4)); // Remove image URL column from view

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
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String productId = (String) model.getValueAt(selectedRow, 0);
                deleteProduct(productId);
                model.removeRow(selectedRow);
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
                                 .noneMatch(field -> field.getText().trim().isEmpty());

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
            pan.revalidate();
            pan.repaint();
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    private void deleteProduct(String productId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "")) {
                String query = "DELETE FROM product_inventory WHERE product_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, productId);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Product with ID " + productId + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
