package com.revature.services;

import java.util.List;

import com.revature.dao.AccountDAO;
import com.revature.dao.AccountPostgres;
import com.revature.models.Account;

public class AccountService {
	
	public static AccountDAO acctDAO = new AccountPostgres();
	
	public List <Account> getAccounts(){
		return acctDAO.getAccounts();
	}
	
	public Account getAccountById (int id) {
		return acctDAO.getAccountById(id);
	}
	
	public boolean createAccount(Account account, int customerId) {
		if (acctDAO.createAccount(account, customerId) > 0)
			return true;
		else
			return false;
	}
	
	public boolean updateAccount(Account account) {
		int result = acctDAO.updateAccount(account);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean deleteAccount(int id) {
		int result = acctDAO.deleteAccount(id);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public List<Account> getPendingAccounts(){
		return acctDAO.getPendingAccounts();
	}
}
