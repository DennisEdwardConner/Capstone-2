package com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels;

import com.techelevator.tenmo.UiTests.MyButton;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class PendingRequestsPanel extends JPanel implements ActionListener {

    private Color bgColor = new Color(0, 153, 102).darker();

    private JScrollPane transferListScrollPane;
    private MyButton acceptButton, denyButton, selectTransferButton;
    private JPanel transferPane;

    private AuthenticatedUser currentUser;
    private TransferService transferService = new TransferService("http://localhost:8080/");
    private UserService userService = new UserService("http://localhost:8080/");
    private AccountService accountService = new AccountService("http://localhost:8080/");

    private JPanel homePanel;
    private Transfer selectedTransaction;

    private boolean tooLittleMoney = false;

    public PendingRequestsPanel(AuthenticatedUser currentUser, JPanel homePanel){
        setBounds(10, 210, 370, 295);
        setBackground(bgColor);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.currentUser = currentUser;
        userService.setCurrentUser(currentUser);
        transferService.setCurrentUser(currentUser);
        accountService.setCurrentUser(currentUser);

        setUpScrollArea();
        setUpInputFields();

        this.homePanel = homePanel;

        setVisible(false);
    }

    private void setUpInputFields(){

        BufferedImage historyPanelBgImg = null;
        try {
            historyPanelBgImg = ImageIO.read(new File("Resources/pendingPanel.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finalHistoryPanelBgImg = historyPanelBgImg;
        JPanel inputFieldPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2D = (Graphics2D) g;

                g2D.drawImage(finalHistoryPanelBgImg, 0, 0, 220, 295, this);


                if(transferService.getAllPendingTransfers().length == 0){
                    return;
                }else if(selectedTransaction == null){
                    selectedTransaction = transferService.getAllPendingTransfers()[0];
                }

                //DRAW TEXT
                g2D.setColor(Color.WHITE);
                g2D.setFont(new Font(Font.SERIF, Font.BOLD, 30));

                g2D.drawString(String.valueOf(selectedTransaction.getTransfer_id()), 60, 45);
                g2D.drawString(selectedTransaction.getUsername_to(), 60, 88);

                g2D.setFont(new Font(Font.SERIF, Font.BOLD, 20));
                g2D.drawString(selectedTransaction.getAmount().toString(), 135, 125);

                if(tooLittleMoney){
                    g2D.setColor(new Color(255, 135, 162));
                    g2D.setFont(new Font(null, Font.PLAIN, 10));
                    g2D.drawString("Cannot Approve More Money Than You Have", 10, 150);
                }
            }
        };
        inputFieldPanel.setLayout(null);
        inputFieldPanel.setPreferredSize(new Dimension(220, 295));
        inputFieldPanel.setBackground(bgColor.darker());
        inputFieldPanel.setBorder(null);

        createAcceptDenyButtons();
        inputFieldPanel.add(acceptButton);
        inputFieldPanel.add(denyButton);

        add(inputFieldPanel, BorderLayout.EAST);
    }

    private void createAcceptDenyButtons(){
        acceptButton = new MyButton("APPROVE");
        acceptButton.setBackground(new Color(174, 245, 176));
        acceptButton.setForeground(new Color(14, 163, 63));
        acceptButton.setHoverBackgroundColor(bgColor);
        acceptButton.setFont(new Font(Font.SERIF, Font.BOLD, 35));
        acceptButton.setBounds(3, 170, 218, 50);
        acceptButton.setFocusable(false);
        acceptButton.setBorderPainted(false);
        acceptButton.setFocusPainted(false);
        acceptButton.addActionListener(this);
        acceptButton.setPressedBackgroundColor(new Color(14, 163, 63));
        acceptButton.setActionCommand("approve");

        denyButton = new MyButton("DENY");
        denyButton.setBackground(new Color(255, 174, 174));
        denyButton.setForeground(new Color(194, 15, 73));
        denyButton.setHoverBackgroundColor(bgColor);
        denyButton.setFont(new Font(Font.SERIF, Font.BOLD, 35));
        denyButton.setBounds(3, 225, 218, 50);
        denyButton.setFocusable(false);
        denyButton.setBorderPainted(false);
        denyButton.setFocusPainted(false);
        denyButton.addActionListener(this);
        denyButton.setPressedBackgroundColor(new Color(194, 15, 73));
        denyButton.setActionCommand("deny");
    }

    private void setUpScrollArea(){
        transferPane = new JPanel();
        transferPane.setPreferredSize(new Dimension(150, 295));
        transferPane.setLayout(null);
        transferPane.setBackground(bgColor);

        Transfer[] transferList = transferService.getAllPendingTransfers();

        int newY = 0;
        for(Transfer transfer : transferList){
            MyButton transferButton = new MyButton(String.valueOf(transfer.getTransfer_id()));
            transferButton.setBackground(bgColor);
            transferButton.setForeground(Color.yellow.darker());
            transferButton.setHoverBackgroundColor(bgColor.darker());
            transferButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
            transferButton.setBounds(0, newY, 150, 30);
            transferButton.setFocusable(false);
            transferButton.setBorderPainted(false);
            transferButton.setFocusPainted(false);
            transferButton.addActionListener(this);
            transferButton.setPressedBackgroundColor(new Color(14, 163, 63));
            transferButton.setActionCommand("transfer " + transfer.getTransfer_id());
            newY += 30;

            transferPane.add(transferButton);
        }
        transferPane.setPreferredSize(new Dimension(150, newY));

        transferListScrollPane = new JScrollPane(transferPane);
        transferListScrollPane.setBorder(null);
        transferListScrollPane.setPreferredSize(new Dimension(150, 295));
        transferListScrollPane.remove(transferListScrollPane.getVerticalScrollBar());
        transferListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(transferListScrollPane, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MyButton clickedButton = (MyButton) e.getSource();

        if(e.getActionCommand().contains("transfer")) {
            Transfer[] transferList = transferService.getAllPendingTransfers();
            String selectedTransferID = e.getActionCommand().split(" ")[1];

            for (int i = 0; i < transferList.length; i++) {
                if (selectedTransferID.equals(String.valueOf(transferList[i].getTransfer_id()))) {
                    selectedTransaction = transferList[i];
                    selectTransferButton = (MyButton) e.getSource();
                }
            }
        }
        else if(e.getActionCommand().equals("approve")){
            if (selectedTransaction.getAmount().doubleValue() > accountService.getAccountBalance().doubleValue()) {
                tooLittleMoney = true;
                repaint();
                return;
            }

            transferService.approveSend(selectedTransaction);
            transferService.updateTransferStatus(selectedTransaction, 2);
            transferPane.remove(selectTransferButton);

        }else if(e.getActionCommand().equals("deny")){
            transferService.updateTransferStatus(selectedTransaction, 3);
            transferPane.remove(selectTransferButton);
        }

        homePanel.repaint();
        repaint();
    }
}