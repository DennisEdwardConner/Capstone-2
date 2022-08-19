package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate template){
        this.jdbcTemplate = template;
    }

    @Override
    public BigDecimal getBalance(int user_id){
        String SQL = "SELECT balance FROM account WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(SQL, user_id);
        return results.getBigDecimal("balance");
    }
}
