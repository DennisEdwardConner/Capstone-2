package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllPendingTransfers(int accountFrom);

    List<Transfer> getAllSuccessfulTransfers();

    Transfer getTransferById();

    Transfer createTransferRequest(Transfer transfer);

    Transfer updateTransferStatus();
}
