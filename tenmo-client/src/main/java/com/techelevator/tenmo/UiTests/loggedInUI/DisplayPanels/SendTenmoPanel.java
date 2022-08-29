package com.techelevator.tenmo.UiTests.loggedInUI.DisplayPanels;

import com.techelevator.tenmo.UiTests.MyButton;
import com.techelevator.tenmo.UiTests.loggedInUI.Entities.FlyingMoneyController;
import com.techelevator.tenmo.UiTests.loggedInUI.TenmoHomeBody;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class SendTenmoPanel extends JPanel implements ActionListener {

    private Color bgColor = new Color(0, 153, 102).darker();

    private JScrollPane userListScrollPane;
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
    private boolean tenmoSent = false;
    public SendTenmoPanel(AuthenticatedUser currentUser, JPanel homePanel){
        setBounds(10, 210, 370, 295);
        setBackground(bgColor);
        setLayout(new BorderLayout());

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

        BufferedImage inputPanelBgImg = null;
        try {
            inputPanelBgImg = ImageIO.read(new File("Resources/SendTenmoPanel.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finalInputPanelBgImg = inputPanelBgImg;
        JPanel inputFieldPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2D = (Graphics2D) g;

                g2D.drawImage(finalInputPanelBgImg, 0, 0, 220, 295, this);

                //DRAW ERRORS
                g2D.setColor(new Color(255, 135, 162));
                if(tooLittleMoney){
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("Cannot Send More Money Than You Have", 5, 220);
                }else if(lettersInInput){
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("Cannot Have Letters In Fields", 35, 220);
                }else if(invalidUserID){
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("Invalid User ID", 75, 220);
                }else if(sendingToSelf){
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("Cannot Send Money To Yourself", 30, 220);
                }else if(negativeSending){
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("Amount Must Be Greater Than Zero", 20, 220);
                }else if(smallAmount){
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("Amount Must Be Greater Than One Cent", 7, 220);
                }else if(tenmoSent) {
                    g2D.setFont(new Font(null, Font.PLAIN, 11));
                    g2D.drawString("TEnmo Bucks sent successfully", 7, 220);
                }
            }
        };
        inputFieldPanel.setLayout(null);
        inputFieldPanel.setPreferredSize(new Dimension(220, 295));
        inputFieldPanel.setBackground(bgColor);
        inputFieldPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        userIdTextField.setBackground(bgColor.darker());
        userIdTextField.setForeground(new Color(215, 255, 128));
        userIdTextField.setBorder(BorderFactory.createLineBorder(bgColor.darker()));
        userIdTextField.setCaretColor(Color.yellow.darker());
        userIdTextField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        userIdTextField.setBounds(85, 80, 50, 30);


        amountTextField.setBackground(bgColor.darker());
        amountTextField.setForeground(new Color(215, 255, 128));
        amountTextField.setBorder(BorderFactory.createLineBorder(bgColor.darker()));
        amountTextField.setCaretColor(Color.yellow.darker());
        amountTextField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        amountTextField.setBounds(85, 165, 50, 30);


        MyButton sendButton = new MyButton("SEND!");
        sendButton.setBackground(bgColor.darker());
        sendButton.setForeground(Color.yellow.darker());
        sendButton.setHoverBackgroundColor(bgColor.darker());
        sendButton.setFont(new Font(Font.SERIF, Font.BOLD, 35));
        sendButton.setBounds(1, 225, 218, 50);
        sendButton.setFocusable(false);
        sendButton.setBorderPainted(false);
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(this);
        sendButton.setPressedBackgroundColor(new Color(14, 163, 63));
        sendButton.setActionCommand("send");


        inputFieldPanel.add(amountTextField);
        inputFieldPanel.add(userIdTextField);
        inputFieldPanel.add(sendButton);

        add(inputFieldPanel, BorderLayout.EAST);
    }


    private void setUpScrollArea(){

        User[] userList = userService.getAllUsers();
        String formattedUserList = "  USERNAME |   ID\n";
               formattedUserList += "---------------------\n";
        int maxChar = 10;
        for(User user : userList){

            if(user.getId().equals(currentUser.getUser().getId()))
                continue;

            int userLength = user.getUsername().length();
            int spacesNeeded = maxChar - userLength;
            String spaces = "";

            for(int i = 0; i < spacesNeeded; i++)
                spaces += " ";

            formattedUserList += " ";
            formattedUserList += user.getUsername() + spaces + "|  " + user.getId();
            formattedUserList += "\n";
        }

        userListTextArea = new JTextArea(formattedUserList);
        userListTextArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 11));
        userListTextArea.setForeground(new Color(215, 255, 128));
        userListTextArea.setBackground(bgColor);
        userListTextArea.setEditable(false);

        userListScrollPane = new JScrollPane(userListTextArea);
        userListScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        userListScrollPane.setPreferredSize(new Dimension(150, 295));
        add(userListScrollPane, BorderLayout.WEST);
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
            tenmoSent = true;
            homePanel.repaint();

            FlyingMoneyController.flyTenmo(3);
        }
    }
}
