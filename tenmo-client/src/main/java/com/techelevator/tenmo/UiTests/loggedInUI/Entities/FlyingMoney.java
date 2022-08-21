package com.techelevator.tenmo.UiTests.loggedInUI.Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;;

public class FlyingMoney{

    private BufferedImage moneyImg;

    private int x, y;
    private int velocity;
    private boolean dead = false;

    public FlyingMoney(){
        try {
            moneyImg = ImageIO.read(new File("Resources/scaledDownFlyingMoney.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        velocity = 1;

        x = 165;
        y = 15;

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
                    x += velocity;

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
