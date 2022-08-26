package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao){
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "{userId}/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int userId){
        return transferDao.getAllPendingTransfers(userId);
    }

    @RequestMapping(value = "transfer/create", method = RequestMethod.POST)
    public Transfer createNewTransfer(@RequestBody Transfer newTransfer){
        return transferDao.createTransferRequest(newTransfer);
    }

    @RequestMapping(value = "{userId}/transfer/previous", method = RequestMethod.GET)
    public List<Transfer> getPreviousTransfer(@PathVariable int userId){
        return transferDao.getPreviousTransfers(userId);
    }

    @RequestMapping(value = "transfer/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transfer_Id){
        return transferDao.getTransferById(transfer_Id);
    }

    @RequestMapping(value = "transfer/update", method = RequestMethod.PUT)
    public boolean updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int transfer_id ){
        return transferDao.updateTransferStatus(transfer, transfer_id);
    }
    
}
