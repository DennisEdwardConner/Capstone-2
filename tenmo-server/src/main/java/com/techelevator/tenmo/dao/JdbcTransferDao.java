package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.jboss.logging.BasicLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllPendingTransfers(int userId) {
        List<Transfer> pendingTransferList = new ArrayList<>();

        String sql = "SELECT transfer_id, user_from.username AS user_from, user_to.username AS user_to, acc_to.user_id, " +
                "transfer_status_desc, transfer_type_desc, account_from, account_to, amount, transfer.transfer_type_id, " +
                "transfer.transfer_status_id " +
                "FROM transfer " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN account AS acc_to ON transfer.account_to = acc_to.account_id " +
                "JOIN account AS acc_from ON transfer.account_from = acc_from.account_id " +
                "JOIN tenmo_user AS user_to ON user_to.user_id = acc_to.user_id " +
                "JOIN tenmo_user AS user_from ON acc_from.user_id = user_from.user_id " +
                "WHERE transfer_status_desc = 'Pending' " +
                "AND transfer_type_desc = 'Request' " +
                "AND acc_from.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while(results.next()){
            pendingTransferList.add(mapRowToTransfer(results));
        }

        return pendingTransferList;
    }

    @Override
    public List<Transfer> getPreviousTransfers(int id) {
        List <Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, user_from.username AS user_from, user_to.username AS user_to, acc_to.user_id, " +
                "transfer_status_desc, transfer_type_desc, account_from, account_to, amount, transfer.transfer_type_id, " +
                "transfer.transfer_status_id " +
                "FROM transfer " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN account AS acc_to ON transfer.account_to = acc_to.account_id " +
                "JOIN account AS acc_from ON transfer.account_from = acc_from.account_id " +
                "JOIN tenmo_user AS user_to ON user_to.user_id = acc_to.user_id " +
                "JOIN tenmo_user AS user_from ON acc_from.user_id = user_from.user_id " +
                "WHERE acc_from.user_id = ? " +
                "AND transfer_status_desc = 'Approved' OR transfer_status_desc = 'Rejected' " +
                "OR acc_to.user_id = ? " +
                "AND transfer_status_desc = 'Approved' OR transfer_status_desc = 'Rejected'; ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while(results.next()){
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, user_from.username AS user_from, user_to.username AS user_to, acc_to.user_id, " +
                "transfer_status_desc, transfer_type_desc, account_from, account_to, amount, transfer.transfer_type_id, " +
                "transfer.transfer_status_id " +
                "FROM transfer " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN account AS acc_to ON transfer.account_to = acc_to.account_id " +
                "JOIN account AS acc_from ON transfer.account_from = acc_from.account_id " +
                "JOIN tenmo_user AS user_to ON user_to.user_id = acc_to.user_id " +
                "JOIN tenmo_user AS user_from ON acc_from.user_id = user_from.user_id " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if(results.next())
            transfer = mapRowToTransfer(results);
        return transfer;
    }

    @Override
    public Transfer createTransferRequest(Transfer transfer) {
//        {
//            "transfer_id" : "4",
//                "transfer_type_id" : "1",
//                "transfer_status_id" : "1",
//                "account_from" : "2001",
//                "account_to" : "2002",
//                "amount" : "50.00"
//        }
        int transfer_type_id = transfer.getTransfer_type_id();
        int transfer_status_id = transfer.getTransfer_status_id();
        int account_from = transfer.getAccount_from();
        int account_to = transfer.getAccount_to();
        BigDecimal amount = transfer.getAmount();

        String sql = "INSERT INTO transfer (account_from, account_to, transfer_status_id, transfer_type_id, amount) " +
                "VAlUES (?, ?, ?, ?, ?); ";
        System.out.println("before");
        try {
            jdbcTemplate.update(sql, account_from, account_to, transfer_status_id, transfer_type_id, amount);
            System.out.println("UPDATING");
        } catch(DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }
    //TODO double check this method
    @Override
    public boolean updateTransferStatus(Transfer transfer, int id) {
        String sql = "UPDATE transfer SET transfer_status_id = ? " +
                "WHERE transfer_id = ?;";
        try {
            jdbcTemplate.update(sql, transfer.getTransfer_status_id(), id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());;
            return false;
        }
        return true;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rowSet.getInt("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setTransfer_type(rowSet.getString("transfer_type_desc"));
        transfer.setTransfer_status(rowSet.getString("transfer_status_desc"));
        transfer.setAccount_from(rowSet.getInt("account_from"));
        transfer.setAccount_to(rowSet.getInt("account_to"));
        transfer.setUsername_from(rowSet.getString("user_from"));
        transfer.setUsername_to(rowSet.getString("user_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }
    @Override
    public boolean sendTEBucks(Transfer transfer){
        String sql = "BEGIN TRANSACTION; " +
                "INSERT INTO transfer (account_from, account_to, transfer_status_id, transfer_type_id, amount) " +
                "VAlUES (?, ?, ?, ?, ?); " +
                "UPDATE account SET balance = (balance - ?) " +
                "WHERE account_id = ?; " +
                "UPDATE account SET balance = (balance + ?) " +
                "WHERE account_id = ?; " +
                "COMMIT TRANSACTION;";
        boolean success = false;
        try {
            jdbcTemplate.update(sql, transfer.getAccount_from(), transfer.getAccount_to(),
                    transfer.getTransfer_status_id(), transfer.getTransfer_type_id(), transfer.getAmount(),
                    transfer.getAmount(), transfer.getAccount_from(), transfer.getAmount(), transfer.getAccount_to());
            success = true;
        }catch (DataAccessException e){
            success = false;
        }
        return success;
    }
}