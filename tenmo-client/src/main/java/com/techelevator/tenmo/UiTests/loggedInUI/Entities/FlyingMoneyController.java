package com.techelevator.tenmo.UiTests.loggedInUI.Entities;

import com.techelevator.tenmo.UiTests.loggedInUI.TenmoHomeHeader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlyingMoneyController {

    public static List<FlyingMoney> moneyList = new ArrayList<>();

    public FlyingMoneyController(){
        startFlyingMoneyThread();
    }

    private void startFlyingMoneyThread(){
        Thread flyingMoney = new Thread(){
            public void run(){
                while(true){
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(System.nanoTime()%1000 == 0){
                        moneyList.add(new FlyingMoney());
                    }

                }
            }
        };

        flyingMoney.start();
    }
}
