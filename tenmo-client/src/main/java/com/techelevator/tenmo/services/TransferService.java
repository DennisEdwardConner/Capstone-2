package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import io.cucumber.core.resource.Resource;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
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
        ResponseEntity<Transfer[]> response = null;

        try {
            response = restTemplate.exchange(API_BASE_URL + currentUser.getUser().getId() + "/transfer/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return response.getBody();
    }
    public Transfer[] getPreviousTransfers(){
        ResponseEntity<Transfer[]> response = null;
        Transfer[] transfers= null;
                try {
                    response = restTemplate.exchange(API_BASE_URL + currentUser.getUser().getId() + "/transfer/previous", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
                    transfers = response.getBody();
                }catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
                    BasicLogger.log(e.getMessage());
                }
                return transfers;
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    public void setCurrentUser(AuthenticatedUser currentUser){
        this.currentUser = currentUser;
    }
    public boolean sendTEBucks (Transfer transfer){
        ResponseEntity<Boolean> response = null;
        HttpEntity<Transfer> entity = makeTransferHttpEntity(transfer);
        boolean success= false;
        try {
            response = restTemplate.exchange(API_BASE_URL + "/transfer/send", HttpMethod.PUT, entity, boolean.class);
            response.getBody();
            success = true;
        }catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }
    private HttpEntity<Transfer> makeTransferHttpEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
