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
                new TenmoLoginFrame(authenticationService);
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

	private void viewCurrentBalance() {
        consoleService.printCurrentBalance(accountService.getAccountBalance());
	}

	private void viewTransferHistory() {

        int currentAccId = accountService.getByUserId(currentUser.getUser().getId()).getId();
        consoleService.displayPastTransfer(transferService.getPreviousTransfers(), currentAccId);
		
	}

	private void viewPendingRequests() {
		transferService.setCurrentUser(currentUser);
        Transfer[] pendingTransfers = transferService.getAllPendingTransfers();

        if(pendingTransfers != null) {
            int selectedTransferId = consoleService.promptAllPendingTransfers(pendingTransfers);
            
        } else{
            consoleService.printErrorMessage();
        }
	}

	private void sendBucks() {
     consoleService.displayUsers(userService.getAllUsers());
     long id = consoleService.promptForInt("Enter user ID for transfer: ");
     BigDecimal amount = consoleService.promptForBigDecimal("Please enter the transfer amount: ");
     Transfer transfer= new Transfer();
     // currentUser.getUser.getId returns the account and the .getID returns the account_id **
     transfer.setAccount_from(accountService.getByUserId(currentUser.getUser().getId()).getId());
     transfer.setAccount_to_id(accountService.getByUserId(id).getId());
     transfer.setAmount(amount);
     transfer.setTransfer_type_id(2);
     transfer.setTransfer_status_id(2);
     transferService.sendTEBucks(transfer);
    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
}
