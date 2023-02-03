package com.revature.dao;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.Transfer;

public interface TransferDAO {
	public List<Transfer> getTransfers();
	public Transfer getTransfersById(int id);
	public int createTransfer(Account accountFrom, Account accountTo, double amount);
	public int updateTransfer(Transfer transfer);
	public int deleteTransfer(int id);
	
	public List<Transfer> getCustomerPendingTransfers(Customer customer);
	public int acceptTranfer(Transfer transfer, double newBalanceTo, double newBalanceFrom);
	public int denyTransfer(Transfer transfer);
}
