package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.model.AuthenticatedUser;

import javax.swing.*;
import java.awt.*;

public class TenmoHomePane extends JPanel {
    private int width = 700;
    private int height = 600;
    private AuthenticatedUser user;

    private TenmoHomeBody homeBody;
    private TenmoHomeHeader homeHeader;

    public TenmoHomePane(AuthenticatedUser user){
        this.user = user;

        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        homeBody = new TenmoHomeBody(user);
        homeHeader = new TenmoHomeHeader();

        add(homeHeader, BorderLayout.PAGE_START);
        add(homeBody, BorderLayout.CENTER);
    }

}
