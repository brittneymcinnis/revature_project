package com.revature.models;

import java.sql.Timestamp;

public class Transaction {

	private int transactionId;
	private Account account;
	private double amount;
	private String type;
	private String message;
	private Timestamp transactionCreateDateTime;
	private Timestamp transactionCompleteDateTime;
	
	public Transaction(int transaction_id, String type, double amount, Account account, String message, Timestamp transactionCreateDateTime, Timestamp transactionCompleteDateTime) {
		super();
		this.transactionId = transaction_id;
		this.amount = amount;
		this.type = type;
		this.account = account;
		this.transactionCreateDateTime = transactionCreateDateTime;
		this.transactionCompleteDateTime = transactionCompleteDateTime;
		this.message = message;
	}

	public int getTransactionId() {
		return this.transactionId;
	}
	
	public void setTransactionId(int id) {
		this.transactionId = id;
	}
		
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		if (amount < 0) {
			this.amount = 0.0;
		} else {
			this.amount = amount;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTransactionCreateDateTime() {
		return transactionCreateDateTime;
	}

	public void setTransactionCreateDateTime(Timestamp transactionCreateDateTime) {
		this.transactionCreateDateTime = transactionCreateDateTime;
	}

	public Timestamp getTransactionCompleteDateTime() {
		return transactionCompleteDateTime;
	}

	public void setTransactionCompleteDateTime(Timestamp transactionCompleteDateTime) {
		this.transactionCompleteDateTime = transactionCompleteDateTime;
	}
	
	
	
}
