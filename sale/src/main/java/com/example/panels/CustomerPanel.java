package com.example.panels;

import java.util.List;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class CustomerPanel extends JPanel {
    private final JTextField searchBar;
    private final JList<String> customerList;
    private final JTextArea customerDetails;
    private final Map<String, String> customerIDMap; // Map to hold customer
    // names and their IDs

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Initialize the map of customer names and their IDs
        customerIDMap = Manager.getCustomerNames();
        
        // Set the shared customerIDMap in Manager

        // Create the search bar
        searchBar = new JTextField("Search...");
        searchBar.setPreferredSize(new Dimension(790, 30));
        searchBar.setFont(new Font("Lato", Font.PLAIN, 18));
        searchBar.setForeground(Color.GRAY);

        // Add a listener to clear the text field when focused
        searchBar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchBar.getText().equals("Search...")) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Search...");
                    // Show all customers when search is cleared
                }
            }
        });

        // Add a listener to handle the Enter key press
        searchBar.addActionListener(e -> updateCustomerList(searchBar.getText()));

        // Add the search bar to a panel with padding
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(searchBar);

        // Add the search panel to the top of the CustomerPanel
        add(searchPanel, BorderLayout.NORTH);

        // Create the customer list
        customerList = new JList<>(new DefaultListModel<>());
        customerList.setFont(new Font("Lato", Font.PLAIN, 18));
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a listener to handle selection events
        customerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCustomer = customerList.getSelectedValue();
                if (selectedCustomer != null) {
                    showCustomerDetails(selectedCustomer);
                }
            }
        });

        JScrollPane customerListScrollPane = new JScrollPane(customerList);
        customerListScrollPane.setPreferredSize(new Dimension(200, 0));

        // Create the customer details area
        customerDetails = new JTextArea();
        customerDetails.setFont(new Font("Lato", Font.PLAIN, 18));
        customerDetails.setEditable(false);

        JScrollPane customerDetailsScrollPane = new JScrollPane(customerDetails);

        // Create a split pane to hold the list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, customerListScrollPane,
                customerDetailsScrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        // Create a panel to hold the split pane and add padding around it
        JPanel splitPanePanel = new JPanel(new BorderLayout());
        splitPanePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the split pane
        splitPanePanel.add(splitPane, BorderLayout.CENTER);

        // Add the split pane panel to the CustomerPanel
        add(splitPanePanel, BorderLayout.CENTER);
        // Add the buttons for creating, updating, and deleting customers
        JButton createButton = createButton("Create");
        createButton.addActionListener(e -> {
            createCustomer();
            populateListAndMap();
            revalidate();
            repaint();
            // Refresh the customer list
        });

        JButton updateButton = createButton("Update");
        updateButton.addActionListener(e -> {
            String selectedCustomer = customerList.getSelectedValue();
            if (selectedCustomer != null) {
                Manager.updateCustomer(selectedCustomer);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a customer to update.");
            }
        });

        JButton deleteButton = createButton("Delete");
        deleteButton.addActionListener(e -> {
            String selectedCustomer = customerList.getSelectedValue();
            if (selectedCustomer != null) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete the customer: " + selectedCustomer + "?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(customerIDMap.get(selectedCustomer));
                    
                    Manager.delete("customer", "customer_id", id, this);
                    populateListAndMap();
                    revalidate();
                    repaint();
                    // Refresh the customer list
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        splitPanePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the split pane panel to the center of the CustomerPanel
        add(splitPanePanel, BorderLayout.CENTER);

        // Initialize the customer list
        updateCustomerList("");
        populateListAndMap();
        revalidate();
        repaint();
    }

    // Method to create a button with specified text and style
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0xF4, 0x71, 0x30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Lato", Font.BOLD, 14));
        return button;
    }

    // Method to update the customer list based on the search query
    private void updateCustomerList(String searchQuery) {
        DefaultListModel<String> model = (DefaultListModel<String>) customerList.getModel();
        model.clear();

        customerIDMap.keySet().stream()
                .filter(name -> name.toLowerCase().contains(searchQuery.toLowerCase()))
                .forEach(model::addElement);
    }

    // Method to display customer details
    private void showCustomerDetails(String customerName) {
        String customerID = customerIDMap.get(customerName);
        String details = Manager.getCustomerDetails(customerID);
        customerDetails.setText(details);
    }

    public static void createCustomer() {
        JFrame createFrame = new JFrame("Create Customer");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(400, 300);
        createFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));

        JTextField[] textFields = {
                Manager.labelField("First Name:", panel),
                Manager.labelField("Last Name:", panel),
                Manager.labelField("Age:", panel),
                Manager.labelField("Gender:", panel),
                Manager.labelField("Phone:", panel),
                Manager.labelField("Email:", panel),
                Manager.labelField("Street Address:", panel),
                Manager.labelField("City:", panel),
                Manager.labelField("Province:", panel),
                Manager.labelField("Postal Code:", panel),
                Manager.labelField("Country:", panel),
        };

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = true;
            for (JTextField textField : textFields) {
                if (textField.getText().trim().isEmpty()) {
                    allFieldsFilled = false;
                    break;
                }
            }
            if (!allFieldsFilled) {
                JOptionPane.showMessageDialog(createFrame, "All fields must be filled out.");
                return;
            }
            try (Connection connection = Manager.getConnection()) {
                String sqlInsert = "INSERT INTO `customer`(`first_name`, `last_name`, `age`, `gender`, `email`, `phone`, `street_address`, `city`, `province`, `postal_code`, `country`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                preparedStatement.setString(1, textFields[0].getText()); // fname
                preparedStatement.setString(2, textFields[1].getText()); // lname
                try {
                    preparedStatement.setInt(3, Integer.parseInt(textFields[2].getText())); // age
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(createFrame, "Invalid age format. Please enter a numeric value.");
                    return;
                }
                preparedStatement.setString(4, textFields[3].getText()); // gender
                preparedStatement.setString(5, textFields[4].getText()); // phone
                preparedStatement.setString(6, textFields[5].getText()); // email
                preparedStatement.setString(7, textFields[6].getText()); // street_address
                preparedStatement.setString(8, textFields[7].getText()); // city
                preparedStatement.setString(9, textFields[8].getText()); // province
                preparedStatement.setString(10, textFields[9].getText()); // postal_code
                preparedStatement.setString(11, textFields[10].getText()); // country
                preparedStatement.executeUpdate(); // Execute the statement
                JOptionPane.showMessageDialog(createFrame, "Customer created successfully.");
                createFrame.dispose();

            } catch (SQLException c) {
                c.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + c.getMessage());
                return;
            }
        });

        panel.add(new JLabel()); // Empty cell
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    private void populateListAndMap() {
        // Clear the list and map
        DefaultListModel<String> model = (DefaultListModel<String>) customerList.getModel();
        model.clear();

        // Fetch the updated list of customers from your database
        // Example: Assuming you have a method in your Manager class that fetches
        // customer names and IDs

        Map<String, String> customers = Manager.getCustomerNames();
        List<Map.Entry<String, String>> entryList = new ArrayList<>(customers.entrySet());

        // Reverse the list
        Collections.reverse(entryList);

        for (Map.Entry<String, String> entry : customers.entrySet()) {
            String name = entry.getKey();
            String id = entry.getValue();
            model.addElement(name);
            customerIDMap.put(name, id);
        }

        // Refresh the panel
        revalidate();
        repaint();
    }

}
