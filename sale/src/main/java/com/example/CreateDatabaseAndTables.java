package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabaseAndTables {

        public static void main(String[] args) {
                String jdbcURL = "jdbc:mysql://localhost:3306/";
                String dbUser = "root";
                String dbPassword = "";
                String dbName = "salesedge";

                try {
                        // Step 1: Create Database
                        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                        Statement statement = connection.createStatement();
                        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + dbName;
                        statement.executeUpdate(createDatabase);
                        System.out.println("Database created successfully!");

                        // Step 2: Switch to the new database
                        statement.executeUpdate("use " + dbName);

                        // Step 3: Create Tables

                        // Employee Account / Staff Information
                        String createStaffTable = "CREATE TABLE IF NOT EXISTS staff (" +
                                        "staff_id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "first_name VARCHAR(50) NOT NULL, " +
                                        "last_name VARCHAR(50) NOT NULL, " +
                                        "username VARCHAR(50) NOT NULL, " +
                                        "password VARCHAR(50) NOT NULL, " +
                                        "role VARCHAR(50) NOT NULL, " +
                                        "email VARCHAR(100) NOT NULL, " +
                                        "phone VARCHAR(20) NOT NULL, " +
                                        "address VARCHAR(255) NOT NULL" +
                                        ");";
                        statement.executeUpdate(createStaffTable);

                        // Customer Information Table
                        String createCustomerTable = "CREATE TABLE IF NOT EXISTS customer (" +
                                        "customer_id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "first_name VARCHAR(50) NOT NULL, " +
                                        "last_name VARCHAR(50) NOT NULL, " +
                                        "age INT NOT NULL, " +
                                        "gender ENUM('Male', 'Female', 'Other') NOT NULL, " +
                                        "email VARCHAR(100) NOT NULL, " +
                                        "phone VARCHAR(20) NOT NULL, " +
                                        "street_address VARCHAR(255) NOT NULL, " +
                                        "city VARCHAR(50) NOT NULL, " +
                                        "province VARCHAR(50) NOT NULL, " +
                                        "postal_code VARCHAR(20) NOT NULL, " +
                                        "country VARCHAR(50) NOT NULL" +
                                        ");";
                        statement.executeUpdate(createCustomerTable);

                        // Product Inventory Table
                        String createProductInventoryTable = "CREATE TABLE IF NOT EXISTS product_inventory (" +
                                        "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "product_name VARCHAR(100) NOT NULL, " +
                                        "description TEXT NOT NULL, " +
                                        "category VARCHAR(50) NOT NULL, " +
                                        "price DECIMAL(10, 2) NOT NULL, " +
                                        "supplier VARCHAR(100) NOT NULL, " +
                                        "stock_level INT NOT NULL, " +
                                        "image_url VARCHAR(255), " +
                                        "low_stock_alert BOOLEAN NOT NULL" +
                                        ");";
                        statement.executeUpdate(createProductInventoryTable);

                        // Sales Transaction Table
                        String createSalesTable = "CREATE TABLE IF NOT EXISTS sales_transaction (" +
                                        "transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "customer_id INT NOT NULL, " +
                                        "transaction_date DATE NOT NULL, " +
                                        "transaction_time TIME NOT NULL, " +
                                        "salesperson_id INT, " +
                                        "total_amount DECIMAL(10, 2) NOT NULL, " +
                                        "FOREIGN KEY (customer_id) REFERENCES customer(customer_id), " +
                                        "FOREIGN KEY (salesperson_id) REFERENCES staff(staff_id)" +
                                        ");";
                        statement.executeUpdate(createSalesTable);

                        // Sales Items Table (part of Sales Transaction)
                        String createTransactionsTableSQL = "CREATE TABLE IF NOT EXISTS Transactions (" +
                                        "TransactionID INT AUTO_INCREMENT PRIMARY KEY," +
                                        "CustomerID INT NOT NULL," +
                                        "Date DATE NOT NULL," +
                                        "Time TIME NOT NULL," +
                                        "SalespersonID INT NOT NULL);";

                        String createTransactionItemsTableSQL = "CREATE TABLE IF NOT EXISTS TransactionItems (" +
                                        "ItemID INT AUTO_INCREMENT PRIMARY KEY," +
                                        "TransactionID INT," +
                                        "ProductID INT NOT NULL," +
                                        "ProductName VARCHAR(255) NOT NULL," +
                                        "Quantity INT NOT NULL," +
                                        "Price DECIMAL(10, 2) NOT NULL," +
                                        "FOREIGN KEY (TransactionID) REFERENCES Transactions(TransactionID));";
                        statement.executeUpdate(createTransactionsTableSQL);
                        statement.executeUpdate(createTransactionItemsTableSQL);

                        System.out.println("Tables created successfully!");

                        connection.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
