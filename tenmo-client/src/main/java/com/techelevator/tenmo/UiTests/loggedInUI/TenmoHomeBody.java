package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.AccountService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TenmoHomeBody extends JPanel{
    private int WIDTH = 700;
    private int HEIGHT = 650;

    private BufferedImage bgImg;

    private AccountService accountService;

    public TenmoHomeBody(AuthenticatedUser currentUser){
        setPreferredSize(new Dimension());
        setBackground(Color.BLUE);

        try {
            bgImg = ImageIO.read(new File("Resources/TenmoHomeBody.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        accountService = new AccountService("http://localhost:8080/");
        accountService.setCurrentUser(currentUser);

    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(bgImg, 0, 0, this);

        Graphics2D g2D = (Graphics2D) g;

        paintAccountInfo(g2D);
    }

    private void paintAccountInfo(Graphics2D g2D){
        g2D.setColor(new Color(0, 153, 102).darker());

        final double ROTATION_RADIANS_FOR_45_DEGREES = 0.785398;
        AffineTransform oldTransform = g2D.getTransform();
        g2D.rotate(ROTATION_RADIANS_FOR_45_DEGREES, 350, 325);

        g2D.setFont(new Font(Font.SERIF, Font.BOLD, 60));
        g2D.drawString("BALANCE " + accountService.getAccountBalance().toString(), 70, 200);

        g2D.setTransform(oldTransform);
    }
}
