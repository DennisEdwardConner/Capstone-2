package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public class TransferService {
    private final String API_BASE_URL;
    private AuthenticatedUser currentUser;


    public TransferService(String apiUrl, AuthenticatedUser currentUser){
        API_BASE_URL = apiUrl;
        this.currentUser = currentUser;
    }
    //TODO List method
    //public List<Transfer> getPendingTransfers(){return null;}
}
