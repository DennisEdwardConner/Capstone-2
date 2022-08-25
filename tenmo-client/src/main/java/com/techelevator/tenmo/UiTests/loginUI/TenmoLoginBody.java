package com.techelevator.tenmo.UiTests.loginUI;


import com.techelevator.tenmo.UiTests.MyButton;
import com.techelevator.tenmo.UiTests.loggedInUI.TenmoHomeFrame;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TenmoLoginBody extends JPanel implements ActionListener {
    private final int WIDTH = 700;
    private final int HEIGHT = 650;
    private Color bgColor = new Color(0, 153, 102);
    private BufferedImage bgImg = null;

    private JTextField usernameField, registerUsernameField;
    private JPasswordField passwordField, registerPasswordField;
    private boolean loginFailed = false;
    private boolean registerFailed = false;

    private MyButton loginButton, registerButton;
    private JFrame masterFrame;

    private AuthenticationService authenticationService;
    private AuthenticatedUser user;


    public TenmoLoginBody(AuthenticationService authenticationService, JFrame masterFrame) {
        this.authenticationService = authenticationService;
        this.masterFrame = masterFrame;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);

        //Get background IMG
        try {
            bgImg = ImageIO.read(new File("Resources/TenmoLoginBody.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpTextFields();
        setUpButtons();

        //color changer for buttons
        createColorThread();

        //text field bouncers
        createTextBouncerThread();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(bgImg, null, 0, 0);

        //Separation line between header and body
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawLine(0, 0, 700, 0);

        //Draw login failed text
        g2D.setColor(new Color(135, 0, 30));
        if(loginFailed) {
            g2D.setFont(new Font(null, Font.PLAIN, 15));
            g2D.drawString("Username or Password is incorrect", 230, 170);
        }

        //Draw register failed text
        if(registerFailed) {
            g2D.setFont(new Font(null, Font.PLAIN, 15));
            g2D.drawString("Username is already taken", 260, 440);
        }
    }

    private void setUpButtons() {
        final int BUTTON_WIDTH = 100;
        final int BUTTON_HEIGHT = 30;

        //Allow pressing enter on buttons
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        //Set up login and register buttons
        loginButton = new MyButton("Login");
        loginButton.setBackground(bgColor.darker());
        loginButton.setForeground(Color.yellow.darker());
        loginButton.setHoverBackgroundColor(bgColor.darker());
        loginButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        loginButton.setBounds(300, 180, BUTTON_WIDTH, BUTTON_HEIGHT);
        loginButton.setName("login");
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setFocusable(true);
        loginButton.addActionListener(this);
        add(loginButton);

        registerButton = new MyButton("Register");
        registerButton.setBackground(bgColor.darker());
        registerButton.setForeground(Color.yellow.darker());
        registerButton.setHoverBackgroundColor(bgColor.darker());
        registerButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        registerButton.setBounds(285, 450, BUTTON_WIDTH + 30, BUTTON_HEIGHT);
        registerButton.setName("register");
        registerButton.setFocusable(true);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(this);
        add(registerButton);
    }

    private void setUpTextFields() {
        final int TEXT_FIELD_WIDTH = 120;
        final int TEXT_FIELD_HEIGHT = 30;
        //set Up username and password
        usernameField = new JTextField("");
        usernameField.setBackground(bgColor.darker());
        usernameField.setForeground(bgColor.brighter());
        usernameField.setBorder(BorderFactory.createLineBorder(bgColor.darker()));
        usernameField.setCaretColor(Color.yellow.darker());
        usernameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        usernameField.setBounds(190, 118, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        usernameField.setName("usernameField");
        add(usernameField);

        passwordField = new JPasswordField("");
        passwordField.setBackground(bgColor.darker());
        passwordField.setForeground(bgColor.brighter());
        passwordField.setBorder(BorderFactory.createLineBorder(bgColor.darker()));
        passwordField.setCaretColor(Color.yellow.darker());
        passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        passwordField.setBounds(525, 118, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        passwordField.setName("passwordField");
        add(passwordField);

        //Set up register username and password
        registerUsernameField = new JTextField("");
        registerUsernameField.setBackground(bgColor.darker());
        registerUsernameField.setForeground(bgColor.brighter());
        registerUsernameField.setBorder(BorderFactory.createLineBorder(bgColor.darker()));
        registerUsernameField.setCaretColor(Color.yellow.darker());
        registerUsernameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        registerUsernameField.setBounds(190, 388, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        registerUsernameField.setName("registerUsername");
        add(registerUsernameField);

        registerPasswordField = new JPasswordField("");
        registerPasswordField.setBackground(bgColor.darker());
        registerPasswordField.setForeground(bgColor.brighter());
        registerPasswordField.setBorder(BorderFactory.createLineBorder(bgColor.darker()));
        registerPasswordField.setCaretColor(Color.yellow.darker());
        registerPasswordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        registerPasswordField.setBounds(525, 388, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        registerPasswordField.setFocusable(true);
        registerPasswordField.setName("registerPassword");
        add(registerPasswordField);
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
                int loginDefaultY = usernameField.getY();
                int registerDefaultY = registerPasswordField.getY();
                while(true){
                    startTime = System.nanoTime();
                    try {
                        sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(usernameField.isFocusOwner())
                        bounceTextArea(usernameField);
                    else
                        usernameField.setBounds(usernameField.getX(), loginDefaultY, usernameField.getWidth(), usernameField.getHeight());

                    if(passwordField.isFocusOwner())
                        bounceTextArea(passwordField);
                    else
                        passwordField.setBounds(passwordField.getX(), loginDefaultY, passwordField.getWidth(), passwordField.getHeight());

                    if(registerUsernameField.isFocusOwner())
                        bounceTextArea(registerUsernameField);
                    else
                        registerUsernameField.setBounds(registerUsernameField.getX(), registerDefaultY, registerUsernameField.getWidth(), registerUsernameField.getHeight());

                    if(registerPasswordField.isFocusOwner())
                        bounceTextArea(registerPasswordField);
                    else
                        registerPasswordField.setBounds(registerPasswordField.getX(), registerDefaultY, registerPasswordField.getWidth(), registerPasswordField.getHeight());

                }
            }
        };

        textBouncer.start();
    }

    private void createColorThread() {
        Thread colorChangingButtonsThread = new Thread() {

            int R = bgColor.darker().getRed();
            int G = bgColor.darker().getGreen();
            int B = bgColor.darker().getBlue();

            int phase = 0;

            private void rotateColor() {

                if (phase == 0) {
                    R++;
                    if (R == 255)
                        phase++;
                }
                if (phase == 1) {
                    G++;
                    if (G == 255)
                        phase++;
                }
                if (phase == 2) {
                    R--;
                    if (R == 0)
                        phase++;
                }
                if (phase == 3) {
                    B++;
                    if (B == 255)
                        phase++;
                }
                if (phase == 4) {
                    G--;
                    if (G == 0)
                        phase++;
                }
                if (phase == 5) {
                    R++;
                    if (R == 255)
                        phase++;
                }
                if (phase == 6) {
                    B--;
                    if (B == 0)
                        phase = 1;
                }

            }

            public void resetRGB(){
                R = bgColor.darker().getRed();
                G = bgColor.darker().getGreen();
                B = bgColor.darker().getBlue();
            }

            public void run() {
                while (true) {
                    try {
                        sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Color buttonColor = new Color(R, G, B);

                    if (loginButton.getModel().isPressed() || loginButton.isFocusOwner()) {
                        loginButton.getModel().setPressed(true);
                        rotateColor();
                        loginButton.setPressedBackgroundColor(buttonColor);
                        loginButton.setForeground(new Color(255 - R, 255 - G, 255 - B));
                        loginButton.repaint();
                    } else {
                        loginButton.setBackground(bgColor.darker());
                        loginButton.setForeground(Color.yellow.darker());
                    }
                    if (registerButton.getModel().isPressed() || registerButton.isFocusOwner()) {
                        rotateColor();
                        registerButton.getModel().setPressed(true);
                        registerButton.setPressedBackgroundColor(buttonColor);
                        registerButton.setForeground(new Color(255 - R, 255 - G, 255 - B));
                        registerButton.repaint();
                    } else{
                        registerButton.setBackground(bgColor.darker());
                        registerButton.setForeground(Color.yellow.darker());
                    }


                    if(!loginButton.getModel().isPressed() && !registerButton.getModel().isPressed()) {
                        resetRGB();
                        phase = 0;
                    }
                }
            }
        };
        colorChangingButtonsThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton)e.getSource();

        if(clickedButton.getName().equals("login")) {

            UserCredentials userCredentials = new UserCredentials(usernameField.getText(), passwordField.getText());
            user = authenticationService.login(userCredentials);

            if(user != null) {
                //create home frame
                TenmoHomeFrame homeFrame = new TenmoHomeFrame(user);
                //kill login frame
                masterFrame.removeAll();
                masterFrame.setVisible(false);
                loginFailed = false;
            }else{
                loginFailed = true;
                repaint();
            }
        } else if(clickedButton.getName().equals("register")) {

            UserCredentials userCredentials = new UserCredentials("", "");
            userCredentials.setUsername(registerUsernameField.getText());
            userCredentials.setPassword(registerPasswordField.getText());

            if(authenticationService.register(userCredentials)) {
                registerFailed = false;
                repaint();
            }else{
                registerFailed = true;
                repaint();
            }

        }
    }

}