package com.example;

import java.awt.*;
import javax.swing.*;

public class LoginForm {
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
        // Get the default toolkit to access system-wide resources
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // Get the screen size to dynamically set the window size
        Dimension screenSize = toolkit.getScreenSize();
        // Calculate the maximum width and height for the window
        int maxWidth = (int) (screenSize.width *.75);
        int maxHeight = (int) (screenSize.height *.75);

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
        // Set the background color of the left panel
        leftPanel.setBackground(new Color(0xF47130));
        // Set the position and size of the left panel
        leftPanel.setBounds(0, 0, (int) (maxWidth *.58), maxHeight);
        // Set the layout manager for the left panel
        leftPanel.setLayout(new BorderLayout());

        // Group 4: Configure logoLabel
        // Set the text displayed by the logo label
        logoLabel.setText("Welcome");
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
        // Set the position and size of the login panel
        loginPanel.setBounds((int) (maxWidth *.66), 0, (int) (maxWidth *.34), maxHeight);
        // Set the layout manager for the login panel
        loginPanel.setLayout(new BorderLayout());

        // Group 6: Assembly
        // Add the logo label to the left panel
        leftPanel.add(logoLabel);
        // Add the left panel to the login frame
        loginFrame.add(leftPanel);
        // Add the login panel to the login frame
        loginFrame.add(loginPanel);
    }
}