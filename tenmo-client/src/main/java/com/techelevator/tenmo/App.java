package com.techelevator.tenmo;

import com.techelevator.tenmo.UiTests.loginUI.TenmoLoginFrame;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        new TenmoLoginFrame(authenticationService);
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection == 3) {
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        accountService.setCurrentUser(currentUser);
        transferService.setCurrentUser(currentUser);
        userService.setCurrentUser(currentUser);
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    /**
     * Uses getAccountBalance method from the accountService class obtain the balance from the database and calls the
     * printCurrentBalance from the consoleService class to display the account balance to the user.
     */
    private void viewCurrentBalance() {
        consoleService.printCurrentBalance(accountService.getAccountBalance());
	}

    /**
     * Uses the User ID to query the database for the account ID and then Sets it as the current account ID.
     * Then, calls the displayPastTransfers method from the consoleServices class.
     * It uses the getPreviousTransfers method from the transferService class to get the transfers and then uses
     * transfers and account ID as the arguments and displays all transfers for that user.
     * After, it prompts the user to select one of the transfer ID's listed and stores the selection.
     * It then uses the selected transfer ID to query the Data base and return the transfer
     * and then Print the details of the transfer using the printTransferDetails method from the consoleService class
     */
	private void viewTransferHistory() {

        int currentAccId = accountService.getByUserId(currentUser.getUser().getId()).getId();
        consoleService.displayPastTransfer(transferService.getPreviousTransfers(), currentAccId);
        int transferIdSelected = consoleService.promptForInt("Enter transfer ID: ");
        consoleService.printTransferDetails(transferService.getTransferById(transferIdSelected));
		
	}

    /**
     * Sets the current user in the transferService class to the current user
     * Then queries the Data base for pending transfers and stores then in an array
     * If no pending transfers are in the database for the user, it tells the user by printing to the console
     * Then it prompts the user for a transfer ID by calling the promptAllPendingTranfers and using the stored array of
     * transfers. After it prompts the user to approve reject or do nothing by calling the promptPendingChange method
     * from the consoleService class.
     *
     * If they selected approve it checks to see they have enough TE bucks to send.
     * If they do not - it updates the status to rejected in the database and tells the user that they do not have enough
     * Te Bucks to complete the transfer and rejects does not send the money.
     *
     * If they have enough TE Bucks - It updates the status to approved in the database and completes the transfer by
     * calling the sendTEBucks methods from the transferService class.
     *
     * If they select reject - it updates status to rejected in the database and does not send the money
     *
     * If They choose to neither - it does nothing and the transfer stays in the pending requests and the user is
     * returned to the main screen for the user.
     */
	private void viewPendingRequests() {
		transferService.setCurrentUser(currentUser);
        Transfer[] pendingTransfers = transferService.getAllPendingTransfers();

        if(pendingTransfers != null) {

            if(pendingTransfers.length <= 0) {
                System.out.println("\tNO PENDING REQUESTS!!");
                return;
            }
            int selectedTransferId = consoleService.promptAllPendingTransfers(pendingTransfers);
            int transferStatusId = consoleService.promptPendingChange(selectedTransferId);

            if (transferStatusId == 1)
                return;
            else {
                Transfer transfer = transferService.getTransferById(selectedTransferId);
                if (transferStatusId == 2) {
                    if (transfer.getAmount().doubleValue() > accountService.getAccountBalance().doubleValue()) {
                        System.err.println("ERR: You Do Not Have Enough Money To Approve Request");
                        transferService.updateTransferStatus(transfer, 3);
                        return;
                    }
                    transferService.sendTEBucks(transfer);

                }
                transferService.updateTransferStatus(transfer, transferStatusId);
            }
        } else{
            consoleService.printErrorMessage();
        }
	}

    /**
     * Calls the displayUsers method from the consoleService class print all the users out
     * It then prompts the user for a user ID and stores it.
     * If they select their own user ID it displays an error message saying you can't send yourself money and returns
     * them to the main screen
     * If not it prompts them for the amount to transfer and store it by call the promptForBigDecimal method from the
     * console service class. It displays and error if they try to send more money than they have or try to send bucks
     * less than or equal to zero. IF they enter a valid ID and Amount it creates a new transfer in the database and
     * completes transfer of the TE Bucks by calling the SendTEBucks method from the transferService class.
     */
	private void sendBucks() {
         consoleService.displayUsers(userService.getAllUsers(), currentUser);
         long id = consoleService.promptForInt("Enter user ID for transfer: ");

         if(id == currentUser.getUser().getId()){
             System.err.println("ERR: Cannot Send Money To Yourself!");
             return;
         }

         BigDecimal amount = consoleService.promptForBigDecimal("Please enter the transfer amount: ");

         if(amount.doubleValue() <= 0.0){
             System.err.println("ERR: Transfer Amount Must Be Greater Than $0");
             return;
         }else if(amount.doubleValue() > accountService.getAccountBalance().doubleValue()){
             System.err.println("ERR: Transfer Amount Cannot Be Greater Than Balance");
             return;
         }

         Transfer transfer= new Transfer();
         // currentUser.getUser.getId returns the account and the .getID returns the account_id **
         transfer.setAccount_from(accountService.getByUserId(currentUser.getUser().getId()).getId());
         transfer.setAccount_to_id(accountService.getByUserId(id).getId());
         transfer.setAmount(amount);
         transfer.setTransfer_type_id(2);
         transfer.setTransfer_status_id(2);
         transferService.sendTEBucks(transfer);
    }

    /**
     * Calls the displayUsers method form the consoleService class and then prompts the user to enter the user ID of
     * the user to transfer the TE Bucks to and stores it. It then uses the getByUserId method from the accountService
     * class to get the users account ID. If the user ID selected is the current Users Id it with print an error message
     * saying you cannot request money from yourself. It then prompts for the amount of the transfer. If the amount isn't
     * greater than zero, it prints a message that says the transferred amount must be greater than zero. If the
     * User id selected and amount requested pass, it creates a new transfer request in the database with a pending
     * status that the requested user can approve or reject.
     */
	private void requestBucks() {

		consoleService.displayUsers(userService.getAllUsers(), currentUser);

        int userId = consoleService.promptForInt("Enter user ID for request: ");
        int requestId = accountService.getByUserId(userId).getId();

        if(requestId == currentUser.getUser().getId()){
            System.err.println("ERR: Cannot Request Money From Yourself!");
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the request amount: ");

        if(amount.doubleValue() <= 0.0){
            System.err.println("ERR: Request Amount Must Be Greater Than $0");
            return;
        }

        Transfer request = new Transfer();

        request.setAmount(amount);
        request.setTransfer_type_id(1);
        request.setTransfer_status_id(1);
        request.setAccount_from(requestId);
        request.setAccount_to_id(accountService.getByUserId(currentUser.getUser().getId()).getId());

        transferService.createTransferRequest(request);
	}
}
