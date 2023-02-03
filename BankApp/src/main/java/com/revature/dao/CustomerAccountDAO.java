package com.revature.dao;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.Customer;

public interface CustomerAccountDAO {
	public List<Account> getCustomerAccounts(Customer customer);
	public Account getCustomerAccountById(Customer customer, int accountId);
	public int createCustomerAccount(Customer customer, Account account);
	public int updateCustomerAccount(Customer customer, Account account);
	public int deleteCustomerAccount(Customer customer, Account account);
}
