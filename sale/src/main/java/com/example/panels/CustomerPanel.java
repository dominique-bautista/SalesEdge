package com.example.panels;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerPanel extends JPanel {
    private final JTextField searchBar;
    private JComboBox<String> searchFilter = null;
    private final JList<String> customerList;
    private final JTextArea customerDetails;
    private final Map<String, String> customerIDMap;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        customerIDMap = Manager.getCustomerNames();

        searchBar = new JTextField("Search...");
        searchBar.setPreferredSize(new Dimension(750, 30));
        searchBar.setFont(new Font("Lato", Font.PLAIN, 18));
        searchBar.setForeground(Color.GRAY);

        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (searchBar.getText().equals("Search...")) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Search...");
                }
            }
        });

        searchBar.addActionListener(e -> updateCustomerList(searchBar.getText(), (String) searchFilter.getSelectedItem()));
        searchFilter = new JComboBox<>(new String[]{"First Name", "Last Name", "Age", "Gender", "Phone", "Email","Street Address","City","Province","Postal Code","Country"});
        searchFilter.setFont(new Font("Lato", Font.PLAIN, 18));

        searchFilter.setPreferredSize(new Dimension(200, searchFilter.getPreferredSize().height));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchBar);
        searchPanel.add(new JLabel(" in "));
        searchPanel.add(searchFilter);

        add(searchPanel, BorderLayout.NORTH);

        customerList = new JList<>(new DefaultListModel<>());
        customerList.setFont(new Font("Lato", Font.PLAIN, 20));
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
                this.thumbColor = new Color(0xF47130);
            }
        });

        customerDetails = new JTextArea();
        customerDetails.setFont(new Font("Lato", Font.PLAIN, 20));
        customerDetails.setEditable(false);
        customerDetails.setText("Select a customer to view details.");

        JScrollPane customerDetailsScrollPane = new JScrollPane(customerDetails);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, customerListScrollPane, customerDetailsScrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        JPanel splitPanePanel = new JPanel(new BorderLayout());
        splitPanePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        splitPanePanel.add(splitPane, BorderLayout.CENTER);

        JButton createButton = createButton("Add");
        createButton.addActionListener(e -> {
            createCustomer(this);
            customerList.clearSelection();
            customerDetails.setText("Select a customer to view details.");
            populateListAndMap();
            revalidate();
            repaint();
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
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the customer: " + selectedCustomer + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(customerIDMap.get(selectedCustomer));
                    Manager.delete("customer", "customer_id", id);
                    customerList.clearSelection();
                    customerDetails.setText("Select a customer to view details.");
                    populateListAndMap();
                    revalidate();
                    repaint();
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

        add(splitPanePanel, BorderLayout.CENTER);
        updateCustomerList("", "Product ID");
        populateListAndMap();
        revalidate();
        repaint();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0xF4, 0x71, 0x30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Lato", Font.BOLD, 14));
        return button;
    }

    private void updateCustomerList(String searchQuery, String filter) {
        DefaultListModel<String> model = (DefaultListModel<String>) customerList.getModel();
        model.clear();

        if (searchQuery.isEmpty()) {
            populateListAndMap();
        } else {
            Map<String, String> filteredMap = new LinkedHashMap<>();
            customerIDMap.entrySet().stream()
                    .filter(entry -> entry.getKey().toLowerCase().contains(searchQuery.toLowerCase()))
                    .forEach(entry -> filteredMap.put(entry.getKey(), entry.getValue()));
            filteredMap.keySet().forEach(model::addElement);
        }
    }

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

