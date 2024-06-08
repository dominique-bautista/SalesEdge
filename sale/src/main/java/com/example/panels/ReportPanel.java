package com.example.panels;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import com.example.panels.report.DashboardReport;
import com.example.panels.report.CustomerReport;
import com.example.panels.report.ProductReport;
import com.example.panels.report.SalesReport;
import com.example.panels.report.InventoryReport;

public class ReportPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton dashboardButton; // New dashboard button reference

    public ReportPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Reports", SwingConstants.CENTER);
        title.setFont(new Font("Roboto", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Top Buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); // BoxLayout with horizontal alignment
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        String[] buttonLabels = {"Dashboard", "Customer", "Product", "Sales", "Inventory"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Lato", Font.PLAIN, 16));
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(120, 30)); // Smaller button size
            topPanel.add(button);

            // Add action listener to navigate to the respective panel
            button.addActionListener(e -> {
                selectButton(button, topPanel);
                cardLayout.show(cardPanel, label);
            });

            // Set the background and foreground colors for the Dashboard button
            if (label.equals("Dashboard")) {
                button.setBackground(new Color(0xF47130)); // Orange background
                button.setForeground(Color.WHITE); // White font
                dashboardButton = button; // Store a reference to the Dashboard button
            }
        }
        add(topPanel, BorderLayout.NORTH);

        // Manually call selectButton to style the Dashboard button
        selectButton(dashboardButton, topPanel);

        // Card Layout to switch between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Add panels for each report with customized scroll panes
        cardPanel.add(createCustomScrollPane(new DashboardReport()), "Dashboard");
        cardPanel.add(createCustomScrollPane(new CustomerReport()), "Customer");
        cardPanel.add(createCustomScrollPane(new ProductReport()), "Product");
        cardPanel.add(createCustomScrollPane(new SalesReport()), "Sales");
        cardPanel.add(createCustomScrollPane(new InventoryReport()), "Inventory");

        add(cardPanel, BorderLayout.CENTER);
    }

    private void selectButton(JButton selectedButton, JPanel topPanel) {
        for (Component component : topPanel.getComponents()) {
            if (component instanceof JButton button) {
                if (button == selectedButton) {
                    button.setBackground(new Color(0xF47130)); // Orange background
                    button.setForeground(Color.WHITE); // White font
                } else {
                    button.setBackground(new Color(0xF5F5F5)); // Default background
                    button.setForeground(Color.BLACK); // Default font color
                }
            }
        }
    }

    private JScrollPane createCustomScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Customize scroll pane
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0xF47130);
            }
        });

        return scrollPane;
    }
}
