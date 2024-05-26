package com.example.panels;

import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel {
    public CartPanel() {
        setBackground(Color.YELLOW);
        add(new JLabel("Cart Section", SwingConstants.CENTER));
    }
}
