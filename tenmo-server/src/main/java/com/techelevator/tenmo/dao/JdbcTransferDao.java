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

    /**
     * Queries the database for all transfer requests for the current user with the status pending. It calls the
     * queryForRowSet method from the jdbcTemplate and uses the User's ID as the wildcard in the SQL statement. It then
     * loops through the results and using the helper method instantiates the transfer for each row and store the
     * transfers in a list.
     * @param userId
     * @returns List - containing all pending transfers
     */
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

    /**
     * Queries the database for all transfer requests for the current user with either an approved or rejected status. It calls the
     * queryForRowSet method from the jdbcTemplate and uses the User's ID as both wildcards in the SQL statement. It then
     * loops through the results and using the helper method instantiates the transfer for each row and stores the
     * transfers in a list.
     * @param id
     * @returns List - containing all past completed transactions (non-pending)
     */
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

    /**
     * Queries the database for a specific transfer. It calls the queryForRowSet method from the jdbcTemplate and takes
     * in our SQL statement and the transfer ID provided as the wildcard for the statement. Then it calls the
     * mapRowToTransfer helper method to instantiate the transfer and returns it.
     * @param id
     * @returns transfer - the on that corresponds with the ID
     */
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

    /**
     * Creates the transfer request in the database completing querying it with an Insert SQL statement.
     * It takes the transfer provided and uses the getter methods from the transfer class to obtain the transfer type id,
     * status id, who money is coming from, who is it going to, and the amount. then it calls the update method from the
     * jdbcTemplate uses the information it just got as the wildcards for the SQL statement. If the update is done
     * successfully is will print to the console it is updating if not it will display a message to the console with a
     * message noting why it could not access the data.
     * @param transfer
     * @returns transfer - the requested transfer
     */
    @Override
    public Transfer createTransferRequest(Transfer transfer) {
//       database visual
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
        try {
            jdbcTemplate.update(sql, account_from, account_to, transfer_status_id, transfer_type_id, amount);
            System.out.println("UPDATING");
        } catch(DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    /**
     * Queries the database by calling the update method from the jdbcTemplate. We use the getter method from the transfer
     * class and the transfer provided to get the status ID of the transfer and then use status ID and provided transfer ID
     * as the wildcards for our SQL statement. If it can't access the data it will print a message to the console.
     * @param transfer
     * @param id
     * @returns true - if it accesses the data and completes the update
     */
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

    /**
     * Helper method - it takes a queried rowset and the calls all the setters from the transfer class and instantiates
     * transfer to be returned
     * @param rowSet
     * @returns transfer - instantiated transfer from the database query
     */
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

    /**
     * Does two update queries in one transaction. The first update is removing the money from the one account and the
     * second is adding to the other account. It uses the getter methods from the transfer to get the amount, who it is
     * from, and who it is to. Then it Uses them as the wildcards in the SQL statement. It calls the update method from
     * the jdbcTemplate and returns false if it cannot access the data.
     * @param transfer
     * @returns True - if it accessed the data and completed the approved transfer
     */
    @Override
    public boolean approveSend(Transfer transfer){
        String sql = "BEGIN TRANSACTION; " +
                "UPDATE account SET balance = (balance - ?) " +
                "WHERE account_id = ?; " +
                "UPDATE account SET balance = (balance + ?) " +
                "WHERE account_id = ?; " +
                "COMMIT TRANSACTION;";

        boolean success = false;
        try {
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_from(),
                    transfer.getAmount(), transfer.getAccount_to());
            success = true;
        }catch (DataAccessException e){
            success = false;
        }
        return success;
    }

    /**
     * Does three queries in one SQL transaction.
     *
     * First - it inserts the transfer into the database.
     *
     * Second - it updates the account balance of the user sending the money by subtracting it from their balance.
     *
     * Third - it updates the account balance of the user receiving the money by adding it to their balance.
     *
     * It calls the update method from the jdbcTemplate and uses the getters from the transfer to get the id of the
     * account the money is coming from, the id of the account the money is going to, status id, type id, and the amount
     * of the transfer. It uses this information as the wildcards in the sql statements.
     * @param transfer
     * @returns true - if it accesses the data and completes the transfer
     */
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