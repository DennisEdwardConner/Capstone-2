package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;

public class TransferService {
    private final String API_BASE_URL;

    private AuthenticatedUser currentUser;

    public TransferService(String apiUrl, AuthenticatedUser currentUser){
        API_BASE_URL = apiUrl;
        this.currentUser = currentUser;
    }
}
