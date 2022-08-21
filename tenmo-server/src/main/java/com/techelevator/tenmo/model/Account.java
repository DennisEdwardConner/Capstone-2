package com.techelevator.tenmo.model;

import org.springframework.data.annotation.Id;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Account {

    @NotNull
    @Id
    private int id;
    @NotNull
    private int user_id;
    @NotNull
    private BigDecimal balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    }

