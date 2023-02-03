package com.revature.models;

import java.sql.Timestamp;

public class Transfer extends Transaction {

	private Account receivingAccount;
	String status;
	
	
	public Transfer(int transaction_id, String type, double amount, Account account, String message, Timestamp transactionCreateDateTime, Timestamp transactionCompleteDateTime, String status, Account receivingAccount) {
		super(transaction_id, type, amount, account, message, transactionCreateDateTime, transactionCompleteDateTime);
		this.receivingAccount = receivingAccount;
		this.status = status;
	}


	public Account getReceivingAccount() {
		return receivingAccount;
	}


	public void setReceivingAccount(Account receivingAccount) {
		this.receivingAccount = receivingAccount;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}	
}
