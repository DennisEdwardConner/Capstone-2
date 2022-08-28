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

public class PreviousTransactionsPanel extends JPanel implements ActionListener {

    private Color bgColor = new Color(0, 153, 102).darker();

    private JScrollPane transferListScrollPane;
    private JTextArea userListTextArea;

    private JTextField userIdTextField = new JTextField();
    private JTextField amountTextField = new JTextField();

    private AuthenticatedUser currentUser;
    private TransferService transferService = new TransferService("http://localhost:8080/");
    private UserService userService = new UserService("http://localhost:8080/");
    private AccountService accountService = new AccountService("http://localhost:8080/");

    private JPanel homePanel;
    private boolean tooLittleMoney = false;
    private boolean lettersInInput = false;
    private boolean invalidUserID = false;
    private boolean sendingToSelf = false;
    private boolean negativeSending = false;
    private boolean smallAmount = false;

    public PreviousTransactionsPanel(AuthenticatedUser currentUser, JPanel homePanel){
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
        createTextBouncerThread();

        BufferedImage historyPanelBgImg = null;
        try {
            historyPanelBgImg = ImageIO.read(new File("Resources/HistoryPanel.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finalHistoryPanelBgImg = historyPanelBgImg;
        JPanel inputFieldPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2D = (Graphics2D) g;

                g2D.drawImage(finalHistoryPanelBgImg, 0, 0, 220, 295, this);
            }
        };
        inputFieldPanel.setLayout(null);
        inputFieldPanel.setPreferredSize(new Dimension(220, 295));
        inputFieldPanel.setBackground(bgColor.darker());
        inputFieldPanel.setBorder(null);

        add(inputFieldPanel, BorderLayout.EAST);
    }


    private void setUpScrollArea(){
        JPanel transferPane = new JPanel();
        transferPane.setPreferredSize(new Dimension(150, 295));
        transferPane.setLayout(null);
        transferPane.setBackground(Color.yellow);

        Transfer[] transferList = transferService.getPreviousTransfers();

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
            transferButton.setActionCommand(String.valueOf(transfer.getTransfer_id()));
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


    private void createTextBouncerThread(){
        Thread textBouncer = new Thread(){
            long startTime;
            double elapsedTime = 0;
            private void bounceTextArea(Component component){
                elapsedTime += (double)(System.nanoTime() - startTime)/100_000;

                final int MAX_BOUNCE = 2;
                int bounceAdd = (int) (MAX_BOUNCE * Math.sin(System.nanoTime()/100_000_000));
                int newY = component.getY() + bounceAdd;

                component.setBounds(component.getX(), newY, component.getWidth(), component.getHeight());
            }

            public void run(){
                int userIdFieldDefaultY = 80;
                int amountFieldDefaultY = 165;
                while(true){
                    startTime = System.nanoTime();
                    try {
                        sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(userIdTextField.isFocusOwner())
                        bounceTextArea(userIdTextField);
                    else
                        userIdTextField.setBounds(userIdTextField.getX(), userIdFieldDefaultY, userIdTextField.getWidth(), userIdTextField.getHeight());

                    if(amountTextField.isFocusOwner())
                        bounceTextArea(amountTextField);
                    else
                        amountTextField.setBounds(amountTextField.getX(), amountFieldDefaultY, amountTextField.getWidth(), amountTextField.getHeight());

                }
            }
        };

        textBouncer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("send")){
            int userID;
            BigDecimal amount;

            if(userIdTextField.getText().contains(".")){
                invalidUserID = true;

                lettersInInput = false;
                tooLittleMoney = false;
                sendingToSelf = false;
                negativeSending = false;
                smallAmount = false;

                repaint();
                return;
            }

            //GET ALL INPUT ERRORS
            try {
                userID = Integer.parseInt(userIdTextField.getText());
                amount = BigDecimal.valueOf(Double.parseDouble(amountTextField.getText()));
            }catch(NumberFormatException formatException){
                lettersInInput = true;

                tooLittleMoney = false;
                invalidUserID = false;
                sendingToSelf = false;
                negativeSending = false;
                smallAmount = false;

                repaint();
                return;
            }

            if(amount.doubleValue() > accountService.getAccountBalance().doubleValue()){
                tooLittleMoney = true;

                lettersInInput = false;
                invalidUserID = false;
                sendingToSelf = false;
                negativeSending = false;
                smallAmount = false;

                repaint();
                return;
            }else if(accountService.getByUserId(userID) == null){
                invalidUserID = true;

                lettersInInput = false;
                tooLittleMoney = false;
                sendingToSelf = false;
                negativeSending = false;
                smallAmount = false;

                repaint();
                return;
            }else if(userID == currentUser.getUser().getId()){
                sendingToSelf = true;

                invalidUserID = false;
                lettersInInput = false;
                tooLittleMoney = false;
                negativeSending = false;
                smallAmount = false;

                repaint();
                return;
            }else if(amount.doubleValue() <= 0.0){
                negativeSending = true;

                sendingToSelf = false;
                invalidUserID = false;
                lettersInInput = false;
                tooLittleMoney = false;
                smallAmount = false;

                repaint();
                return;
            }else if(amount.doubleValue() < 0.01){
                smallAmount = true;

                negativeSending = false;
                sendingToSelf = false;
                invalidUserID = false;
                lettersInInput = false;
                tooLittleMoney = false;

                repaint();
                return;
            }


            //CREATE TRANSER AND SEND IT
            Transfer sendTransfer = new Transfer();
            sendTransfer.setTransfer_status_id(2);
            sendTransfer.setTransfer_type_id(2);
            sendTransfer.setAccount_from(accountService.getByUserId(currentUser.getUser().getId()).getId());
            sendTransfer.setAccount_to_id(accountService.getByUserId(userID).getId());
            sendTransfer.setAmount(amount);

            transferService.sendTEBucks(sendTransfer);

            tooLittleMoney = false;
            lettersInInput = false;
            invalidUserID = false;
            sendingToSelf = false;

            homePanel.repaint();
        }
    }
}
