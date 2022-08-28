package com.techelevator.tenmo.UiTests;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class MyButton extends JButton {

    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    private boolean bannerButton = false;

    public MyButton() {
        this(null);
    }

    public MyButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
    }

    public MyButton(boolean bannerButton){
        this(null);
        this.bannerButton = bannerButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        if (getModel().isPressed()) {
            g2D.setColor(pressedBackgroundColor);
        } else if (getModel().isRollover()) {
            g2D.setColor(hoverBackgroundColor);
        } else {
            g2D.setColor(getBackground());
        }

        if(!bannerButton) {
            g2D.fillRect(0, 0, getWidth(), getHeight());
        }

        if(bannerButton){
            final double ROTATION_RADIANS_FOR_45_DEGREES = 0.785398;
            AffineTransform oldTransform = g2D.getTransform();

            g2D.setColor(new Color(0, 0, 0 ,0 ));

            g2D.rotate(ROTATION_RADIANS_FOR_45_DEGREES,  getWidth()/2, getHeight()/2);

            g2D.fillRect(0, 0, getWidth(), getHeight());

            g2D.setTransform(oldTransform);
        }

        super.paintComponent(g2D);

    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }

    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}
