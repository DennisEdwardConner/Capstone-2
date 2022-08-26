package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferService {
    private final String API_BASE_URL;
    private AuthenticatedUser currentUser;

    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String apiUrl){
        API_BASE_URL = apiUrl;
        this.currentUser = currentUser;
    }


    public Transfer[] getAllPendingTransfers(){
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + currentUser.getUser().getId() + "/transfer/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class);

        return response.getBody();
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    public void setCurrentUser(AuthenticatedUser currentUser){
        this.currentUser = currentUser;
    }
}
