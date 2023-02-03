package com.revature.dao;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.Transaction;

public interface TransactionDAO {
	public List<Transaction> getTransactions();
	public Transaction getTransactionById(int id);
	public int createTransaction(Transaction transaction);
	public int updateTransaction(Transaction transaction);
	public int deleteTransaction(int id);
	public int makeWithdrawal(Account account, double amount);
	public int makeDeposit(Account account, double amount);
}
