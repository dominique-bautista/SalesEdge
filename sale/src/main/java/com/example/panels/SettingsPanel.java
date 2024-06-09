package com.example.panels;

import com.example.LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SettingsPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(0xF5F5F5); // Light gray background
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(0xF47130); // Orange
    private static final Color TEXT_COLOR = new Color(0x4A4A4A); // Dark gray
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(200, 40); // Set the height of the text fields
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 40;

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Create the main content panel with padding
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        contentPanel.setLayout(new BorderLayout());

        // Create the header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(PANEL_COLOR);
        JLabel headerLabel = new JLabel("Account Settings");
        headerLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        headerLabel.setForeground(Color.BLACK);
        headerPanel.add(headerLabel);

        JLabel subHeaderLabel = new JLabel("Change your profile and account settings");
        subHeaderLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
        subHeaderLabel.setForeground(Color.BLACK);
        headerPanel.add(subHeaderLabel);

        // Add a header panel to the top of the content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        gbc.weightx = 1; // Allow horizontal expansion

        addLabeledTextField(formPanel, "First Name", gbc, 0, 0);
        addLabeledTextField(formPanel, "Last Name", gbc, 0, 1);
        addLabeledTextField(formPanel, "Username", gbc, 0, 2);
        addLabeledTextField(formPanel, "Password", gbc, 0, 3);
        addLabeledTextField(formPanel, "Email", gbc, 1, 0);
        addLabeledTextField(formPanel, "Address", gbc, 1, 1);
        addLabeledTextField(formPanel, "Phone", gbc, 1, 2);
        addLabeledTextField(formPanel, "Role", gbc, 1, 3);

        // Add a form panel to the center of content panel
        contentPanel.add(formPanel, BorderLayout.CENTER);

        // Create the logout and save buttons
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        logoutButton.setFont(new Font("Roboto", Font.BOLD, 16));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(ACCENT_COLOR);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.setFocusPainted(false);

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        saveButton.setFont(new Font("Roboto", Font.BOLD, 16));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(ACCENT_COLOR);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);

        // Add ActionListener to the logout button
        logoutButton.addActionListener(e -> {
            // Close the current SettingsPanel window
            SwingUtilities.getWindowAncestor(SettingsPanel.this).dispose();
            // Open the login form
            LoginForm.on_start();
        });

        // Add logout and save buttons to the bottom right corner within the content panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); // Adding spacing between buttons
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(logoutButton);

        // Add a button panel to the south of content panel
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add a content panel to the center of this panel
        add(contentPanel, BorderLayout.CENTER);
    }

    // Method to create and add a labeled text field
    private void addLabeledTextField(JPanel panel, String labelText, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;

        JPanel subPanel = new JPanel(new BorderLayout(5, 5));
        subPanel.setBackground(PANEL_COLOR);

        JLabel label = new JLabel(labelText.toUpperCase());
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);

        JTextField textField = new JTextField();
        textField.setFont(new Font("Lato", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));
        textField.setPreferredSize(TEXT_FIELD_SIZE); // Set preferred size to control height

        // Add focus listener to change border color on focus
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));
            }
        });

        subPanel.add(label, BorderLayout.NORTH);
        subPanel.add(textField, BorderLayout.CENTER);

        panel.add(subPanel, gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Settings Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new SettingsPanel());
        frame.setVisible(true);
    }
}
