# SalesEdge: A Retail Management System

## Overview
SalesEdge is a comprehensive retail management system designed to streamline operations and manage critical aspects of retail businesses. This system includes modules for managing employee information, customer data, product inventory, sales transactions, and supplier information. Developed as a project for the Information Management course at CvSU-CCAT Campus, SalesEdge leverages MySQL for database management.

## Database Schema
1. **Employee Account / Staff Information**
    - **Staff ID / Employee ID**: Unique identifier for each staff member.
    - **Name**: Staff member's first name and last name.
    - **Username**: Username for the staff member's account.
    - **Password**: Password for the staff member's account.
    - **Role**: Role of the staff member (e.g., Manager, Salesperson).
    - **Contact Information**: Staff member's email address and phone number.
    - **Address**: Staff member's residential address.

2. **Customer Information Table**
    - **Customer ID**: Unique identifier for each customer.
    - **Name**: Customer's first name and last name.
    - **Demographic**: Age and gender of the customer.
    - **Contact Information**: Customer's email address and phone number.
    - **Address**: Customer's residential address.

3. **Product Information / Inventory Table**
    - **Product ID**: Unique identifier for each product.
    - **Product Name**: Name of the product.
    - **Description**: Description of the product.
    - **Category**: Product category (e.g., Clothing, Electronics).
    - **Price**: Price of the product.
    - **Supplier**: Supplier of the product.
    - **Stock Level**: Quantity available of the product.
    - **Low Stock Alert**: Indicator for low stock levels.
      
4. **Sales Transaction Table**
    - **Transaction ID**: Unique identifier for each transaction.
    - **Customer ID**: Reference to the Customer table.
    - **Date**: Date of the transaction.
    - **Time**: Time of the transaction.
    - **Salesperson ID**: Optional reference to the Staff table.
    - **List of purchased items**: Details of products purchased (Product ID, quantity, price).
    - **Total Amount**: Total amount of the transaction.

## Technologies Used
- **Programming Language**: Java
- **Database**: MySQL

## Cloning and Setting Up SalesEdge

### Prerequisites
- Java Development Kit (JDK)
- MySQL Server
- An Integrated Development Environment (IDE) such as IntelliJ IDEA, Eclipse, or NetBeans

### Steps to Clone and Run SalesEdge

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/SalesEdge.git
   ```

2. **Navigate to the project directory:**
   ```bash
   cd SalesEdge
   ```

3. **Configure the database:**
   - Make sure your MySQL server is running.
   - Update the `com.example.CreateDatabaseAndTables` class with your MySQL database credentials if necessary.

4. **Create the database and tables:**
   - Open the project in your preferred IDE.
   - Locate and run the `com.example.CreateDatabaseAndTables` class to automatically create the database and tables.

5. **Run the application:**
   - Locate and run the `LoginForm.java` class to start the application.

6. **Access the application:**
   - The application will open as a Java Swing GUI.

## Project Team
- **Project Leader**: Shyrine Salvador
- **Team Members**:
  - Skyla Arra Tamio
  - Mariejoe Diamzon
  - Christian Dominique Bautista
  - Romjil Mejia

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements
This project was developed as part of the Information Management course at CvSU-CCAT Campus. Special thanks to our instructor and mentors for their guidance and support.
