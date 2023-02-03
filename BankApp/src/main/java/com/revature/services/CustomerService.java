package com.revature.services;

import java.util.List;

import com.revature.dao.CustomerDAO;
import com.revature.dao.CustomerPostgres;
import com.revature.models.Customer;

public class CustomerService {

	public static CustomerDAO custDAO = new CustomerPostgres();
	
	public List<Customer> getCustomers () {
		return custDAO.getCustomers();
	}
	
	public Customer getCustomerById (int id) {
		return custDAO.getCustomerById(id);
	}
	
	public boolean createCustomer (Customer customer) {
		int result = custDAO.createCustomer(customer);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean updateCustomer (Customer customer) {
		int result = custDAO.updateCustomer(customer);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean deleteCustomer (int id) {
		int result = custDAO.deleteCustomer(id);
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public Customer authenticateCustomer(String username, String password) {
		return custDAO.authenticateCustomer(username, password);
	}
}
