package com.revature.services;

import java.util.List;

import com.revature.dao.TransactionDAO;
import com.revature.dao.TransactionPostgres;
import com.revature.models.Account;
import com.revature.models.Transaction;

public class TransactionService {

	public static TransactionDAO transactDAO= new TransactionPostgres();
	
	public List<Transaction> getTransactions(){
		return transactDAO.getTransactions();
	}
	
	public Transaction getTransactionById(int id) {
		return transactDAO.getTransactionById(id);
	}
	
	public boolean createTransaction (Transaction transaction) {
		int result = transactDAO.createTransaction(transaction);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean updateTransaction(Transaction transaction) {
		int result = transactDAO.updateTransaction(transaction);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean deleteTransaction(int id) {
		int result = transactDAO.deleteTransaction(id);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean makeWithdrawal(Account account, double amount) {
		if (transactDAO.makeWithdrawal(account, amount) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean makeDeposit(Account account, double amount) {
		if (transactDAO.makeDeposit(account, amount) > 0) {
			return true;
		} else {
			return false;
		}
	}
}
