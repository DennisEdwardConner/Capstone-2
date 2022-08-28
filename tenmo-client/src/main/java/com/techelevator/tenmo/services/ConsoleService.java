package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import java.math.BigDecimal;
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

    /**
     * Takes in the balance and prints to the console
     * @param balance
     */
    public void printCurrentBalance(BigDecimal balance){
        System.out.println("\n=========================================");
        System.out.println("Your current balance is: $" + balance);
        System.out.println("=========================================");
    }

    /**
     * Takes all the transfers and for each one displays them to the console accordingly
     * @param transfers
     * @return int from prompted user
     */
    public int promptAllPendingTransfers(Transfer[] transfers){
        System.out.printf("<------------PENDING TRANSFERS---------->\n");
        System.out.printf(" %2s%13s%15s\n", "ID", "To", "AMOUNT");
        System.out.printf("=========================================\n");


        for(Transfer transfer : transfers){
            System.out.printf(" %-4d", transfer.getTransfer_id());
            System.out.printf("%9s", "");
            System.out.printf("%-11s", transfer.getUsername_to());
            System.out.printf("$%-15.2f", transfer.getAmount().doubleValue());
            System.out.println("\n=========================================");
        }

        return promptForMenuSelection("       Please choose an ID: ");
    }

    /**
     * Displays all current users available in the system for selection to send/request TE bucks. It displays all users
     * except for the currentUser. You are not allowed to request or send TE bucks to yourself.
     * @param users
     * @param currentUser
     */
    public void displayUsers(User[] users, AuthenticatedUser currentUser){
        System.out.printf("Id%15s\n", "Username");
        System.out.println("=======================");
        for (User user: users) {

            if(user.getUsername().equals(currentUser.getUser().getUsername()))
                continue;

            System.out.printf("%4d%5s%-1s\n", user.getId(), "", user.getUsername());
        }
        System.out.println("=======================");
    }

    /**
     * Displays all past transfers. It takes the transfers that are approved or rejected and displays them to the
     * console and uses logic and the current account id to determine whether the transfer was from or to the current user.
     * @param transfers
     * @param currentAccId
     */
    public void displayPastTransfer(Transfer[] transfers, int currentAccId) {
        System.out.printf("<------------TRANSFER  HISTORY---------->\n");
        System.out.printf(" %2s%13s%15s\n", "ID", "From/To", "AMOUNT");
        System.out.printf("=========================================\n");


        for (Transfer transfer : transfers) {
            System.out.printf(" %-4d", transfer.getTransfer_id());
            System.out.printf("%4s", "");
            if(currentAccId == transfer.getAccount_to()){
                System.out.printf("%-16s", "From: " + transfer.getUsername_from());
            }
            else{
                System.out.printf("%-16s", "To: " + transfer.getUsername_to());
            }
            System.out.printf("$%-15.2f", transfer.getAmount().doubleValue());
            //System.out.println("\n=========================================");
            System.out.println();
        }

        System.out.println("=========================================");
    }

    /**
     *
     * @param transferId
     * @returns user prompted selection
     */
    public int promptPendingChange(int transferId){
        System.out.println("\t1: Approve");
        System.out.println("\t2: Reject");
        System.out.println("\t0: Dont Approve or Reject");

        return promptForInt("Please choose an option: ") + 1;
    }

    /**
     * Uses the selected tranfer and then displays the details of the transaction to the console.
     * @param transfer
     */
    public void printTransferDetails(Transfer transfer){
        System.out.printf("\n<------------TRANSFER  DETAILS---------->\n");
        System.out.println("\t\tTRANSFER ID: " + transfer.getTransfer_id());
        System.out.println("\t\tFROM: " + transfer.getUsername_from());
        System.out.println("\t\tTO:   " + transfer.getUsername_to());
        System.out.println("\t\tTYPE: " + transfer.getTransfer_type());
        System.out.println("\t\tSTATUS: " + transfer.getTransfer_status());
        System.out.println("\t\tAMOUNT: " + transfer.getAmount());
        System.out.println("=========================================");
    }
}
