package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate template){
        this.jdbcTemplate = template;
    }

    @Override
    public BigDecimal getBalance(int user_id){
        String SQL = "SELECT account_id, user_id, balance" +
                "FROM account " +
                "WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, user_id);

        Account account = mapRowToAccount(results);
        return account.getBalance();
    }

    @Override
    public List<Account> findAllByUserId(int id) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account FROM tenmo_user WHERE user_id ILIKE ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while(results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }


    @Override
    public Account findByUserId(int id) {
        String sql = "SELECT account_id, user_id, balance" +
                " FROM account " +
                "WHERE user_id ILIKE ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            Account account = mapRowToAccount(results);
            return account;
        }
        else return null;
    }
    @Override
    public Account findByAccountId(int id) {
        String sql = "SELECT account_id, user_id, balance" +
                " FROM account " +
                "WHERE account_id ILIKE ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            Account account = mapRowToAccount(results);
            return account;
        }
        else return null;
    }


    @Override
    public boolean update(Account account) {
        String sql = "Update account set balance = ?" +
                "where account_id = ?";
        try {
            jdbcTemplate.update(sql, account.getBalance(), account.getId());
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }
    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setId(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("account_balance"));

        return account;
    }
}
