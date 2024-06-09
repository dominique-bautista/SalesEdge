package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class RegisterForm {

    public static void on_start() {
        // Group 1: Initialize components
        // Create the main window frame
        MainFrame registerFrame = new MainFrame();
        // Create a label to display the logo text
        JLabel logoLabel = new JLabel();
        // Create a panel for the left side of the window
        JPanel leftPanel = new JPanel();
        // Create a panel for the register details on the right side
        JPanel registerPanel = new JPanel();

        // Create fields for the registration form
        JLabel salesEdgeLabel = new JLabel("SalesEdge");
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JButton registerButton = new JButton("Register");

        // Calculate the maximum width and height for the window
        int maxWidth = registerFrame.getMaxWidth();
        int maxHeight = registerFrame.getMaxHeight();

        // Make the frame visible
        registerFrame.showMainFrame();

        // Group 3: Configure leftPanel
        leftPanel.setBackground(new Color(0xF47130)); // Set the background color of the left panel
        int leftPanelWidth = (int) (maxWidth * 0.58); // Calculate the width of the left panel
        leftPanel.setBounds(0, 0, leftPanelWidth, maxHeight); // Set the position and size of the left panel
        leftPanel.setLayout(new BorderLayout()); // Set the layout manager for the left panel

        // Create a button for going back to the login form
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Close the current registration form
            registerFrame.getMainFrame().dispose();
            // Open the login form
            LoginForm.on_start();
        });

        // Set font and color for the back button
        Font backButtonFont = new Font("Lato", Font.BOLD, 18);
        backButton.setFont(backButtonFont);
        backButton.setBackground(new Color(0xF47130)); // Set orange background
        backButton.setForeground(Color.WHITE); // Set text color to white
        backButton.setOpaque(true); // Make the background color visible
        backButton.setBorderPainted(false); // Remove the default border

        // Define padding (10 pixels top and left)
        Insets padding = new Insets(15, 15, 15, 15); // top, left, bottom, right
        backButton.setMargin(padding);

        // Create a panel for the back button and the salesEdge label
        JPanel topPanel = new JPanel(new BorderLayout());

        // Set the background color of the top panel to orange
        topPanel.setBackground(new Color(0xF47130));

        // Add the back button to the top left corner
        topPanel.add(backButton, BorderLayout.WEST);
        // Add the salesEdge label to the top center
        topPanel.add(salesEdgeLabel, BorderLayout.CENTER);

        // Add the top panel to the left panel
        leftPanel.add(topPanel, BorderLayout.NORTH);

        // Group 4: Configure logoLabel
        // Set the text displayed by the logo label to a random greeting
        String[] greetings = {
                "Welcome!", "Bienvenido!", "Bienvenue!", "Willkommen!", "Benvenuto!",
                "欢迎!", "Добро пожаловать!", "ようこそ!", "환영합니다!", "Bem-vindo!",
                "Καλώς ορίσατε!", "Selamat datang!", "Welkom!", "Tervetuloa!"
        };
        Random rand = new Random();
        String randomGreeting = greetings[rand.nextInt(greetings.length)];
        logoLabel.setText(randomGreeting);
        // Set the icon for the logo label
        logoLabel.setIcon(registerFrame.getLogo());
        // Align the text vertically at the top of the label
        logoLabel.setVerticalAlignment(JLabel.TOP);
        // Center the text horizontally within the label
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        // Position the horizontal text relative to the icon
        logoLabel.setHorizontalTextPosition(JLabel.CENTER);
        // Position the vertical text relative to the icon
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM);
        // Set the font for the logo label
        logoLabel.setFont(new Font("Inter", Font.BOLD, 48));

        // Group 5: Configure registerPanel
        registerPanel.setBackground(new Color(0xFDFDFD));
        // Calculate the remaining width for the register panel
        int registerPanelWidth = maxWidth - leftPanelWidth;
        // Set the position and size of the register panel
        registerPanel.setBounds(leftPanelWidth, 0, registerPanelWidth, maxHeight);
        // Set the layout manager for the register panel
        registerPanel.setLayout(new GridBagLayout());

        // Create fonts
        Font titleFont = new Font("Roboto", Font.BOLD, 48);
        Font font = new Font("Lato", Font.BOLD, 18);

        // Set font for salesEdge label
        salesEdgeLabel.setFont(titleFont);
        salesEdgeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Set font for all labels and fields
        firstNameLabel.setFont(font);
        firstNameField.setFont(font);
        lastNameLabel.setFont(font);
        lastNameField.setFont(font);
        usernameLabel.setFont(font);
        usernameField.setFont(font);
        passwordLabel.setFont(font);
        passwordField.setFont(font);
        roleLabel.setFont(font);
        roleField.setFont(font);
        emailLabel.setFont(font);
        emailField.setFont(font);
        phoneLabel.setFont(font);
        phoneField.setFont(font);
        addressLabel.setFont(font);
        addressField.setFont(font);
        registerButton.setFont(font);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 20, 0); // Add space above and below salesEdge label

        // Add salesEdge label
        registerPanel.add(salesEdgeLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 4, 4); // Consistent space between components

        // Add fields and labels to the panel
        addFieldToPanel(firstNameLabel, firstNameField, registerPanel, gbc);
        addFieldToPanel(lastNameLabel, lastNameField, registerPanel, gbc);
        addFieldToPanel(usernameLabel, usernameField, registerPanel, gbc);
        addFieldToPanel(passwordLabel, passwordField, registerPanel, gbc);
        addFieldToPanel(roleLabel, roleField, registerPanel, gbc);
        addFieldToPanel(emailLabel, emailField, registerPanel, gbc);
        addFieldToPanel(phoneLabel, phoneField, registerPanel, gbc);
        addFieldToPanel(addressLabel, addressField, registerPanel, gbc);

        // Before adding the register button
        gbc.gridy++;
        gbc.gridx = 0; // Reset gridx to ensure the button starts from the left
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2; // This is correct for the register button, spanning 2 columns
        gbc.weighty = 1.0; // Allow vertical stretching
        gbc.insets = new Insets(5, 5, 50, 5); // Increase top and bottom space for register button
        registerButton.setBackground(new Color(0xF47130)); // Set orange background
        registerButton.setForeground(Color.WHITE); // Set text color to white
        registerButton.setOpaque(true); // Make the background color visible
        registerButton.setBorderPainted(false); // Remove the default border
        registerPanel.add(registerButton, gbc);

        // Database integration: Register button action listener
        registerButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            // Validate input fields
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                    password.isEmpty() || role.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame.getMainFrame(), "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Database integration: Save data to the database
            try {
                // Establish connection
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesedge", "root", "");

                // Create SQL statement
                String sql = "INSERT INTO staff (first_name, last_name, username, password, role, email, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(
                        3, username);
                statement.setString(4, password);
                statement.setString(5, role);
                statement.setString(6, email);
                statement.setString(7, phone);
                statement.setString(8, address);

                // Execute the statement
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(registerFrame.getMainFrame(), "Registration successful.", "Register", JOptionPane.INFORMATION_MESSAGE);
                }

                // Close connection
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(registerFrame.getMainFrame(), "Error: Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        Insets regPadding = new Insets(5, 5, 5, 5); // top, left, bottom, right
        registerButton.setMargin(regPadding);

        // Group 6: Assembly
        // Add the logo label to the left panel
        leftPanel.add(logoLabel, BorderLayout.CENTER);
        // Add the left panel to the register frame
        registerFrame.getMainFrame().add(leftPanel, BorderLayout.WEST);
        // Add the register panel to the register frame
        registerFrame.getMainFrame().add(registerPanel, BorderLayout.CENTER);
        // Make the frame visible
        registerFrame.showMainFrame();
    }

    private static void addFieldToPanel(JLabel label, JTextField field, JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;

        // Set preferred size and default black border
        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Add FocusListener to change border color on focus
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Change border color to orange when focused
                field.setBorder(BorderFactory.createLineBorder(new Color(0xF47130), 1));
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Change border color back to black when focus lost
                field.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
        });

        panel.add(field, gbc);
    }


    public static void main(String[] args) {
        on_start();
    }
}
