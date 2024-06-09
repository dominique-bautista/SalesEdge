package com.example;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Random;
import javax.swing.*;

public class LoginForm {

    // Method to authenticate user credentials against the database
    private static boolean authenticateUser(String username, String password) {
        String jdbcURL = "jdbc:mysql://localhost:3306/salesedge";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String query = "SELECT * FROM staff WHERE username = ? AND BINARY password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If the result set contains any rows, the credentials are valid
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getUserId(String username, String password) {
        String jdbcURL = "jdbc:mysql://localhost:3306/salesedge";
        String dbUser = "root";
        String dbPassword = "";
        String user;
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String query = "SELECT * FROM staff WHERE username = ? AND BINARY password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = resultSet.getString("staff_id");
                return user; // If the result set contains any rows, the credentials are valid
            }
            return ""; // If the result set contains any rows, the credentials are valid
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void on_start() {
        // Group 1: Initialize components
        // Create the main window frame
        MainFrame loginFrame = new MainFrame();
        // Create a label to display the logo text
        JLabel logoLabel = new JLabel();
        // Create a panel for the left side of the window
        JPanel leftPanel = new JPanel();
        // Create a panel for the login details on the right side
        JPanel loginPanel = new JPanel();
        // Create fields for username and password
        JLabel salesEdgeLabel = new JLabel("SalesEdge");
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        // Create a login button
        JButton loginButton = new JButton("Login");

        // Create a label for the register link
        JLabel registerLabel = new JLabel("Don't have an account? Register");

        // Calculate the maximum width and height for the window
        int maxWidth = loginFrame.getMaxWidth();
        int maxHeight = loginFrame.getMaxHeight();

        // Make the frame visible
        loginFrame.showMainFrame();

        // Group 3: Configure leftPanel
        leftPanel.setBackground(new Color(0xF47130));
        int leftPanelWidth = (int) (maxWidth * 0.58);
        leftPanel.setBounds(0, 0, leftPanelWidth, maxHeight);
        leftPanel.setLayout(new BorderLayout());

        // Group 4: Configure logoLabel
        String[] greetings = {
                "Welcome!", "Bienvenido!", "Bienvenue!", "Willkommen!", "Benvenuto!",
                "欢迎!", "Добро пожаловать!", "ようこそ!", "환영합니다!", "Bem-vindo!",
                "Καλώς ορίσατε!", "Selamat datang!", "Welkom!", "Tervetuloa!"
        };
        Random rand = new Random();
        String randomGreeting = greetings[rand.nextInt(greetings.length)];
        logoLabel.setText(randomGreeting);
        logoLabel.setIcon(loginFrame.getLogo());
        logoLabel.setVerticalAlignment(JLabel.TOP);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setHorizontalTextPosition(JLabel.CENTER);
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM);
        logoLabel.setFont(new Font("Inter", Font.BOLD, 48));

        // Group 5: Configure loginPanel
        loginPanel.setBackground(new Color(0xFDFDFD));
        int loginPanelWidth = maxWidth - leftPanelWidth;
        loginPanel.setBounds(leftPanelWidth, 0, loginPanelWidth, maxHeight);
        loginPanel.setLayout(new GridBagLayout());

        // Create fonts
        Font titleFont = new Font("Roboto", Font.BOLD, 48);
        Font font = new Font("Lato", Font.BOLD, 18);
        Font linkFont = new Font("Lato", Font.BOLD | Font.ITALIC, 18);

        // Set font for salesEdge label
        salesEdgeLabel.setFont(titleFont);
        salesEdgeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Set font for username label and field
        usernameLabel.setFont(font);
        usernameField.setFont(font);
        // Set font for password label and field
        passwordLabel.setFont(font);
        passwordField.setFont(font);
        // Set font for login button
        loginButton.setFont(font);

        // Set font and style for register label
        registerLabel.setFont(linkFont);
        registerLabel.setForeground(new Color(0xE7723C));
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setOpaque(false);
        registerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Add mouse listener to the register label
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Dispose the login frame and open the register form
                loginFrame.getMainFrame().dispose();
                com.example.RegisterForm.on_start();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);

        // Add salesEdge label
        loginPanel.add(salesEdgeLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add username label and field
        loginPanel.add(usernameLabel, gbc);
        gbc.gridy++;
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginPanel.add(usernameField, gbc);

        gbc.gridy++;
        // Add password label and field
        loginPanel.add(passwordLabel, gbc);
        gbc.gridy++;
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginPanel.add(passwordField, gbc);

        // Add focus listeners to change border color on focus
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                usernameField.setBorder(BorderFactory.createLineBorder(new Color(0xF47130), 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createLineBorder(new Color(0xF47130), 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        });

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        loginButton.setBackground(new Color(0xF47130));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Authenticate user against the database
            if (authenticateUser(username, password)) {
                DashBoard.initializeDashboard(loginFrame.getMainFrame(), getUserId(username, password));
            } else {
                JOptionPane.showMessageDialog(loginFrame.getMainFrame(), "Invalid username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the register label
        gbc.gridy++;
        gbc.insets = new Insets(15, 25, 5, 5);
        loginPanel.add(registerLabel, gbc);

        // Group 6: Assembly
        leftPanel.add(logoLabel, BorderLayout.CENTER);
        loginFrame.getMainFrame().add(leftPanel, BorderLayout.WEST);
        loginFrame.getMainFrame().add(loginPanel, BorderLayout.CENTER);
        loginFrame.showMainFrame();
    }

    public static void main(String[] args) {
        on_start();
    }
}
