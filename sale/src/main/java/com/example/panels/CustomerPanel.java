package com.example.panels;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {
    public CustomerPanel() {
        setBackground(Color.CYAN);
        add(new JLabel("Customer Section", SwingConstants.CENTER));
    }
}
