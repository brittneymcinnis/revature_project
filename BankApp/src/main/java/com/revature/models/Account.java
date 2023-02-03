package com.revature.models;

import java.io.Serializable;

public class Account implements Serializable {
	
	private int accountId;
	private double balance;
	private String type;
	private String status;
	
	public Account() {}
	
	public Account(String type) {
		this.type = type;
		this.balance = 0.0;
		this.status = "PENDING";
	}
	
	public Account(String type, double balance, String status) {
		this.type = type; 
		this.balance = balance;
		this.status = status;
	}
	
	public Account(int id, String type, double balance, String status) {
		this.accountId = id;
		this.type = type; 
		this.balance = balance;
		this.status = status;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public int getAccountId() {
		return accountId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", balance=" + balance + ", type=" + type + ", status=" + status
				+ "]";
	}
	
	
}
