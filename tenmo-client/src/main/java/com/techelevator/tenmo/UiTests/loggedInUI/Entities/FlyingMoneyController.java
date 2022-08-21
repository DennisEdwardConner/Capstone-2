package com.techelevator.tenmo.UiTests.loggedInUI.Entities;

import com.techelevator.tenmo.UiTests.loggedInUI.TenmoHomeHeader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlyingMoneyController {

    public static List<FlyingMoney> moneyList = new ArrayList<>();
    public static int spawnedMoney = 0;
    public static int desiredMoney = 0;
    public static boolean allSpawned = false;

    public FlyingMoneyController(){
        //startFlyingMoneyThread();
    }

    public static void flyTenmo(int amount){
        desiredMoney = amount;
        startFlyingMoneyThread();
    }

    private static void startFlyingMoneyThread(){
        Thread flyingMoney = new Thread(){
            public void run(){
                while(!allSpawned){
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(System.nanoTime()%1000 == 0){
                        moneyList.add(new FlyingMoney());
                        spawnedMoney ++;
                    }

                    if(desiredMoney == spawnedMoney) {
                        allSpawned = true;
                        desiredMoney = 0;
                    }

                }
                allSpawned = false;
            }
        };

        flyingMoney.start();
    }
}
