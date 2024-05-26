package com.example.panels;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    public InventoryPanel() {
        setBackground(Color.ORANGE);
        add(new JLabel("Inventory Section", SwingConstants.CENTER));
    }
}
