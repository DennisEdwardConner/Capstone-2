package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllPendingTransfers(int accountFrom);

    List<Transfer> getPreviousTransfers(int id);

    Transfer getTransferById(int id);

    Transfer createTransferRequest(Transfer transfer);

    boolean updateTransferStatus(Transfer transfer, int id);
}
