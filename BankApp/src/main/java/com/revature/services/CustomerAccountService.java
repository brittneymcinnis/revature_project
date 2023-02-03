package com.revature.services;

import java.util.List;

import com.revature.dao.CustomerAccountDAO;
import com.revature.dao.CustomerAccountPostgres;
import com.revature.models.Account;
import com.revature.models.Customer;

public class CustomerAccountService {
	
	public static CustomerAccountDAO custAcctDAO = new CustomerAccountPostgres();
	
	public List<Account> getCustomerAccounts(Customer customer){
		return custAcctDAO.getCustomerAccounts(customer);
	}
	
	public Account getCustomerAccountById(Customer customer, int id) {
		return custAcctDAO.getCustomerAccountById(customer, id);
	}
	
	public boolean createCustomerAccount(Customer customer, Account account) {
		if (custAcctDAO.createCustomerAccount(customer, account) > 0) {
			return true;
		} else {
			return false;
		}
	}
}
