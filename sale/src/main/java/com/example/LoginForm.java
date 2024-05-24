package com.example;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class LoginForm {
    private static GridBagConstraints createGBC(int gridx, int gridy, int gridwidth, double weightx, double weighty,
            int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        return gbc;
    }
    private static GridBagConstraints createGBC(int gridx, int gridy, int gridwidth, double weightx, double weighty,
            int fill, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        gbc.anchor = anchor;
        return gbc;
    }

    public static void main(String[] args) {
        // Group 1: Initialize components
        // Load the logo image
        ImageIcon logo = new ImageIcon("sale\\src\\main\\resources\\SaleEge.png");
        // Create the main window frame
        JFrame loginFrame = new JFrame();
        // Create a label to display the logo text
        JLabel logoLabel = new JLabel();
        // Create a panel for the left side of the window
        JPanel leftPanel = new JPanel();
        // Create a panel for the login details on the right side
        JPanel loginPanel = new JPanel();
        // Create fields for username and password
        // Creates labels and fields for username and password
        JLabel salesEdgeLabel = new JLabel("SalesEdge");
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        // Create a login button
        JButton loginButton = new JButton("Login");
        // Get the default toolkit to access system-wide resources
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // Get the screen size to dynamically set the window size
        Dimension screenSize = toolkit.getScreenSize();
        // Calculate the maximum width and height for the window
        int maxWidth = (int) (screenSize.width * .75);
        int maxHeight = (int) (screenSize.height * .75);

        // Group 2: Configure loginFrame
        // Set the size of the login frame based on the calculated dimensions
        loginFrame.setSize(maxWidth, maxHeight);
        // Specify the operation to perform when the window is closed
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Prevent resizing of the window
        loginFrame.setResizable(false);
        // Set the layout manager for the frame to null for manual positioning
        loginFrame.setLayout(null);
        // Center the window on the screen
        loginFrame.setLocationRelativeTo(null);
        // Set the icon image for the window
        loginFrame.setIconImage(logo.getImage());
        // Make the frame visible
        loginFrame.setVisible(true);

        // Group 3: Configure leftPanel
        leftPanel.setBackground(new Color(0xF47130));// Set the background color of the left panel
        int leftPanelWidth = (int) (maxWidth * 0.58);// Calculate the width of the left panel
        leftPanel.setBounds(0, 0, leftPanelWidth, maxHeight);// Set the position and size of the left panel
        leftPanel.setLayout(new BorderLayout());// Set the layout manager for the left panel
        
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
        logoLabel.setIcon(logo);
        // Align the text vertically at the top of the label
        logoLabel.setVerticalAlignment(JLabel.TOP);
        // Center the text horizontally within the label
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        // Position the horizontal text relative to the icon
        logoLabel.setHorizontalTextPosition(JLabel.CENTER);
        // Position the vertical text relative to the icon
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM);
        // Set the font for the logo label
        logoLabel.setFont(new Font("Inter", Font.BOLD, 26));

        // Group 5: Configure loginPanel
        loginPanel.setBackground(new Color(0xFDFDFD));
        // Calculate the remaining width for the login panel
        int loginPanelWidth = maxWidth - leftPanelWidth;
        // Set the position and size of the login panel
        loginPanel.setBounds(leftPanelWidth, 0, loginPanelWidth, maxHeight);
        // Set the layout manager for the login panel
        loginPanel.setLayout(new GridBagLayout());

        // Create fonts
        Font titleFont = new Font("Roboto", Font.BOLD, 36);
        Font font = new Font("Lato", Font.BOLD, 14);

        // Set font for salesEdge label
        salesEdgeLabel.setFont(titleFont);
        salesEdgeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Set font for username label and field
        usernameLabel.setFont(font);
        usernameField.setFont(font);
        // Set font for password label and field
        passwordLabel.setFont(font);
        passwordField.setFont(font);
        // Set font for login button
        loginButton.setFont(font);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0); // Add space above and below salesEdge label

        // Add salesEdge label
        loginPanel.add(salesEdgeLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Consistent space between components

        // Add username label and field
        loginPanel.add(usernameLabel, gbc);
        gbc.gridy++;
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0xF47130), 2)); // Set orange border
        loginPanel.add(usernameField, gbc);

        gbc.gridy++;
        // Add password label and field
        loginPanel.add(passwordLabel, gbc);
        gbc.gridy++;
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0xF47130), 2)); // Set orange border
        loginPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5); // Increase top space for login button
        loginButton.setBackground(new Color(0xF47130)); // Set orange background
        loginButton.setForeground(Color.WHITE); // Set text color to white
        loginButton.setOpaque(true); // Make the background color visible
        loginButton.setBorderPainted(false); // Remove the default border
        loginPanel.add(loginButton, gbc);

        // Group 6: Assembly
        // Add the logo label to the left panel
        leftPanel.add(logoLabel, BorderLayout.CENTER);
        // Add the left panel to the login frame
        loginFrame.add(leftPanel, BorderLayout.WEST);
        // Add the login panel to the login frame
        loginFrame.add(loginPanel, BorderLayout.CENTER);
    }
}
