package com.example.panels;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerManager {

    private static Map<String, String> customerIDMap = new HashMap<>();

    // Method to create a new customer
    public static void createCustomer() {
        JFrame createFrame = new JFrame("Create Customer");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(400, 300);
        createFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(createFrame, "Name cannot be empty.");
                return;
            }

            // Create a unique ID
            String customerID = String.valueOf(customerIDMap.size() + 101);
            customerIDMap.put(name, customerID);

            JOptionPane.showMessageDialog(createFrame, "Customer created successfully.");
            createFrame.dispose();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(genderLabel);
        panel.add(genderField);
        panel.add(new JLabel()); // Empty cell
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    // Method to update an existing customer
    public static void updateCustomer(String selectedCustomer) {
        String customerID = customerIDMap.get(selectedCustomer);

        JFrame updateFrame = new JFrame("Update Customer");
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setSize(400, 300);
        updateFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(selectedCustomer);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(selectedCustomer.toLowerCase().replace(" ", ".") + "@example.com");

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField("(123) 456-7890");

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField("123 Main St");

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newName = nameField.getText();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(updateFrame, "Name cannot be empty.");
                return;
            }

            customerIDMap.remove(selectedCustomer);
            customerIDMap.put(newName, customerID);

            JOptionPane.showMessageDialog(updateFrame, "Customer updated successfully.");
            updateFrame.dispose();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(genderLabel);
        panel.add(genderField);
        panel.add(new JLabel()); // Empty cell
        panel.add(saveButton);

        updateFrame.add(panel);
        updateFrame.setVisible(true);
    }

    // Method to delete an existing customer
    public static void deleteCustomer(String selectedCustomer) {
        String customerID = customerIDMap.remove(selectedCustomer);
        if (customerID != null) {
            JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Customer not found.");
        }
    }

    // Method to set the initial customer map
    public static void setCustomerIDMap(Map<String, String> initialMap) {
        customerIDMap = initialMap;
    }
}