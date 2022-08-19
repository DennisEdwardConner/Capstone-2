package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private int account_from;
    private int account_to;
    private BigDecimal amount;
    private String transfer_type;
    private String transfer_status;
    private String user_from;
    private String user_to;

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status = transfer_status;
    }

    public String getUser_from() {
        return user_from;
    }

    public void setUser_from(String user_from) {
        this.user_from = user_from;
    }

    public String getUser_to() {
        return user_to;
    }

    public void setUser_to(String user_to) {
        this.user_to = user_to;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Transfer(int transfer_id, int transfer_type_id, int transfer_status_id, int account_from,
                    int account_to, BigDecimal amount, String transfer_type, String transfer_status,
                    String user_from, String user_to) {
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
        this.transfer_type = transfer_type;
        this.transfer_status = transfer_status;
        this.user_from = user_from;
        this.user_to = user_to;
    }



}

