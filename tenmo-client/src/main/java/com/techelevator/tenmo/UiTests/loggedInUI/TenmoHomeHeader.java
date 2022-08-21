package com.techelevator.tenmo.UiTests.loggedInUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TenmoHomeHeader extends JPanel {
    private final int WIDTH = 700;
    private final int HEIGHT = 50;

    private BufferedImage bgImg;

    public TenmoHomeHeader() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GREEN);

        try {
            bgImg = ImageIO.read(new File("Resources/TenmoHomeHeader.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g){
        g.drawImage(bgImg, 0, 0, WIDTH, HEIGHT, null);
    }
}
