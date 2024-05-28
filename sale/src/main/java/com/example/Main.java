package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://192.168.1.9:3306/salesedge?useSSL=false&serverTimezone=UTC";
        String username = "dom";
        String password = "d0minic@123";

        try {
            // Load the MySQL Connector/J driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute a query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");

            // Process the result set
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Contact: " + resultSet.getString("contact"));
                System.out.println("Address: " + resultSet.getString("address"));
                System.out.println("Age: " + resultSet.getInt("age"));
                System.out.println("Gender: " + resultSet.getString("gender"));
                System.out.println("-----");
            }

            // Close the connections
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
