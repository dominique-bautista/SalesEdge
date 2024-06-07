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
            statement.executeUpdate("use "+ dbName);

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
                    "brand VARCHAR(50), " +
                    "color VARCHAR(30), " +
                    "size VARCHAR(30), " +
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
            String createSalesItemsTable = "CREATE TABLE IF NOT EXISTS sales_items (" +
                    "sales_item_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "transaction_id INT NOT NULL, " +
                    "product_id INT NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, " +
                    "FOREIGN KEY (transaction_id) REFERENCES sales_transaction(transaction_id), " +
                    "FOREIGN KEY (product_id) REFERENCES product_inventory(product_id)" +
                    ");";
            statement.executeUpdate(createSalesItemsTable);

            // Supplier Information Table
            String createSupplierTable = "CREATE TABLE IF NOT EXISTS supplier (" +
                    "supplier_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "products_supplied VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "phone VARCHAR(20) NOT NULL, " +
                    "address VARCHAR(255) NOT NULL" +
                    ");";
            statement.executeUpdate(createSupplierTable);

            System.out.println("Tables created successfully!");

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
