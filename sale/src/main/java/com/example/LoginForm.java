package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;


public class LoginForm {
    public static void main(String[] args) {

        ImageIcon logo =  new ImageIcon("sale\\src\\main\\resources\\SaleEge.png");
        JFrame loginFrame = new JFrame();
        JLabel leftLabel = new JLabel();
        JPanel logoPanel = new JPanel();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int maxWidth = (int) (screenSize.width * .75);
        int maxHeight = (int) (screenSize.height * .75);

        loginFrame.setSize(maxWidth, maxHeight);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setIconImage(logo.getImage());
        loginFrame.setVisible(true);
        
        
        
        logoPanel.setBackground(new Color(0xF47130));
        logoPanel.setBounds(0, 0,(int) (maxWidth*.66), maxHeight);
        logoPanel.setLayout(new BorderLayout());
        loginFrame.add(logoPanel);
        
        leftLabel.setText("Welcome");
        leftLabel.setIcon(logo);
        leftLabel.setVerticalAlignment(JLabel.TOP);
        leftLabel.setHorizontalAlignment(JLabel.CENTER);
        leftLabel.setHorizontalTextPosition(JLabel.CENTER);
        leftLabel.setVerticalTextPosition(JLabel.BOTTOM);
        leftLabel.setFont(new Font("Inter",Font.BOLD, 14));

        logoPanel.add(leftLabel);
    }
}
