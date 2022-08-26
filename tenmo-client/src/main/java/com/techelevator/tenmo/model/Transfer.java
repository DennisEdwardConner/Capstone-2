package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private int account_from;
    private int account_to;
    private String username_from;
    private String username_to;
    private BigDecimal amount;
    private String transfer_type_desc;
    private String transfer_status_desc;

    public Transfer(){

    }

    public String getUsername_from() {
        return username_from;
    }

    public void setUsername_from(String username_from) {
        this.username_from = username_from;
    }

    public String getUsername_to() {
        return username_to;
    }

    public void setUsername_to(String username_to) {
        this.username_to = username_to;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from_id) {
        this.account_from = account_from_id;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to_id(int account_to_id) {
        this.account_to = account_to_id;
    }

    public String getTransfer_type() {
        return transfer_type_desc;
    }

    public void setTransfer_type(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }

    public String getTransfer_status() {
        return transfer_status_desc;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status_desc = transfer_status;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Transfer(int transfer_id, int transfer_type_id, int transfer_status_id, int account_from,
                    int account_to, BigDecimal amount) {
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }

}
