package com.techelevator.tenmo.UiTests.loginUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TenmoLoginHeader extends JPanel {
    private final int WIDTH = 700;
    private final int HEIGHT = 50;
//    private Color bgColor = new Color(9, 144, 53);
    private BufferedImage bgImg = null;

    public TenmoLoginHeader(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //get header background img
        try {
            bgImg = ImageIO.read(new File("Resources/TenmoLoginHeader.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawImage(bgImg, null, 0, 0);
    }
}
