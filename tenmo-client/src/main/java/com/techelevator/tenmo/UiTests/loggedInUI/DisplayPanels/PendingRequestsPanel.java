package com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels;

import com.techelevator.tenmo.model.AuthenticatedUser;

import javax.swing.*;
import java.awt.*;

public class PendingRequestsPanel extends JPanel {

    private Color bgColor = new Color(0, 153, 102).darker();
    private AuthenticatedUser currentUser;

    public PendingRequestsPanel(AuthenticatedUser currentUser){
        setBounds(10, 210, 370, 295);
        setBackground(bgColor);

        this.currentUser = currentUser;

        setVisible(false);
    }
}
