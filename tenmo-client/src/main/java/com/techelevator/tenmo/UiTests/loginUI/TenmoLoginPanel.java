package com.techelevator.tenmo.UiTests.loginUI;

import com.techelevator.tenmo.services.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class TenmoLoginPanel extends JPanel {
    private final int WIDTH = 700;
    private final int HEIGHT = 600;
    //private BufferedImage bgImage;
    private AuthenticationService authenticationService;
    public TenmoLoginPanel(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setUpLoginFramePanel();
    }

    //Create and add all child containers in layout for login
    private void setUpLoginFramePanel() {
        BorderLayout frameLayout = new BorderLayout();
        setLayout(frameLayout);

        TenmoLoginHeader tenmoLoginHeader = new TenmoLoginHeader();
        add(tenmoLoginHeader, BorderLayout.PAGE_START);

        TenmoLoginBody tenmoLoginBody = new TenmoLoginBody(authenticationService);
        add(tenmoLoginBody, BorderLayout.CENTER);

    }
}
