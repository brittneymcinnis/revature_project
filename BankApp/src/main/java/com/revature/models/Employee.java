package com.revature.models;

public class Employee extends User{
	
	private long employeeId;
	public long getEmployeeId() {
		return employeeId;
	}
	
	public Employee(String firstName, String lastName, String userName, String password, String email) {
		super(firstName, lastName, userName, password, email);
	}
	
	public Employee(int userId, String firstName, String lastName, String userName, String password, String email) {
		super(userId, firstName, lastName, userName, password, email);
	}
	
//	public Account viewCustomerAccount(Customer customer, int index) {
//		return customer.getAccount(index);
//	}
}
