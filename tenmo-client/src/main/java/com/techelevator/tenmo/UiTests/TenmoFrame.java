package com.techelevator.tenmo.UiTests;

import com.techelevator.tenmo.UiTests.loggedInUI.TenmoHome;
import com.techelevator.tenmo.UiTests.loginUI.TenmoLoginPanel;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class TenmoFrame extends JFrame{
    private final int WIDTH = 700;
    private final int HEIGHT = 600;
    private final String TITLE = "TEnmo";
    private TenmoLoginPanel loginPanel;
    private AuthenticatedUser user;

    //Set Up Frame
    public TenmoFrame(AuthenticationService authenticationService){
        loginPanel = new TenmoLoginPanel(authenticationService, this);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setTitle(TITLE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        add(loginPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
