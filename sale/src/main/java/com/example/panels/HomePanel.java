package com.example.panels;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("Hello, World!", SwingConstants.CENTER);
        label.setFont(new Font("Lato", Font.BOLD, 24));
        add(label);
    }
}
