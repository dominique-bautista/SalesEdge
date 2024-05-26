// testing lang toh


package com.example;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;


public class MainFrame  {
    private final Toolkit tk = Toolkit.getDefaultToolkit();
    private final Dimension ssize = tk.getScreenSize(); // screen size getter
    private final int maxWidth = (int) (ssize.width * .75);

    public ImageIcon getLogo() {
        return logo;
    }

    private final ImageIcon logo = new ImageIcon("sale\\src\\main\\resources\\SaleEge.png");
    private final int maxHeight = (int) (ssize.height * .75);

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }


    private final JFrame mainFrame;

    public MainFrame() {
        mainFrame = new JFrame();
        mainFrame.setSize(maxWidth, maxHeight);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setIconImage(logo.getImage());
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setLocationRelativeTo(null);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void showMainFrame() {
        mainFrame.setVisible(true);
    }
}
