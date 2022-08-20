package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.model.AuthenticatedUser;

import javax.swing.*;
import java.awt.*;

public class TenmoHome extends JPanel {
    private int width = 700;
    private int height = 600;
    private AuthenticatedUser user;

    private TenmoHomeBody homeBody;

    public TenmoHome(AuthenticatedUser user){
        this.user = user;
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        homeBody = new TenmoHomeBody();
        add(homeBody, BorderLayout.CENTER);
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(0, 0, width, height);
    }
}
