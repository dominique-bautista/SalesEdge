package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SalesPanel extends JPanel {
    private static final Color ACCENT_COLOR = new Color(0xF47130);
    private static final AtomicInteger transactionIdCounter = new AtomicInteger(1);
    private static final String currentSalespersonId = "S001";
    private static final Map<String, String[][]> transactionProductDetails = new HashMap<>();

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

        String[] columns = {"Transaction ID", "Customer ID", "Date", "Time", "Salesperson ID", "Product", "Total Amount"};
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
                return column != 0 && column != 4;
            }
        };

        transactionTableModel.addRow(new Object[]{"T001", "C001", "2023-05-01", "10:30 AM", "S001", "Click to view", "$600.00"});
        transactionProductDetails.put("T001", new String[][]{
                {"P001", "Product A", "2", "$300.00"},
                {"P002", "Product B", "1", "$300.00"}
        });

        transactionTableModel.addRow(new Object[]{"T002", "C002", "2023-05-02", "11:00 AM", "S002", "Click to view", "$45.00"});
        transactionProductDetails.put("T002", new String[][]{
                {"P003", "Product C", "1", "$45.00"}
        });

        transactionTableModel.addRow(new Object[]{"T003", "C003", "2023-05-03", "01:45 PM", "S003", "Click to view", "$150.00"});
        transactionProductDetails.put("T003", new String[][]{
                {"P004", "Product D", "1", "$150.00"}
        });

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
        transactionScrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));

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
        JButton editButton = new JButton("Update");
        JButton exportButton = new JButton("Export");

        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);
        editButton.setBackground(ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
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

        editButton.addActionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow != -1) {
                transactionTableModel.setValueAt("EditedValue", selectedRow, transactionTable.getSelectedColumn());
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

        JTextField customerIdField = createHighlightedTextField(20, font);
        JTextField dateField = createHighlightedTextField(20, font);
        JTextField timeField = createHighlightedTextField(20, font);
        JTextField productField = createHighlightedTextField(20, font);
        JTextField totalAmountField = createHighlightedTextField(20, font);

        addFieldToPanel(panel, "Customer ID:", customerIdField, gbc, 0, font);
        addFieldToPanel(panel, "Date:", dateField, gbc, 1, font);
        addFieldToPanel(panel, "Time:", timeField, gbc, 2, font);
        addFieldToPanel(panel, "Product:", productField, gbc, 3, font);
        addFieldToPanel(panel, "Total Amount:", totalAmountField, gbc, 4, font);

        int option = JOptionPane.showConfirmDialog(this, panel, "Create New Sales Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String transactionId = String.format("T%03d", transactionIdCounter.getAndIncrement());
            String productDetails = productField.getText();
            tableModel.addRow(new Object[]{
                    transactionId,
                    customerIdField.getText(),
                    dateField.getText(),
                    timeField.getText(),
                    currentSalespersonId,
                    "Click to view",
                    totalAmountField.getText()
            });

            String[] products = productDetails.split(";"); // Split products by semicolon
            String[][] productArray = new String[products.length][4];

            for (int i = 0; i < products.length; i++) {
                String[] productInfo = products[i].split(","); // Split each product info by comma
                if (productInfo.length == 4) {
                    productArray[i] = productInfo;
                }
            }
            transactionProductDetails.put(transactionId, productArray);
        }
    }


    private JTextField createHighlightedTextField(int columns, Font font) {
        JTextField textField = new JTextField(columns);
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

    private void showProductDetails(String transactionId) {
        String[][] products = transactionProductDetails.get(transactionId);
        if (products != null) {
            String[] columnNames = {"Product ID", "Product Name", "Quantity", "Price"};
            JTable productTable = new JTable(products, columnNames);
            productTable.setFont(new Font("Serif", Font.PLAIN, 16));
            productTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
            productTable.getTableHeader().setBackground(ACCENT_COLOR);
            productTable.getTableHeader().setForeground(Color.WHITE);
            productTable.setFillsViewportHeight(true);
            productTable.setGridColor(ACCENT_COLOR);
            productTable.setShowGrid(true);
            productTable.setSelectionBackground(ACCENT_COLOR);
            productTable.setSelectionForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(productTable);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            productTable.setPreferredScrollableViewportSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Product Details", JOptionPane.INFORMATION_MESSAGE);
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
