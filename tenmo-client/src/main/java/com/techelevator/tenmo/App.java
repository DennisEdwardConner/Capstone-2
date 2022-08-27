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
        int transferIdSelected = consoleService.promptForInt("Enter transfer ID: ");
        consoleService.printTransferDetails(transferService.getTransferById(transferIdSelected));
		
	}

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

	private void requestBucks() {
		// TODO Auto-generated method stub
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
