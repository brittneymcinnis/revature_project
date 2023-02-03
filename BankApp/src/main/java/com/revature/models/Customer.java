package com.revature.models;

import java.io.Serializable;

public class Customer extends User implements Serializable{
	
	public Customer(String firstName, String lastName, String userName, String password, String email) {
		super(firstName, lastName, userName, password, email);
	}
	
	public Customer(int userId, String firstName, String lastName, String userName, String password, String email) {
		super(userId, firstName, lastName, userName, password, email);
	}
	
	@Override
	public String toString() {
		return "Customer [getUserId()=" + getUserId() + ", getFirstName()=" + getFirstName() + ", getLastName()="
				+ getLastName() + ", getUserName()=" + getUserName() + ", getPassword()=" + getPassword()
				+ ", getEmail()=" + getEmail() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
