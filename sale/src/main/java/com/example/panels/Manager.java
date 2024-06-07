package com.example.panels;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public static JTextField createLabeledTextField(String labelText, JPanel panel, Font labelFont, Font textFieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setFont(textFieldFont);


        Color orangeBorderColor = new Color(0xF47130);


        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createLineBorder(orangeBorderColor));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(UIManager.getBorder("TextField.border"));
            }
        });

        panel.add(textField);

        return textField;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/salesedge";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    public static void delete(String tableName, String columnName, int id) {
        try (Connection conn = Manager.getConnection()) {
            PreparedStatement preparedStatement;
            
            // Delete the row with the ID to be deleted
            String deleteSql = "DELETE FROM "+tableName+" WHERE "+columnName+" = " +id;
            preparedStatement = conn.prepareStatement(deleteSql);
            preparedStatement.executeUpdate();

            

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

    public static JTextField updateLabeledTextField(String labelText, JPanel panel, Font labelFont, Font textFieldFont,String tableName, String columnName, String customerID, String primeID) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label);
        String text = "";
        try(Connection con  = Manager.getConnection())
        {
            String sql = "SELECT * FROM " + tableName +" WHERE "+ primeID +" = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                text = resultSet.getString(columnName);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        JTextField textField = new JTextField(text);
        textField.setFont(textFieldFont);


        Color orangeBorderColor = new Color(0xF47130);


        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createLineBorder(orangeBorderColor));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(UIManager.getBorder("TextField.border"));
            }
        });

        panel.add(textField);

        return textField;
    }

    public static Map<String, String> getCustomerNamesOrderedByIDAsc() {
        Map<String, String> customerMap = new LinkedHashMap<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT customer_id, first_name, last_name FROM customer ORDER BY customer_id ASC";
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


}
