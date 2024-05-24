package com.example;

import java.awt.*;
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
        ImageIcon logo = new ImageIcon("sale\\src\\main\\resources\\SaleEge.png");// Load the logo image
        MainFrame mF = new MainFrame();// Create the main window frame
        JLabel logoLabel = new JLabel();// Create a label to display the logo text
        JPanel leftPanel = new JPanel();// Create a panel for the left side of the window
        JPanel loginPanel = new JPanel();// Create a panel for the login details on the right side
        Toolkit toolkit = Toolkit.getDefaultToolkit();// Get the default toolkit to access system-wide resources
        Dimension screenSize = toolkit.getScreenSize();// Get the screen size to dynamically set the window size
        int maxWidth = (int) (screenSize.width * .75);// Calculate the maximum width and height for the window
        int maxHeight = (int) (screenSize.height * .75);
        mF.showMainFrame();
        // Group 2: Configure mF
        // mF.setSize(maxWidth, maxHeight);// Set the size of the login frame based on the calculated dimensions
        // mF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Specify the operation to perform when the window is closed
        // mF.setResizable(false);// Prevent resizing of the window
        // mF.setLayout(null);// Set the layout manager for the frame to null for manual positioning
        // mF.setLocationRelativeTo(null);// Center the window on the screen
        // Set the icon image for the windowd
        // mF.setVisible(true);// Make the frame visible
        
        // Group 3: Configure leftPanel
        leftPanel.setBackground(new Color(0xF47130));// Set the background color of the left panel
        int leftPanelWidth = (int) (maxWidth * 0.58);// Calculate the width of the left panel
        leftPanel.setBounds(0, 0, leftPanelWidth, maxHeight);// Set the position and size of the left panel
        leftPanel.setLayout(new BorderLayout());// Set the layout manager for the left panel
        
        // Group 4: Configure logoLabel
        logoLabel.setText("Welcome");// Set the text displayed by the logo label
        logoLabel.setIcon(logo);// Set the icon for the logo label
        logoLabel.setVerticalAlignment(JLabel.TOP);// Align the text vertically at the top of the label
        logoLabel.setHorizontalAlignment(JLabel.CENTER);// Center the text horizontally within the label
        logoLabel.setHorizontalTextPosition(JLabel.CENTER);// Position the horizontal text relative to the icon
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM);// Position the vertical text relative to the icon
        logoLabel.setFont(new Font("Inter", Font.BOLD, 26));// Set the font for the logo label
        
        // Group 5: Configure loginPanel
        loginPanel.setBackground(new Color(0x656D72));// Set the background color of the login panel
        int loginPanelWidth = maxWidth - leftPanelWidth;// Calculate the remaining width for the login panel
        loginPanel.setBounds(leftPanelWidth, 0, loginPanelWidth, maxHeight);// Set the position and size of the login panel
        loginPanel.setLayout(new GridBagLayout());// Set the layout manager for the login panel
        
        // Group 6: Assembly
        leftPanel.add(logoLabel);// Add the logo label to the left panel
        mF.getMainFrame().getContentPane().add(leftPanel);// Add the left panel to the login frame
        JPanel topPanel = new JPanel(new GridBagLayout());// Add the login panel to the login frame
        
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        topPanel.setBackground(new Color(0x656D72)); // Set the background color to visualize the top panel
        loginPanel.add(topPanel, createGBC(0, 0, 2, 1.0, 1.0, GridBagConstraints.BOTH));// Configure GridBagConstraints for the topPanel

        middlePanel.setBackground(new Color(0x656D72)); // Set the background color to visualize the middle panel
        loginPanel.add(middlePanel, createGBC(0, 1, 2, 1.0, 2.0, GridBagConstraints.BOTH));// Configure GridBagConstraints for the middlePanel

        bottomPanel.setBackground(new Color(0x656D72)); // Set the background color to visualize the bottom panel
        loginPanel.add(bottomPanel, createGBC(0, 2, 2, 1.0, 2.0, GridBagConstraints.BOTH));// Configure GridBagConstraints for the bottomPanel

        JLabel topLabel = new JLabel("Sales Edge");
        topLabel.setFont(new Font("Times New Roman", Font.BOLD, 19));
        topPanel.add(topLabel, createGBC(0, 0, 0, 0,0,GridBagConstraints.BOTH,GridBagConstraints.CENTER));// Configure GridBagConstraints for the topLabel
        mF.getMainFrame().getContentPane().add(loginPanel);
    }
}