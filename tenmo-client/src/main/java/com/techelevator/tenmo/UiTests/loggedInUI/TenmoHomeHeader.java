package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.UiTests.loggedInUI.Entities.FlyingMoneyController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TenmoHomeHeader extends JPanel {
    private final int WIDTH = 700;
    private final int HEIGHT = 50;

    private BufferedImage bgImg;

    private FlyingMoneyController moneyController;

    public TenmoHomeHeader() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        moneyController = new FlyingMoneyController();
        startRepaintThread();

        FlyingMoneyController.flyTenmo(3);

        try {
            bgImg = ImageIO.read(new File("Resources/TenmoHomeHeader.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g.drawImage(bgImg, 0, 0, WIDTH, HEIGHT, null);

        for(int i = 0; i < FlyingMoneyController.moneyList.size(); i++){
            FlyingMoneyController.moneyList.get(i).draw(g2D);
        }
//        int x = (getWidth() - money.getWidth()) / 2;
//        int y = (getHeight() - money.getHeight()) / 2;
//        AffineTransform at = new AffineTransform();
//        at.setToRotation(90, x + (money.getWidth() / 2), y + (money.getHeight() / 2));
//        at.translate(x, y);
//        g2D.setTransform(at);
//
//        g2D.drawImage(money, 50, 0, this);
    }

    public void startRepaintThread(){
        Thread repaint = new Thread(){
            public void run(){
                while(true){
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
            }
        };

        repaint.start();
    }
}
