package com.example.panels;

import com.example.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(0xF5F5F5); // Light gray background
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(0xF47130); // Orange
    private static final Color TEXT_COLOR = new Color(0x4A4A4A); // Dark gray
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(200, 40); // Set the height of the text fields
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 40;
    private String loggedUserID;
    private JTextField[] textFields = new JTextField[8];

    public SettingsPanel(String userID) {

        this.loggedUserID = userID;

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

        textFields[0] = updateLabeledTextField(formPanel, "First Name", gbc, 0, 0, "first_name");
        textFields[1] = updateLabeledTextField(formPanel, "Last Name", gbc, 0, 1, "last_name");
        textFields[2] = updateLabeledTextField(formPanel, "Username", gbc, 0, 2, "username");
        textFields[3] = updateLabeledTextField(formPanel, "Password", gbc, 0, 3, "password");
        textFields[4] = updateLabeledTextField(formPanel, "Email", gbc, 1, 0, "email");
        textFields[5] = updateLabeledTextField(formPanel, "Address", gbc, 1, 1, "address");
        textFields[6] = updateLabeledTextField(formPanel, "Phone", gbc, 1, 2, "phone");
        textFields[7] = updateLabeledTextField(formPanel, "Role", gbc, 1, 3, "role");

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
        saveButton.addActionListener(e -> updateStaffLoggedIn());

        // Add ActionListener to the logout button
        logoutButton.addActionListener(e -> {
            // Close the current SettingsPanel window
            SwingUtilities.getWindowAncestor(SettingsPanel.this).dispose();
            // Open the login form
            LoginForm.on_start();
        });

        // Add logout and save buttons to the bottom right corner within the content
        // panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); // Adding spacing between buttons
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(logoutButton);

        // Add a button panel to the south of content panel
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add a content panel to the center of this panel
        add(contentPanel, BorderLayout.CENTER);
    }

    private void updateStaffLoggedIn() {
        try (Connection con = Manager.getConnection()) {
            String updateQuery = "UPDATE staff SET first_name =?, last_name =?, username =?, password =?, email =?, address =?, phone =?, role =? WHERE staff_id =?";
            try (PreparedStatement statement = con.prepareStatement(updateQuery)) {
                // Set parameters from textFields in order
                statement.setString(1, textFields[0].getText());
                statement.setString(2, textFields[1].getText());
                statement.setString(3, textFields[2].getText());
                statement.setString(4, textFields[3].getText());
                statement.setString(5, textFields[4].getText());
                statement.setString(6, textFields[5].getText());
                statement.setString(7, textFields[6].getText());
                statement.setString(8, textFields[7].getText());

                // Add more setString calls for additional fields as needed
                statement.setString(9, loggedUserID); // Assuming loggedUserID is the staff_id to update
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "User updated successfully");
                } else {
                    System.out.println("No user found with the specified ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to update user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database.");
        }
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

        subPanel.add(label, BorderLayout.NORTH);
        subPanel.add(textField, BorderLayout.CENTER);

        panel.add(subPanel, gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Settings Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new SettingsPanel(""));
        frame.setVisible(true);
    }

    private JTextField updateLabeledTextField(JPanel panel, String labelText, GridBagConstraints gbc, int x, int y,
            String columnName) {
        gbc.gridx = x;
        gbc.gridy = y;
        String text = "";

        JPanel subPanel = new JPanel(new BorderLayout(5, 5));
        subPanel.setBackground(PANEL_COLOR);

        JLabel label = new JLabel(labelText.toUpperCase());
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);

        try (Connection con = Manager.getConnection()) {
            String sql = "SELECT * FROM staff WHERE staff_id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, loggedUserID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                text = resultSet.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Lato", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));
        textField.setPreferredSize(TEXT_FIELD_SIZE); // Set preferred size to control height

        subPanel.add(label, BorderLayout.NORTH);
        subPanel.add(textField, BorderLayout.CENTER);

        panel.add(subPanel, gbc);
        return textField;
    }

}
