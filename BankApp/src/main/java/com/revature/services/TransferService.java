package com.revature.services;

import java.util.List;

import com.revature.dao.TransferDAO;
import com.revature.dao.TransferPostgres;
import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.Transfer;

public class TransferService {

	public static TransferDAO transferDAO = new TransferPostgres();
	
	public List<Transfer> getTransfers() {
		return transferDAO.getTransfers();
	}
	
	public Transfer getTransferById (int id) {
		return transferDAO.getTransfersById(id);
	}
	
	public boolean createTransfer (Account accountFrom, Account accountTo, double amount) {
		int result = transferDAO.createTransfer(accountFrom, accountTo, amount);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean updateTransfer (Transfer transfer) {
		int result = transferDAO.updateTransfer(transfer);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean deleteTransfer (int id) {
		int result = transferDAO.deleteTransfer(id);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public List<Transfer> getCustomerPendingTransfers(Customer customer){
		return transferDAO.getCustomerPendingTransfers(customer);
	}
	
	public boolean denyTransfer(Transfer transfer) {
		if (transferDAO.denyTransfer(transfer) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean acceptTransfer(Transfer transfer, double newBalanceTo, double newBalanceFrom) {
		if (transferDAO.acceptTranfer(transfer, newBalanceTo, newBalanceFrom) > 0) {
			return true;
		} else {
			return false;
		}
	}
}
