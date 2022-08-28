package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private final String API_BASE_URL;

    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public AccountService(String apiUrl){
        API_BASE_URL = apiUrl;
    }

    public void setCurrentUser(AuthenticatedUser currentUser){
        this.currentUser = currentUser;
    }

    /**
     * Queries the database for the current users account balance
     * @returns BigDecimal - account balance
     */
    public BigDecimal getAccountBalance(){
        ResponseEntity<BigDecimal> response =
                restTemplate.exchange(API_BASE_URL + "/api/" + currentUser.getUser().getId() + "/getbalance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);

        return response.getBody();
    }

    /**
     * Instantiates HttpHeaders to set bearer authority by giving the user access token
     * @return HttpEntity
     */
    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    /**
     * Queries the Database for the account using the user ID
     * @param userId
     * @returns the user's account
     */
    public Account getByUserId(long userId){
        ResponseEntity<Account> response =
                restTemplate.exchange(API_BASE_URL + "account/findById/" + userId,
                        HttpMethod.GET, makeAuthEntity(), Account.class);
        return response.getBody();
    }
}
