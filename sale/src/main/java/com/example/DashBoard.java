package com.example;

import com.example.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DashBoard {
    // Maps to hold the paths for the button icons and their white variants
    private static final Map<JButton, String> buttonIconPaths = new HashMap<>();
    private static final Map<JButton, String> buttonIconPathsWhite = new HashMap<>();
    // Panel to hold the main content
    private static JPanel mainContentPanel;
    static final MainFrame dashboardFrame = new MainFrame();
    static final JFrame mainFrame = dashboardFrame.getMainFrame();
    private static String currentId;

    public static void UI()
    {
        // Create the sidebar panel
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0xF5F5F5)); // Light gray background
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Vertical layout
        sidebar.setBounds(0, 0, 320, dashboardFrame.getMaxHeight());

        // Create the main content panel
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());

        // Add logo label
        JLabel logoLabel = new JLabel("SalesEdge");
        logoLabel.setFont(new Font("Roboto", Font.BOLD, 50));
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        logoLabel.setPreferredSize(new Dimension(320, 50));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 20)); // Add padding
        sidebar.add(logoLabel);

        // Add a vertical gap after the logo
        sidebar.add(Box.createVerticalStrut(20));

        // Button labels and icons
        String[] buttonLabels = {"Home", "Customer", "Product", "Sales", "Inventory", "Report", "Settings"};
        String[] iconPaths = {
                "sale\\src\\main\\resources\\home.png",
                "sale\\src\\main\\resources\\customer.png",
                "sale\\src\\main\\resources\\product.png",
                "sale\\src\\main\\resources\\sales.png",
                "sale\\src\\main\\resources\\inventory.png",
                "sale\\src\\main\\resources\\report.png",
                "sale\\src\\main\\resources\\user_settings.png"  // Added settings icon
        };
        String[] iconPathsWhite = {
                "sale\\src\\main\\resources\\home-white.png",
                "sale\\src\\main\\resources\\customer-white.png",
                "sale\\src\\main\\resources\\product-white.png",
                "sale\\src\\main\\resources\\sales-white.png",
                "sale\\src\\main\\resources\\inventory-white.png",
                "sale\\src\\main\\resources\\report-white.png",
                "sale\\src\\main\\resources\\user_settings-white.png"  // Added settings white icon
        };

        // Add buttons to the sidebar and set the Home button as selected
        JButton[] buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            // Create a sidebar button with the label and icon
            JButton button = createSidebarButton(buttonLabels[i], iconPaths[i]);
            buttons[i] = button;
            buttonIconPaths.put(button, iconPaths[i]);
            buttonIconPathsWhite.put(button, iconPathsWhite[i]);

            // Select the Home button by default
            if (i == 0) {
                selectButton(button, buttons);
                showPanel(new HomePanel()); // Initially show the HomePanel
            }

            // Add action listener to each button
            int index = i;
            button.addActionListener(e -> {
                selectButton(button, buttons);
                showPanel(getPanelForIndex(index));
            });

            sidebar.add(button);
        }

        // Add the sidebar and main content panel to the main frame
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(sidebar, BorderLayout.WEST);
        mainFrame.add(mainContentPanel, BorderLayout.CENTER);
    }

    // Method to initialize the dashboard
    public static void initializeDashboard(JFrame loginFrame, String id) {
        // Dispose of the login frame
        loginFrame.dispose();
        // Set the Id
        currentId = id;
        UI();
        // Create the main dashboard frame
        dashboardFrame.showMainFrame();
    }
    public static String getCurrentUserId() {
        return currentId;
    }
    // for testing without logging in
    public static void initializeDashboard() {
        UI();

        dashboardFrame.showMainFrame();
    }

    // Method to create a sidebar button
    private static JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setIcon(new ImageIcon(iconPath));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(new Color(0xF5F5F5)); // Light gray background
        button.setPreferredSize(new Dimension(360, 50));
        button.setMaximumSize(new Dimension(360, 50));
        button.setFont(new Font("Lato", Font.PLAIN, 23));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setIconTextGap(20);
        button.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 20)); // Add padding for better alignment
        return button;
    }

    // Method to select a button and change its appearance
    private static void selectButton(JButton selectedButton, JButton[] buttons) {
        if (selectedButton == null || buttons == null) {
            return;
        }

        // Loop through all buttons and update their appearance based on selection
        for (JButton button : buttons) {
            if (button != null) {
                if (button == selectedButton) {
                    button.setBackground(new Color(0xF47130)); // Orange background
                    button.setForeground(Color.WHITE); // White font
                    button.setIcon(new ImageIcon(buttonIconPathsWhite.get(button))); // White icon
                } else {
                    button.setBackground(new Color(0xF5F5F5)); // Default background
                    button.setForeground(Color.BLACK); // Default font color
                    button.setIcon(new ImageIcon(buttonIconPaths.get(button))); // Default icon
                }
            }
        }
    }

    // Method to show a panel in the main content area
    private static void showPanel(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    // Method to get the panel for the given index
    private static JPanel getPanelForIndex(int index) {
        return switch (index) {
            case 1 -> new CustomerPanel();
            case 2 -> new ProductPanel();
            case 3 -> new SalesPanel();
            case 4 -> new InventoryPanel();
            case 5 -> new ReportPanel();
            case 6 -> new SettingsPanel(getCurrentUserId()); // Added SettingsPanel
            default -> new HomePanel(); // Default HomePanel
        };
    }

    public static void cleanUpMainFrame() {
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
        }
    }

    public static void main(String[] args) {
        initializeDashboard();
    }
}
