package com.example.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.atomic.AtomicInteger;

public class SalesPanel extends JPanel {
    // Define the accent color
    private static final Color ACCENT_COLOR = new Color(0xF47130);

    // To auto-increment transaction IDs
    private static final AtomicInteger transactionIdCounter = new AtomicInteger(1);
    // Placeholder for the current logged-in salesperson ID
    private static final String currentSalespersonId = "S001";

    // Constructor for the SalesPanel
    public SalesPanel() {
        // Set the layout for this panel
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE); // Set the background color to white
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a panel for the title and search components
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Sales Transactions", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));

        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        // Create the search field and column selector
        JTextField searchField = new JTextField(20);
        // Add focus listener to the search field
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

        String[] columns = {"Transaction ID", "Customer ID", "Date", "Time", "Salesperson ID", "Total Amount"};
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

        // Create the table model for sales transactions
        DefaultTableModel transactionTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the "Transaction ID" and "Salesperson ID" columns non-editable
                return column != 0 && column != 4;
            }
        };

        // Add some sample data to the table model
        transactionTableModel.addRow(new Object[]{"T001", "C001", "2023-05-01", "10:30 AM", "S001", "$600.00"});
        transactionTableModel.addRow(new Object[]{"T002", "C002", "2023-05-02", "11:00 AM", "S002", "$45.00"});
        transactionTableModel.addRow(new Object[]{"T003", "C003", "2023-05-03", "01:45 PM", "S003", "$150.00"});

        // Create the table using the transaction table model
        JTable transactionTable = new JTable(transactionTableModel);
        transactionTable.setRowHeight(30); // Set the height of each row
        transactionTable.setFont(new Font("Lato", Font.PLAIN, 16));
        transactionTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        transactionTable.getTableHeader().setBackground(ACCENT_COLOR);
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setGridColor(ACCENT_COLOR);
        transactionTable.setShowGrid(true);
        transactionTable.setSelectionBackground(ACCENT_COLOR);
        transactionTable.setSelectionForeground(Color.WHITE);

        // Create a scroll pane for the table
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        transactionScrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));

        // Display the transaction table
        add(transactionScrollPane, BorderLayout.CENTER);

        // Create a row sorter for the table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(sorter);

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

        // Create a panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(Color.WHITE); // Set button panel background to white
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Create action buttons for adding, delete, edit, and export
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Update");
        JButton exportButton = new JButton("Export");

        // Set button colors to the accent color and text color to white
        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);
        editButton.setBackground(ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);

        // Add action buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(exportButton);

        // Add the button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners for buttons
        exportButton.addActionListener(e -> {
            // Code to export sales reports (to be implemented)
        });

        addButton.addActionListener(e -> createSales(transactionTableModel));

        deleteButton.addActionListener(e -> {
            // Code to delete selected transaction (to be implemented)
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow != -1) {
                transactionTableModel.removeRow(selectedRow);
            }
        });

        editButton.addActionListener(e -> {
            // Code to edit selected transaction (to be implemented)
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

        // Excluding transaction ID and salesperson ID fields
        JTextField customerIdField = createHighlightedTextField(20, font);
        JTextField dateField = createHighlightedTextField(20, font);
        JTextField timeField = createHighlightedTextField(20, font);
        JTextField purchasedItemsField = createHighlightedTextField(20, font);
        JTextField totalAmountField = createHighlightedTextField(20, font);

        addFieldToPanel(panel, "Customer ID:", customerIdField, gbc, 0, font);
        addFieldToPanel(panel, "Date:", dateField, gbc, 1, font);
        addFieldToPanel(panel, "Time:", timeField, gbc, 2, font);
        addFieldToPanel(panel, "List of Purchased Items:", purchasedItemsField, gbc, 3, font);
        addFieldToPanel(panel, "Total Amount:", totalAmountField, gbc, 4, font);

        // Customize the JOptionPane to remove the question mark icon
        int option = JOptionPane.showConfirmDialog(this, panel, "Create New Sales Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String transactionId = String.format("T%03d", transactionIdCounter.getAndIncrement());
            tableModel.addRow(new Object[]{
                    transactionId,
                    customerIdField.getText(),
                    dateField.getText(),
                    timeField.getText(),
                    currentSalespersonId,
                    totalAmountField.getText()
            });
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sales Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new SalesPanel());
        frame.setVisible(true);
    }
}
