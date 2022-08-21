package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.AccountService;

import javax.swing.*;
import java.awt.*;

public class TenmoHomeBody extends JPanel {
    private int WIDTH = 100;
    private int HEIGHT = 100;

    private AccountService accountService;

    public TenmoHomeBody(AuthenticatedUser currentUser){
        setBounds(0, 0, WIDTH, HEIGHT);
        //setBackground(Color.BLUE);

        accountService = new AccountService("http://localhost:8080/");
        accountService.setCurrentUser(currentUser);
    }

    public void paintComponents(Graphics g){
        g.setColor(Color.GREEN);
        g.drawString(accountService.getAccountBalance().toString(), 50, 50);
    }
}
