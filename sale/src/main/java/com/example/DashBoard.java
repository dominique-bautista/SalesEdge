package com.example;

import javax.swing.*;

public class DashBoard {
    public static void initializeDashboard(JFrame loginFrame) {
        loginFrame.dispose();
        MainFrame dashboardFrame = new MainFrame();
        JLabel label = new JLabel("Test");
        
        dashboardFrame.getMainFrame().add(label);
        dashboardFrame.showMainFrame();
    }

    public static void main(String[] args) {
        
    }
}