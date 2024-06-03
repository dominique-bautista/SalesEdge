package com.example.panels;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Manager {

    // Method to create a new label and field and add to panel
    public static JTextField labelField(String container, JPanel panel) {
        JLabel label = new JLabel(container);
        JTextField textField = new JTextField();
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/salesedge";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    public static void delete(String tableName, String columnName, int id, JComponent customerPanelInstance) {
        try (Connection conn = Manager.getConnection()) {
            int maxId;
            // Disable AUTO_INCREMENT
            String removeIncrement = "ALTER TABLE " + tableName + " MODIFY COLUMN " + columnName + " INT NOT NULL";
            PreparedStatement preparedStatement = conn.prepareStatement(removeIncrement);
            preparedStatement.executeUpdate();

            // Delete the row with the ID to be deleted
            String deleteSql = "DELETE FROM "+tableName+" WHERE "+columnName+" = " +id;
            preparedStatement = conn.prepareStatement(deleteSql);
            preparedStatement.executeUpdate();

            // Find the maximum ID currently in use
            String maxID = "SELECT MAX("+columnName+") AS max_id FROM "+tableName;
            preparedStatement = conn.prepareStatement(maxID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                maxId = rs.getInt("max_id");
            } else {
                // Handle the case where no rows are returned
                System.out.println("No rows found in the table.");
                return; // Exit the method or handle this case as needed
            }

            // Loop through and update IDs

            while (id < maxId) { // Assuming 4 is the new maximum ID after deletion
                String updateSql = "UPDATE "+tableName+" SET "+columnName+" ="+id+" WHERE "+columnName+" ="+(id+1);
                PreparedStatement pstmt = conn.prepareStatement(updateSql);
                pstmt.executeUpdate();
                id += 1;
            }

            // Re-enable AUTO_INCREMENT (if needed for future inserts)
            preparedStatement = conn.prepareStatement("ALTER TABLE "+tableName+" MODIFY COLUMN "+columnName+" INT NOT NULL AUTO_INCREMENT");
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
            customerPanelInstance.revalidate();
            customerPanelInstance.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get customer names from the database
    public static Map<String, String> getCustomerNames() {
        Map<String, String> customerMap = new HashMap<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT customer_id, first_name, last_name FROM customer";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("customer_id");
                String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                customerMap.put(name, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerMap;
    }

    // Method to get customer details by ID
    public static String getCustomerDetails(String customerID) {
        StringBuilder details = new StringBuilder();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM customer WHERE customer_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                details.append("ID: ").append(resultSet.getString("customer_id")).append("\n");
                details.append("First Name: ").append(resultSet.getString("first_name")).append("\n");
                details.append("Last Name: ").append(resultSet.getString("last_name")).append("\n");
                details.append("Age: ").append(resultSet.getInt("age")).append("\n");
                details.append("Gender: ").append(resultSet.getString("gender")).append("\n");
                details.append("Email: ").append(resultSet.getString("email")).append("\n");
                details.append("Phone: ").append(resultSet.getString("phone")).append("\n");
                details.append("Street Address: ").append(resultSet.getString("street_address")).append("\n");
                details.append("City: ").append(resultSet.getString("city")).append("\n");
                details.append("Province: ").append(resultSet.getString("province")).append("\n");
                details.append("Postal Code: ").append(resultSet.getString("postal_code")).append("\n");
                details.append("Country: ").append(resultSet.getString("country")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details.toString();
    }

    // Method to update an existing customer
    public static void updateCustomer(String selectedCustomer) {
        // String customerID = customerIDMap.get(selectedCustomer);

        // JFrame updateFrame = new JFrame("Update Customer");
        // updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // updateFrame.setSize(400, 300);
        // updateFrame.setLocationRelativeTo(null);

        // JPanel panel = new JPanel();
        // panel.setLayout(new GridLayout(7, 2, 10, 10));

        // JLabel nameLabel = new JLabel("Name:");
        // JTextField nameField = new JTextField(selectedCustomer);

        // JLabel emailLabel = new JLabel("Email:");
        // JTextField emailField = new
        // JTextField(selectedCustomer.toLowerCase().replace(" ", ".") +
        // "@example.com");

        // JLabel phoneLabel = new JLabel("Phone:");
        // JTextField phoneField = new JTextField("(123) 456-7890");

        // JLabel addressLabel = new JLabel("Address:");
        // JTextField addressField = new JTextField("123 Main St");

        // JLabel ageLabel = new JLabel("Age:");
        // JTextField ageField = new JTextField();

        // JLabel genderLabel = new JLabel("Gender:");
        // JTextField genderField = new JTextField();

        // JButton saveButton = new JButton("Save");
        // saveButton.addActionListener(e -> {
        // String newName = nameField.getText();
        // if (newName.isEmpty()) {
        // JOptionPane.showMessageDialog(updateFrame, "Name cannot be empty.");
        // return;
        // }

        // // customerIDMap.remove(selectedCustomer);
        // // customerIDMap.put(newName, customerID);

        // JOptionPane.showMessageDialog(updateFrame, "Customer updated successfully.");
        // updateFrame.dispose();
        // });

        // panel.add(nameLabel);
        // panel.add(nameField);
        // panel.add(emailLabel);
        // panel.add(emailField);
        // panel.add(phoneLabel);
        // panel.add(phoneField);
        // panel.add(addressLabel);
        // panel.add(addressField);
        // panel.add(ageLabel);
        // panel.add(ageField);
        // panel.add(genderLabel);
        // panel.add(genderField);
        // panel.add(new JLabel()); // Empty cell
        // panel.add(saveButton);

        // updateFrame.add(panel);
        // updateFrame.setVisible(true);
    }

    // Method to delete an existing customer
    public static void deleteCustomer(String selectedCustomer) {
        // // String customerID = customerIDMap.remove(selectedCustomer);
        // // if (customerID != null) {
        // JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
        // // } else {
        // JOptionPane.showMessageDialog(null, "Customer not found.");
        // }
    }

    // Method to set the initial customer map
    public static void setCustomerIDMap(Map<String, String> initialMap) {
        // customerIDMap = initialMap;
    }
}