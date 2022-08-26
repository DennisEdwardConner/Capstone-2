package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    private final String API_BASE_URL;
    private AuthenticatedUser currentUser;

    private RestTemplate restTemplate = new RestTemplate();

    public UserService(String apiUrl){
        API_BASE_URL = apiUrl;
        this.currentUser = currentUser;
    }
    public void setCurrentUser(AuthenticatedUser currentUser){
        this.currentUser = currentUser;
    }
    private HttpEntity<Void> createAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
    public User[] getAllUsers(){
        ResponseEntity<User[]> response = null;
        User[] users = null;
        try {
            response = restTemplate.exchange(API_BASE_URL + "users/getAll",
                    HttpMethod.GET, createAuthEntity(), User[].class);
            users = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;

    }
}
