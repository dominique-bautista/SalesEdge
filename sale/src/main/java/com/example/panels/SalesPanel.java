package com.example.panels;

import com.example.DashBoard;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SalesPanel extends JPanel {
    private static final Color ACCENT_COLOR = new Color(0xF47130);
    private static final AtomicInteger transactionIdCounter = new AtomicInteger(1);
    private static final String currentSalespersonId = "S001";
    private static final Map<String, String[][]> transactionProductDetails = new HashMap<>();

    String productView;
    String total;
    public SalesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Sales Transactions", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(20);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        });

        String[] columns = { "Transaction ID", "Customer ID", "Date", "Time", "Salesperson ID", "Product",
                "Total Amount" };
        JComboBox<String> columnSelector = new JComboBox<>(columns);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel(" in "));
        searchPanel.add(columnSelector);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        DefaultTableModel transactionTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells not editable
                return false;
            }
        };


        try (Connection connection = Manager.getConnection()) {
            String staffName = "";
            String query = "SELECT * FROM Transactions";
            String productQuery = "SELECT * FROM TransactionItems WHERE TransactionID = ?";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    PreparedStatement productStmt = connection.prepareStatement(productQuery);
                    productStmt.setString(1, resultSet.getString("TransactionID"));
                    ResultSet Rs = productStmt.executeQuery();

                    int totalAmount = 0;
                    while (Rs.next()) {
                        int price = Rs.getInt("Price");
                        int qty = Rs.getInt("Quantity");
                        totalAmount += price * qty;
                    }
                    PreparedStatement staff = connection.prepareStatement("SELECT first_name from staff where staff_id = ?");
                    staff.setInt(1,resultSet.getInt("SalespersonID"));
                    ResultSet staffRs = staff.executeQuery();

                    if(staffRs.next()){
                        staffName = staffRs.getString(1);
                    }
                    PreparedStatement cus = connection.prepareStatement(" SELECT first_name from customer where customer_id = ?");
                    cus.setInt(1,resultSet.getInt("CustomerID"));
                    ResultSet cusRs = cus.executeQuery();
                    String cusName = "";
                    if(cusRs.next())
                    {
                        cusName = cusRs.getString(1);
                    }


                    Object[] rowData = {
                            resultSet.getString("TransactionID"),
//                            resultSet.getString("CustomerID"),
                            cusName,
                            resultSet.getString("Date"),
                            resultSet.getString("Time"),
//                            resultSet.getInt("SalespersonID"),
                            staffName,
                            productView = "See products",
                            totalAmount,
                    };
                    transactionTableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable transactionTable = new JTable(transactionTableModel);
        transactionTable.setRowHeight(30);
        transactionTable.setFont(new Font("Lato", Font.PLAIN, 16));
        transactionTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        transactionTable.getTableHeader().setBackground(ACCENT_COLOR);
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setGridColor(ACCENT_COLOR);
        transactionTable.setShowGrid(true);
        transactionTable.setSelectionBackground(ACCENT_COLOR);
        transactionTable.setSelectionForeground(Color.WHITE);

        transactionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = transactionTable.columnAtPoint(e.getPoint());
                if (column == 5) {
                    int row = transactionTable.rowAtPoint(e.getPoint());
                    String transactionId = (String) transactionTable.getValueAt(row, 0);
                    showProductDetails(transactionId);
                }
            }
        });

        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        transactionScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0xF47130); // Set the color of the scrollbar thumb
            }
        });

        add(transactionScrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(sorter);

        searchField.addActionListener(e -> {
            String text = searchField.getText();
            int columnIndex = columnSelector.getSelectedIndex();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton exportButton = new JButton("Export");

        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);

        add(buttonPanel, BorderLayout.SOUTH);

        exportButton.addActionListener(e -> {
            // Code to export sales reports (to be implemented)
        });

        addButton.addActionListener(e -> createSales(transactionTableModel));

        deleteButton.addActionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow != -1) {
                String transactionId = (String) transactionTable.getValueAt(selectedRow, 0);
                transactionTableModel.removeRow(selectedRow);
                transactionProductDetails.remove(transactionId);
            }
        });
    }

    private void createSales(DefaultTableModel tableModel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(500, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Serif", Font.PLAIN, 18);

        JTextField customerIdField = createHighlightedTextField(font);
        JTextField dateField = createHighlightedTextField(font);
        JTextField timeField = createHighlightedTextField(font);
        JTextField productIdField = createHighlightedTextField(font);
        JTextField quantityField = createHighlightedTextField(font);

        addFieldToPanel(panel, "Customer ID:", customerIdField, gbc, 0, font);
        addFieldToPanel(panel, "Date:", dateField, gbc, 1, font);
        addFieldToPanel(panel, "Time:", timeField, gbc, 2, font);

        JButton cartButton = new JButton("Cart");
        cartButton.setBackground(ACCENT_COLOR);
        cartButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(cartButton, gbc);

        AtomicInteger totalAmount = new AtomicInteger(0);
        String[][] productDetails = new String[10][4]; // temporary storage for products
        Map<String, String> productsQuantities = new HashMap<>();
        double totAmount = 0;
        cartButton.addActionListener(e -> {
            JPanel cartPanel = new JPanel(new BorderLayout(10, 10));
            DefaultTableModel cartTableModel = new DefaultTableModel(
                    new String[] { "Product ID", "Product Name", "Quantity", "Price" }, 0);
            JTable cartTable = new JTable(cartTableModel);
            cartTable.setRowHeight(30);
            cartTable.setFont(new Font("Lato", Font.PLAIN, 16));
            cartTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
            cartTable.getTableHeader().setBackground(ACCENT_COLOR);
            cartTable.getTableHeader().setForeground(Color.WHITE);
            cartTable.setFillsViewportHeight(true);
            cartTable.setGridColor(ACCENT_COLOR);
            cartTable.setShowGrid(true);
            cartTable.setSelectionBackground(ACCENT_COLOR);
            cartTable.setSelectionForeground(Color.WHITE);

            JScrollPane cartScrollPane = new JScrollPane(cartTable);
            cartScrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
            cartPanel.add(cartScrollPane, BorderLayout.CENTER);

            JButton addProductButton = new JButton("Add Product");
            addProductButton.setBackground(ACCENT_COLOR);
            addProductButton.setForeground(Color.WHITE);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            buttonPanel.add(addProductButton);
            cartPanel.add(buttonPanel, BorderLayout.SOUTH);
            addProductButton.addActionListener(ev -> {
                JPanel productPanel = new JPanel(new GridBagLayout());
                productPanel.setBackground(Color.WHITE);
                GridBagConstraints productGbc = new GridBagConstraints();
                productGbc.insets = new Insets(10, 10, 10, 10);
                productGbc.fill = GridBagConstraints.HORIZONTAL;

                addFieldToPanel(productPanel, "Product ID:", productIdField, productGbc, 0, font);
                addFieldToPanel(productPanel, "Quantity:", quantityField, productGbc, 1, font);

                int option = JOptionPane.showConfirmDialog(this, productPanel, "Add Product",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    String productId = productIdField.getText();
                    String quantityStr = quantityField.getText();
                    int quantity = Integer.parseInt(quantityStr);
                    String productName = "";
                    int price = 0;
                    int stock = 0;
                    try (Connection conn = Manager.getConnection()) {
                        String sql = "SELECT * FROM product_inventory WHERE product_id =?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, productId);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            productName = rs.getString("product_name");
                            price = rs.getInt("price");
                            stock = rs.getInt("stock_level");
                            if(stock < quantity)
                            {
                                JOptionPane.showMessageDialog(this, "Not enough stock", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }else {
                                totalAmount.addAndGet((int)(price * quantity));
                                String tot = "₱" + price; // Correctly concatenates dollar sign with total
                                cartTableModel.addRow(new Object[]{productId, productName, quantityStr, tot});
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid Product ID", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                }

            });

            int option = JOptionPane.showConfirmDialog(this, cartPanel, "Cart", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                int rowCount = cartTableModel.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    productDetails[i][0] = (String) cartTableModel.getValueAt(i, 0);
                    productDetails[i][1] = (String) cartTableModel.getValueAt(i, 1);
                    productDetails[i][2] = (String) cartTableModel.getValueAt(i, 2);
                    productDetails[i][3] = (String) cartTableModel.getValueAt(i, 3);
                    productsQuantities.put(productDetails[i][0], productDetails[i][2]);

                }
            }
        });

        int option = JOptionPane.showConfirmDialog(this, panel, "Create New Sales Transaction",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            insertTransaction(customerIdField.getText(), dateField.getText(), timeField.getText(), DashBoard.getCurrentUserId(),
                    productsQuantities); 

//            String transactionId = String.format("T%03d", transactionIdCounter.getAndIncrement());
//            tableModel.addRow(new Object[] {
//                    transactionId,
//                    customerIdField.getText(),
//                    dateField.getText(),
//                    timeField.getText(),
//                    DashBoard.getCurrentUserId(),
//                    "Click to view",
//                    "$" + totalAmount.get()
//            });

        }
    }

    public void insertTransaction(String customerId, String date, String time, String salespersonId,
            Map<String, String> productsQuantities) {
        
        Connection conn = null;
        try  {
            conn = Manager.getConnection();
            conn.setAutoCommit(false);
            String insertTransactionSQL = "INSERT INTO transactions (CustomerID, Date, Time, SalespersonID) VALUES (?,?,?,?)";
            PreparedStatement transactionStmt = conn.prepareStatement(insertTransactionSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            transactionStmt.setString(1, customerId);
            transactionStmt.setDate(2, java.sql.Date.valueOf(date)); // Assuming 'date' is in "yyyy-MM-dd" format
            transactionStmt.setTime(3, java.sql.Time.valueOf(time)); // Assuming 'time' is in "HH:mm:ss" format
            transactionStmt.setInt(4, Integer.parseInt(salespersonId));
            int rowsAffected = transactionStmt.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Transaction Inserted");
            } else {
                System.out.println("No rows were inserted.");
            }

            ResultSet rs = transactionStmt.getGeneratedKeys();
            int transactId = rs.next() ? rs.getInt(1) : -1;

            String insertItemSQL = "INSERT INTO transactionitems (TransactionID, ProductID, ProductName, Quantity, Price) VALUES (?,?,?,?,?)";
            PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL);
            for (Map.Entry<String, String> entry : productsQuantities.entrySet()) {
                int productId = Integer.parseInt(entry.getKey());
                int quantity = Integer.parseInt(entry.getValue());
                // Fetch price from Products table
                String fetchPriceSQL = "SELECT price, product_name FROM product_inventory WHERE product_id =?";
                PreparedStatement priceStmt = conn.prepareStatement(fetchPriceSQL);
                priceStmt.setInt(1, productId);
                ResultSet Rs = priceStmt.executeQuery();
                double priceRs;
                String name;
                if (Rs.next()) {
                    priceRs = Rs.getDouble(1);
                    name = Rs.getString(2);

                } else {
                    priceRs = 0;
                    name = "";
                }
                // double price = Rs.next() ? Rs.getDouble("price") : 0.0; // Handle price not
                // found

                // Insert into TransactionItems
                itemStmt.setInt(1, transactId);
                itemStmt.setInt(2, productId);
                itemStmt.setString(3, name);
                itemStmt.setInt(4, quantity);
                itemStmt.setDouble(5, priceRs);
                
                int res = itemStmt.executeUpdate();
                if (res >0)
                {
                    System.out.println("Transaction Inserted");
                } else {
                    System.out.println("No rows were inserted.");
                }
                // Update stock level
                String updateStockSQL = "UPDATE product_inventory SET stock_level = stock_level -? WHERE product_id =?";
                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL);
                updateStockStmt.setInt(1, quantity);
                updateStockStmt.setInt(2, productId);
                updateStockStmt.executeUpdate();

            }

            // Commit the transaction
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    private JTextField createHighlightedTextField(Font font) {
        JTextField textField = new JTextField(20);
        textField.setFont(font);
        textField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        });

        return textField;
    }

    private void addFieldToPanel(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc, int yPos,
            Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        panel.add(textField, gbc);
    }

    private void showProductDetails(String transactionId) {
        try (Connection connection = Manager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT ProductID, ProductName, Quantity, Price FROM TransactionItems WHERE TransactionID = ?")) {
            statement.setString(1, transactionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                DefaultTableModel model = new DefaultTableModel(new Object[]{"Product ID", "Product Name", "Quantity", "Price"}, 0);
                while (resultSet.next()) {
                    String productId = resultSet.getString("ProductID");
                    String productName = resultSet.getString("ProductName");
                    int quantity = resultSet.getInt("Quantity");
                    double price = resultSet.getDouble("Price");
                    model.addRow(new Object[]{productId, productName, quantity, price});
                }

                SwingUtilities.invokeLater(() -> {
                    JTable productTable = new JTable(model);
                    productTable.setRowHeight(30);
                    productTable.setFont(new Font("Lato", Font.PLAIN, 16));
                    productTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
                    productTable.getTableHeader().setBackground(new Color(0xF47130));
                    productTable.getTableHeader().setForeground(Color.WHITE);
                    productTable.setFillsViewportHeight(true);
                    productTable.setGridColor(new Color(0xF47130));
                    productTable.setShowGrid(true);
                    productTable.setSelectionBackground(new Color(0xF47130));
                    productTable.setSelectionForeground(Color.WHITE);

                    JScrollPane scrollPane = new JScrollPane(productTable);
                    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xF47130), 2));

                    // Increase the size of the dialog window
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int width = (int) (screenSize.width * 0.6); // 40% of screen width
                    int height = (int) (screenSize.height * 0.4); // 40% of screen height
                    scrollPane.setPreferredSize(new Dimension(width, height));

                    JOptionPane.showMessageDialog(this, scrollPane, "Product Details", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sales Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new SalesPanel());
            frame.setVisible(true);
        });
    }
}
