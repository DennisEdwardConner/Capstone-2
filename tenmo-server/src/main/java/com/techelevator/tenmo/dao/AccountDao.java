package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalance(int account_id);

    List<Account> findAllByUserId(int id);
    Account findByUserId(int id);
    Account findByAccountId(int id);
    boolean update(Account account);
    }

