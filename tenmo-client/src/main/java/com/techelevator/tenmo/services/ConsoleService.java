package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printPendingTransfer(Transfer transfer){
        System.out.printf("TRANSFER ID:%5d\n", transfer.getTransfer_id());
        System.out.printf("TRANSFER TYPE:%5s\n", transfer.getTransfer_type());

        if(transfer.getTransfer_type().equals("Request"))
            System.out.printf("FROM ACCOUNT:%5d\n", transfer.getAccount_from());

        System.out.printf("AMOUNT:%5d\n", transfer.getAmount().toBigInteger());
        System.out.printf("STATUS:%5s\n", transfer.getTransfer_status());
    }

    public void printAllPendingTransfers(Transfer[] transfers){
        System.out.printf("<------------PENDING TRANSFERS---------->\n");
        System.out.printf(" %2s%13s%14s\n", "ID", "To", "AMOUNT");
        System.out.printf("=========================================\n");


        for(Transfer transfer : transfers){
            if(transfer.getTransfer_type().equals("Request")) {
                System.out.printf(" %-4d", transfer.getTransfer_id());
                System.out.printf("%9s", "");
                System.out.printf("%-10s", transfer.getAccount_to());
                System.out.printf("$%-12.2f", transfer.getAmount().doubleValue());
                System.out.println();
            }
        }
    }
}
