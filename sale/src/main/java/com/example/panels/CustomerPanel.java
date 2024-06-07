package com.example.panels;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
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

        customerListScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0xF47130); // Set the color of the scrollbar thumb
            }
        });

        // Create the customer details area
        customerDetails = new JTextArea();
        customerDetails.setFont(new Font("Lato", Font.PLAIN, 18));
        customerDetails.setEditable(false);
        customerDetails.setText("Select a customer to view details.");

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
        JButton createButton = createButton("Add");
        createButton.addActionListener(e -> {
            createCustomer(this);
            // Deselect any selected item in the list
            customerList.clearSelection();

            // Clear the details panel
            customerDetails.setText("Select a customer to view details.");

            // Optionally, refresh the list to ensure it's up to date
            populateListAndMap();
            revalidate();
            repaint();
            // Refresh the customer list
        });

        JButton updateButton = createButton("Edit");
        updateButton.addActionListener(e -> {
            String selectedCustomer = customerList.getSelectedValue();
            if (selectedCustomer != null) {
                updateCustomer(this, customerIDMap.get(selectedCustomer));
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
                    Manager.delete("customer", "customer_id", id);
                    // Deselect any selected item in the list
                    customerList.clearSelection();

                    // Clear the details panel
                    customerDetails.setText("Select a customer to view details.");

                    // Optionally, refresh the list to ensure it's up to date
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

        if (searchQuery.isEmpty()) {
            // If a search query is empty, re-populate the list in ascending order of ID
            populateListAndMap();
        } else {
            // Use a linked hash map to maintain the order of insertion
            Map<String, String> filteredMap = new LinkedHashMap<>();

            // Filter the map based on the search query and maintain the order of IDs
            customerIDMap.entrySet().stream()
                    .filter(entry -> entry.getKey().toLowerCase().contains(searchQuery.toLowerCase()))
                    .forEach(entry -> filteredMap.put(entry.getKey(), entry.getValue()));

            // Add the filtered and ordered names to the list model
            filteredMap.keySet().forEach(model::addElement);
        }
    }



    // Method to display customer details
    private void showCustomerDetails(String customerName) {
        String customerID = customerIDMap.get(customerName);
        String details = Manager.getCustomerDetails(customerID);
        customerDetails.setText(details);
    }

    public void createCustomer(JComponent pan) {
        JFrame createFrame = new JFrame("Create Customer");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(500, 600);
        createFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Lato", Font.PLAIN, 20);
        Font textFieldFont = new Font("Lato", Font.PLAIN, 15);

        JTextField firstNameField = Manager.createLabeledTextField("First Name:", panel, labelFont, textFieldFont);
        JTextField lastNameField = Manager.createLabeledTextField("Last Name:", panel, labelFont, textFieldFont);
        JTextField ageField = Manager.createLabeledTextField("Age:", panel, labelFont, textFieldFont);
        JTextField genderField = Manager.createLabeledTextField("Gender:", panel, labelFont, textFieldFont);
        JTextField phoneField = Manager.createLabeledTextField("Phone:", panel, labelFont, textFieldFont);
        JTextField emailField = Manager.createLabeledTextField("Email:", panel, labelFont, textFieldFont);
        JTextField streetAddressField = Manager.createLabeledTextField("Street Address:", panel, labelFont,
                textFieldFont);
        JTextField cityField = Manager.createLabeledTextField("City:", panel, labelFont, textFieldFont);
        JTextField provinceField = Manager.createLabeledTextField("Province:", panel, labelFont, textFieldFont);
        JTextField postalCodeField = Manager.createLabeledTextField("Postal Code:", panel, labelFont, textFieldFont);
        JTextField countryField = Manager.createLabeledTextField("Country:", panel, labelFont, textFieldFont);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(0xF47130));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Lato", Font.BOLD, 14));

        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = true;
            JTextField[] textFields = { firstNameField, lastNameField, ageField, genderField, phoneField, emailField,
                    streetAddressField, cityField, provinceField, postalCodeField, countryField };
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
                preparedStatement.setString(1, firstNameField.getText()); // fname
                preparedStatement.setString(2, lastNameField.getText()); // lname
                try {
                    preparedStatement.setInt(3, Integer.parseInt(ageField.getText())); // age
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(createFrame, "Invalid age format. Please enter a numeric value.");
                    return;
                }
                preparedStatement.setString(4, genderField.getText()); // gender
                preparedStatement.setString(5, phoneField.getText()); // phone
                preparedStatement.setString(6, emailField.getText()); // email
                preparedStatement.setString(7, streetAddressField.getText()); // street_address
                preparedStatement.setString(8, cityField.getText()); // city
                preparedStatement.setString(9, provinceField.getText()); // province
                preparedStatement.setString(10, postalCodeField.getText()); // postal_code
                preparedStatement.setString(11, countryField.getText()); // country
                preparedStatement.executeUpdate(); // Execute the statement
                createFrame.dispose();

            } catch (SQLException c) {
                c.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + c.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(null, "Customer created successfully.");
            pan.revalidate(); // Revalidate the
            pan.repaint();
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }

    public void updateCustomer(JComponent pan, String cus) {
        JFrame createFrame = new JFrame("Update Customer");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(500, 600);
        createFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Lato", Font.PLAIN, 20);
        Font textFieldFont = new Font("Lato", Font.PLAIN, 15);

        JTextField firstNameField = Manager.updateLabeledTextField("First Name:", panel, labelFont, textFieldFont, "customer", "first_name", cus, "customer_id");
        JTextField lastNameField = Manager.updateLabeledTextField("Last Name:", panel, labelFont, textFieldFont, "customer", "last_name", cus, "customer_id");
        JTextField ageField = Manager.updateLabeledTextField("Age:", panel, labelFont, textFieldFont, "customer", "age", cus, "customer_id");
        JTextField genderField = Manager.updateLabeledTextField("Gender:", panel, labelFont, textFieldFont, "customer", "gender", cus, "customer_id");
        JTextField phoneField = Manager.updateLabeledTextField("Phone:", panel, labelFont, textFieldFont, "customer", "phone", cus, "customer_id");
        JTextField emailField = Manager.updateLabeledTextField("Email:", panel, labelFont, textFieldFont, "customer", "email", cus, "customer_id");
        JTextField streetAddressField = Manager.updateLabeledTextField("Street Address:", panel, labelFont, textFieldFont, "customer", "street_address", cus, "customer_id");
        JTextField cityField = Manager.updateLabeledTextField("City:", panel, labelFont, textFieldFont, "customer", "city", cus, "customer_id");
        JTextField provinceField = Manager.updateLabeledTextField("Province:", panel, labelFont, textFieldFont, "customer", "province", cus, "customer_id");
        JTextField postalCodeField = Manager.updateLabeledTextField("Postal Code:", panel, labelFont, textFieldFont, "customer", "postal_code", cus, "customer_id");
        JTextField countryField = Manager.updateLabeledTextField("Country:", panel, labelFont, textFieldFont, "customer", "country", cus, "customer_id");

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(0xF47130));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Lato", Font.BOLD, 14));

        saveButton.addActionListener(e -> {
            boolean allFieldsFilled = true;
            JTextField[] textFields = { firstNameField, lastNameField, ageField, genderField, phoneField, emailField,
                    streetAddressField, cityField, provinceField, postalCodeField, countryField };
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
                String sqlInsert = "UPDATE `customer` SET `first_name` =?, `last_name` =?, `age` =?, `gender` =?, `email` =?, `phone` =?, `street_address` =?, `city` =?, `province` =?, `postal_code` =?, `country` =? WHERE `customer_id` =?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                preparedStatement.setString(1, firstNameField.getText()); // fname
                preparedStatement.setString(2, lastNameField.getText()); // lname
                try {
                    preparedStatement.setInt(3, Integer.parseInt(ageField.getText())); // age
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(createFrame, "Invalid age format. Please enter a numeric value.");
                    return;
                }
                preparedStatement.setString(4, genderField.getText()); // gender
                preparedStatement.setString(5, emailField.getText()); // email
                preparedStatement.setString(6, phoneField.getText()); // phone
                preparedStatement.setString(7, streetAddressField.getText()); // street_address
                preparedStatement.setString(8, cityField.getText()); // city
                preparedStatement.setString(9, provinceField.getText()); // province
                preparedStatement.setString(10, postalCodeField.getText()); // postal_code
                preparedStatement.setString(11, countryField.getText()); // country
                preparedStatement.setString(12, cus); // customer_id
                preparedStatement.executeUpdate(); // Execute the statement

                // Update the right side details immediately
                showCustomerDetails(cus);

                createFrame.dispose();
            } catch (SQLException c) {
                c.printStackTrace();
                JOptionPane.showMessageDialog(createFrame, "Error: " + c.getMessage());
                return;
            }

            JOptionPane.showMessageDialog(null, "Customer Updated successfully.");
            pan.revalidate();
            pan.repaint();
            populateListAndMap();
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        createFrame.add(panel);
        createFrame.setVisible(true);
    }




    private void populateListAndMap() {
        // Clear the list and map
        DefaultListModel<String> model = (DefaultListModel<String>) customerList.getModel();
        model.clear();
        customerIDMap.clear(); // Ensure the map is also cleared

        // Fetch the updated list of customers from a database
        Map<String, String> customers = Manager.getCustomerNamesOrderedByIDAsc();

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

