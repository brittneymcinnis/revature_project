package com.revature.dao;

import java.util.List;

import com.revature.models.Account;

public interface AccountDAO {
	public List<Account> getAccounts();
	public Account getAccountById(int id);
	public int createAccount(Account account, int customerId);
	public int updateAccount(Account account);
	public int deleteAccount(int id);
	
	public List<Account> getPendingAccounts();
}
