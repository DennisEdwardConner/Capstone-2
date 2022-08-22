package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.model.AuthenticatedUser;

import javax.swing.*;
import java.awt.*;

public class TenmoHomeFrame extends JFrame {
    private final int WIDTH = 700;
    private final int HEIGHT = 600;
    private final String TITLE = "TEnmo Home";

    private AuthenticatedUser user;

    private TenmoHomePane homePane;

    public TenmoHomeFrame(AuthenticatedUser user){
        this.user = user;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setTitle(TITLE);

        homePane = new TenmoHomePane(user);
        add(homePane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
