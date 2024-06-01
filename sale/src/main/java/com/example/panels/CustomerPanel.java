package com.example.panels;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerPanel extends JPanel {
    private final JTextField searchBar;
    private final JList<String> customerList;
    private final JTextArea customerDetails;
    private final Map<String, String> customerIDMap; // Map to hold customer names and their IDs

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Initialize the map of customer names and their IDs
        customerIDMap = new HashMap<>();
        customerIDMap.put("John Doe", "101");
        customerIDMap.put("Jane Smith", "102");
        customerIDMap.put("Alice Johnson", "103");
        customerIDMap.put("Bob Brown", "104");
        customerIDMap.put("Charlie Davis", "105");
        customerIDMap.put("Diana Evans", "106");
        customerIDMap.put("Frank Green", "107");
        customerIDMap.put("Grace Harris", "108");
        customerIDMap.put("Henry James", "109");
        customerIDMap.put("Isla Kelly", "110");
        customerIDMap.put("Jack Lewis", "111");
        customerIDMap.put("Kara Martinez", "112");
        customerIDMap.put("Leo Nelson", "113");
        customerIDMap.put("Mia Owens", "114");
        customerIDMap.put("Noah Parker", "115");
        customerIDMap.put("Olivia Quinn", "116");
        customerIDMap.put("Paul Roberts", "117");
        customerIDMap.put("Quinn Sanchez", "118");
        customerIDMap.put("Rachel Turner", "119");
        customerIDMap.put("Sam Underwood", "120");
        customerIDMap.put("Shyrine Salvador", "121");
        customerIDMap.put("Sheena Salvador", "122");

        // Set the shared customerIDMap in CustomerManager
        CustomerManager.setCustomerIDMap(customerIDMap);

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
                    updateCustomerList(""); // Show all customers when search is cleared
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
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, customerListScrollPane, customerDetailsScrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        // Create a panel to hold the split pane and add padding around it
        JPanel splitPanePanel = new JPanel(new BorderLayout());
        splitPanePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the split pane
        splitPanePanel.add(splitPane, BorderLayout.CENTER);

        // Add the buttons for creating, updating, and deleting customers
        JButton createButton = createButton("Create");
        createButton.addActionListener(e -> CustomerManager.createCustomer());

        JButton updateButton = createButton("Update");
        updateButton.addActionListener(e -> {
            String selectedCustomer = customerList.getSelectedValue();
            if (selectedCustomer != null) {
                CustomerManager.updateCustomer(selectedCustomer);
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
                    CustomerManager.deleteCustomer(selectedCustomer);
                    updateCustomerList(""); // Refresh the customer list
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
    private void updateCustomerList(String query) {
        DefaultListModel<String> model = (DefaultListModel<String>) customerList.getModel();
        model.clear();

        if (query.isEmpty() || query.equals("Search...")) {
            for (String customer : customerIDMap.keySet()) {
                model.addElement(customer);
            }
        } else {
            for (String customer : customerIDMap.keySet()) {
                if (customer.toLowerCase().contains(query.toLowerCase())) {
                    model.addElement(customer);
                }
            }
        }
    }

    // Method to display customer details
    private void showCustomerDetails(String customer) {
        // Fetch and display details for the selected customer
        // This is a placeholder implementation.
        // Replace with actual details fetching logic.
        String customerID = customerIDMap.get(customer);
        customerDetails.setText("Customer Information:\n\n" +
                "Customer ID: " + customerID + "\n" +
                "Name: " + customer + "\n\n" +
                "Contact Information:\n" +
                "Email Address: " + customer.toLowerCase().replace(" ", ".") + "@example.com\n" +
                "Phone Number: (123) 456-7890\n\n" +
                "Address:\n" +
                "Street Address: 123 Main St\n" +
                "City: Any-town\n" +
                "State/Province: State\n" +
                "Postal Code: 12345\n" +
                "Country: Country\n" +
                "Age: \n" +
                "Gender: ");
    }
}