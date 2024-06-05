package com.example.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HomePanel extends JPanel {

    private static final Color ACCENT_COLOR = new Color(0xF47130);
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    public HomePanel() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20)); // Add padding between components

        // Panel for total sales at the top
        JPanel totalSalesPanel = createRoundedPanel("Total Sales", getTotalSales(), new Font("Serif", Font.BOLD, 48));
        totalSalesPanel.setMaximumSize(new Dimension(400, 150));
        add(totalSalesPanel, BorderLayout.NORTH);

        // Panel for customers and low stock in the middle
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        middlePanel.setBackground(BACKGROUND_COLOR);
        middlePanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the panel
        middlePanel.add(createRoundedPanel("Customers", getCustomerCount(), new Font("Serif", Font.BOLD, 24)));
        middlePanel.add(createRoundedPanel("Low Stock", getLowStockCount(), new Font("Serif", Font.BOLD, 24)));
        add(middlePanel, BorderLayout.CENTER);

        // Panel for recent sales transactions at the bottom
        JPanel transactionsPanel = new JPanel(new BorderLayout());
        transactionsPanel.setBackground(BACKGROUND_COLOR);
        transactionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the panel
        JLabel transactionsLabel = new JLabel("Recent Sales Transactions", SwingConstants.LEFT);
        transactionsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        transactionsLabel.setForeground(ACCENT_COLOR);
        transactionsLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR)); // Add bottom border
        transactionsPanel.add(transactionsLabel, BorderLayout.NORTH);
        transactionsPanel.add(createTransactionsTable(getRecentTransactions()), BorderLayout.CENTER);
        add(transactionsPanel, BorderLayout.SOUTH);
    }

    // Method to create a rounded panel for summary
    private JPanel createRoundedPanel(String title, int value, Font valueFont) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BACKGROUND_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }

            @Override
            protected void paintBorder(Graphics g) {
                super.paintBorder(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 0)); // Set background to be transparent

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        titleLabel.setForeground(ACCENT_COLOR);

        JLabel valueLabel = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        valueLabel.setFont(valueFont);
        valueLabel.setForeground(ACCENT_COLOR);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    // Method to create a table for recent transactions
    private JScrollPane createTransactionsTable(List<String[]> transactions) {
        String[] columnNames = {"Date", "Customer", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (String[] transaction : transactions) {
            model.addRow(transaction);
        }
        JTable table = new JTable(model);
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2)); // Add border to the scroll pane
        return scrollPane;
    }

    // Mock methods to get data (replace with actual data fetching logic)
    private int getTotalSales() {
        return 1000; // Example value
    }

    private int getCustomerCount() {
        return 200; // Example value
    }

    private int getLowStockCount() {
        return 15; // Example value
    }

    private List<String[]> getRecentTransactions() {
        return List.of(
                new String[]{"2024-05-20", "John Doe", "$150"},
                new String[]{"2024-05-19", "Jane Smith", "$200"}
                // Add more transactions
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Home Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HomePanel());
        frame.setVisible(true);
    }
}
