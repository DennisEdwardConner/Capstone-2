package com.techelevator.tenmo.UiTests.loggedInUI.Entities;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class FlyingMoney{

    private BufferedImage moneyImg;

    private int x, y, degreesRotation;
    private boolean dead = false;

    public FlyingMoney(){
        try {
            moneyImg = ImageIO.read(new File("Resources/scaledDownFlyingMoney.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        x = 165;
        y = 15;
        degreesRotation = 0;

        startMovementUpdateThread();
    }

    public void draw(Graphics2D g2D){
        g2D.drawImage(moneyImg, null, x, y);
    }


    public void startMovementUpdateThread(){
        Thread movementThread = new Thread(){
            public void run(){
                while(!dead){
                    try {
                        sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    double yMovementCoeficient = Math.sin(System.nanoTime()/100_000_000);
                    int yDiff = (int) (yMovementCoeficient * 2);

                    y += yDiff;
                    x++;

                    if(x >= 700) {
                        dead = true;
                        FlyingMoneyController.moneyList.remove(this);
                    }
                }
            }
        };

        movementThread.start();
    }
}
