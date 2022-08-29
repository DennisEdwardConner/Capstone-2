package com.techelevator.tenmo.UiTests.loggedInUI;

import com.techelevator.tenmo.UiTests.MyButton;
import com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels.PendingRequestsPanel;
import com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels.PreviousTransactionsPanel;
import com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels.RequestTenmoPanel;
import com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels.SendTenmoPanel;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.AccountService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class TenmoHomeBody extends JPanel implements ActionListener {
    private int WIDTH = 700;
    private int HEIGHT = 650;

    private BufferedImage bgImg;

    private AccountService accountService;

    private AuthenticatedUser currentUser;

    PendingRequestsPanel requestsPanel;
    PreviousTransactionsPanel transactionsPanel;
    RequestTenmoPanel requestPanel;
    SendTenmoPanel sendPanel;


    public TenmoHomeBody(AuthenticatedUser currentUser){
        setPreferredSize(new Dimension());
        setLayout(null);
        setBackground(Color.BLUE);


        try {
            bgImg = ImageIO.read(new File("Resources/TenmoHomeBody.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        accountService = new AccountService("http://localhost:8080/");
        accountService.setCurrentUser(currentUser);
        this.currentUser = currentUser;

        setUpButtons();
        setUpPanels();
    }

    private void setUpPanels(){
        requestsPanel = new PendingRequestsPanel(currentUser, this);
        transactionsPanel = new PreviousTransactionsPanel(currentUser, this);
        requestPanel = new RequestTenmoPanel(currentUser, this);
        sendPanel = new SendTenmoPanel(currentUser, this);

        add(requestsPanel);
        add(transactionsPanel);
        add(requestPanel);
        add(sendPanel);
    }

    private void setUpButtons(){
        MyButton pastTransButton = new MyButton(true);
        pastTransButton.setBackground(new Color(0, 0, 0, 0));
        pastTransButton.setBounds(400, 60, 120, 80);
        pastTransButton.setActionCommand("pastTransfers");
        pastTransButton.addActionListener(this);
        pastTransButton.setBorderPainted(false);
        pastTransButton.setFocusable(false);
        add(pastTransButton);

        MyButton pendingReqButton = new MyButton(true);
        pendingReqButton.setBackground(new Color(0, 0, 0, 0));
        pendingReqButton.setBounds(510, 160, 120, 110);
        pendingReqButton.setActionCommand("pendingRequests");
        pendingReqButton.addActionListener(this);
        pendingReqButton.setBorderPainted(false);
        pendingReqButton.setFocusable(false);
        add(pendingReqButton);

        MyButton requestTenmoButton = new MyButton(true);
        requestTenmoButton.setBackground(new Color(0, 0, 0, 0));
        requestTenmoButton.setBounds(580, 90, 120, 110);
        requestTenmoButton.setActionCommand("requestTenmo");
        requestTenmoButton.addActionListener(this);
        requestTenmoButton.setBorderPainted(false);
        requestTenmoButton.setFocusable(false);
        add(requestTenmoButton);

        MyButton sendTenmoButton = new MyButton(true);
        sendTenmoButton.setBackground(new Color(0, 0, 0, 0));
        sendTenmoButton.setBounds(490, 0, 80, 80);
        sendTenmoButton.setActionCommand("sendTenmo");
        sendTenmoButton.addActionListener(this);
        sendTenmoButton.setBorderPainted(false);
        sendTenmoButton.setFocusable(false);
        add(sendTenmoButton);
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(bgImg, 0, 0, this);

        Graphics2D g2D = (Graphics2D) g;

        paintAccountInfo(g2D);

        g2D.setColor(new Color(215, 255, 128));
        g2D.setFont(new Font(Font.SERIF, Font.BOLD, 45));
        if(sendPanel.isVisible()){
            g2D.drawString("SEND", 130, 180);
        }else if(requestPanel.isVisible()){
            g2D.drawString("REQUEST", 80, 180);
        }else if(transactionsPanel.isVisible()){
            g2D.drawString("HISTORY", 80, 180);
        }else if(requestsPanel.isVisible()){
            g2D.drawString("PENDING", 80, 180);
        }


    }

    private void paintAccountInfo(Graphics2D g2D){
        g2D.setColor(new Color(0, 153, 102).darker());

        final double ROTATION_RADIANS_FOR_45_DEGREES = 0.785398;
        AffineTransform oldTransform = g2D.getTransform();
        g2D.rotate(ROTATION_RADIANS_FOR_45_DEGREES, 350, 325);

        g2D.setFont(new Font(Font.SERIF, Font.BOLD, 60));
        g2D.drawString("BALANCE $" + accountService.getAccountBalance().toString(), 75, 200);

        g2D.setTransform(oldTransform);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        remove(sendPanel);
        remove(requestsPanel);
        remove(requestPanel);
        remove(transactionsPanel);

        sendPanel = new SendTenmoPanel(currentUser, this);
        requestPanel = new RequestTenmoPanel(currentUser, this);
        requestsPanel = new PendingRequestsPanel(currentUser, this);
        transactionsPanel = new PreviousTransactionsPanel(currentUser, this);

        add(sendPanel);
        add(requestPanel);
        add(requestsPanel);
        add(transactionsPanel);

        if(actionCommand.equals("sendTenmo")) {
            setAllInvisible();
            sendPanel.setVisible(true);
        }
        else if(actionCommand.equals("requestTenmo")) {
            setAllInvisible();
            requestPanel.setVisible(true);
        }
        else if(actionCommand.equals("pendingRequests")) {
            setAllInvisible();
            requestsPanel.setVisible(true);
        }
        else if(actionCommand.equals("pastTransfers")) {
            setAllInvisible();
            transactionsPanel.setVisible(true);
        }

        repaint();

    }

    private void setAllInvisible(){
        sendPanel.setVisible(false);
        requestPanel.setVisible(false);
        requestsPanel.setVisible(false);
        transactionsPanel.setVisible(false);

    }
}
