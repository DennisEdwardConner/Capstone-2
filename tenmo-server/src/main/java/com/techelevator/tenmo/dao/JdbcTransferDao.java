package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
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
    public List<Transfer> getAllPendingTransfers(int accountFrom) {
        List<Transfer> pendingTransferList = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer.transfer_status_id, account_from, account_to, amount " +
                     "FROM transfer JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                     "WHERE transfer_status_desc LIKE 'Pending' " +
                     "AND account_from = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);

        while(results.next()){
            pendingTransferList.add(mapRowToTransfer(results));
        }

        return pendingTransferList;
    }

    @Override
    public List<Transfer> getAllSuccessfulTransfers() {
        return null;
    }

    @Override
    public Transfer getTransferById() {
        return null;
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
        int transfer_id = transfer.getTransfer_id();
        int transfer_status_id = transfer.getTransfer_status_id();
        int account_from = transfer.getAccount_from();
        int account_to = transfer.getAccount_to();
        BigDecimal amount = transfer.getAmount();

        String sql = "INSERT INTO transfer(transfer_type_id, transfer_id, transfer_status_id, account_from, account_to, amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?);";

        jdbcTemplate.update(sql, transfer_type_id, transfer_id, transfer_status_id, account_from, account_to, amount);

        return transfer;
    }

    @Override
    public Transfer updateTransferStatus() {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rowSet.getInt("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setAccount_from(rowSet.getInt("account_from"));
        transfer.setAccount_to(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }
}
