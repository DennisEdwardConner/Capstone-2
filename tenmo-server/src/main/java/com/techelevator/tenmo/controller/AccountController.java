package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private AccountDao accountDao;

    public AccountController(AccountDao dao) {
        this.accountDao = dao;
    }


    @RequestMapping(value = "api/{id}/getbalance", method = RequestMethod.GET )
    public BigDecimal getBalance(@PathVariable int id){
        return accountDao.getBalance(id);
    }

    @RequestMapping(value = "api/account/findAll/{id}", method = RequestMethod.GET)
    List<Account> findAllByUserId(@PathVariable int id){
        return accountDao.findAllByUserId(id);
    }

    @RequestMapping(value = "api/account/user_id/{user_id}", method = RequestMethod.GET)
    Account findByUserId(@PathVariable int user_id){
        return accountDao.findByUserId(user_id);
    }

    @RequestMapping(value = "api/account/account_id/{account_id}")
    Account findByAccountId(@PathVariable int account_id){
        return accountDao.findByAccountId(account_id);
    }

    @RequestMapping(value = "api/account/{account_id}")
    Account update(@RequestBody Account account, @PathVariable int account_id){
        Account updatedAccount = new Account();
        updatedAccount.setId(account_id);
        updatedAccount.setBalance(account.getBalance());
        updatedAccount.setUser_id(account.getUser_id());
        accountDao.update(account);
        return updatedAccount;

    }
}

