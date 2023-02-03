package com.revature.dao;

import java.util.List;

import com.revature.models.Customer;

public interface CustomerDAO {
	public List<Customer> getCustomers();
	public Customer getCustomerById(int id);
	public int createCustomer(Customer customer);
	public int updateCustomer(Customer customer);
	public int deleteCustomer(int id);
	public Customer authenticateCustomer(String username, String password);
}
