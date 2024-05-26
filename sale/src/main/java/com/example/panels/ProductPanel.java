package com.example.panels;

import javax.swing.*;
import java.awt.*;

public class ProductPanel extends JPanel {
    public ProductPanel() {
        setBackground(Color.MAGENTA);
        add(new JLabel("Product Section", SwingConstants.CENTER));
    }
}
