package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.Transaction;
import com.revature.util.ConnectionUtil;

public class TransactionPostgres implements TransactionDAO {

	@Override
	public List<Transaction> getTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		ResultSet rs = null;
		
		String sql = "SELECT * FROM bank.transactions t"
				+ " INNER JOIN bank.account a ON t.account_id = a.account_id;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			Statement s = c.createStatement();
			rs = s.executeQuery(sql);
			
			while (rs.next()) {
				int transactionId = rs.getInt("transaction_id");
				String transactionType = rs.getString("transact_type");
				double amount = rs.getDouble("amount");
				String message = rs.getString("message");
				Timestamp transactionStart = rs.getTimestamp("transaction_date_start");
				Timestamp transactionStop = rs.getTimestamp("transaction_date_complete");
				
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("acc_type");
				double balance = rs.getDouble("balance");
				String accountStatus = rs.getString("acc_status");
				
				transactions.add(new Transaction(transactionId, transactionType, amount, new Account(accountId, accountType, balance, accountStatus), message, transactionStart, transactionStop));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactions;
	}

	@Override
	public Transaction getTransactionById(int id) {
		ResultSet rs = null;
		Transaction transaction = null;
		
		String sql = "SELECT * FROM bank.transactions t "
				+ " INNER JOIN bank.account a ON t.account_id = a.account_id"
				+ " WHERE a.account_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int transactionId = rs.getInt("transaction_id");
				String transactionType = rs.getString("transaction_type");
				double amount = rs.getDouble("amount");
				String message = rs.getString("message");
				Timestamp transactionStart = rs.getTimestamp("transaction_date_start");
				Timestamp transactionStop = rs.getTimestamp("transaction_date_complete");
				
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("acc_type");
				double balance = rs.getDouble("balance");
				String accountStatus = rs.getString("acc_status");
				
				transaction = new Transaction(transactionId, transactionType, amount, new Account(accountId, accountType, balance, accountStatus), message, transactionStart, transactionStop);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transaction;
	}

	@Override
	public int createTransaction(Transaction transaction) {
		int transactionsCreated = 0;
		
		String sql = "INSERT INTO bank.transactions (transaction_id, account_id, amount, message, transact_type, transaction_date_start, transaction_date_complete) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, transaction.getTransactionId());
			ps.setInt(2, transaction.getAccount().getAccountId());
			ps.setDouble(3, transaction.getAmount());
			ps.setString(4, transaction.getMessage());
			ps.setString(5, transaction.getType());
			ps.setTimestamp(6, transaction.getTransactionCreateDateTime());
			ps.setTimestamp(7, transaction.getTransactionCompleteDateTime());
			
			transactionsCreated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionsCreated;
	}

	@Override
	public int updateTransaction(Transaction transaction) {
		int transactionsUpdated = 0;
		
		String sql = "UPDATE bank.transactions SET account_id = ?, amount = ?, message = ?, transact_type = ?, transaction_date_start = ?, transaction_date_complete = ? WHERE transaction_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, transaction.getAccount().getAccountId());
			ps.setDouble(2, transaction.getAmount());
			ps.setString(3, transaction.getMessage());
			ps.setString(4, transaction.getType());
			ps.setTimestamp(5,  transaction.getTransactionCreateDateTime());
			ps.setTimestamp(6,  transaction.getTransactionCompleteDateTime());
			ps.setInt(7, transaction.getTransactionId());
			
			transactionsUpdated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transactionsUpdated;
	}

	@Override
	public int deleteTransaction(int id) {
		int transactionsDeleted = 0;
		
		String sql = "DELETE FROM bank.transactions WHERE transaction_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			
			transactionsDeleted = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionsDeleted;
	}
	
	@Override
	public int makeWithdrawal(Account account, double amount) {
		int withdrawSuccess = 0;
		
		String sql = " { call bank.makeTransaction(?::bank.transaction_type, ?, ?::numeric) } ";
		
		try (
				Connection c = ConnectionUtil.getConnection();
				CallableStatement cs = c.prepareCall(sql)) {
			//c.setAutoCommit(false);
			cs.setString(1, "WITHDRAWAL");
			cs.setInt(2, account.getAccountId());
			cs.setDouble(3,  amount);
			
			cs.execute();
			
			withdrawSuccess = 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return withdrawSuccess;
	}

	@Override
	public int makeDeposit(Account account, double amount) {
		int depositSuccess = 0;
		
		String sql = " { call bank.makeTransaction(?::bank.transaction_type, ?, ?::numeric) } ";
		
		try (
				Connection c = ConnectionUtil.getConnection();
				CallableStatement cs = c.prepareCall(sql)) {
			//c.setAutoCommit(false);
			cs.setString(1, "DEPOSIT");
			cs.setInt(2, account.getAccountId());
			cs.setDouble(3,  amount);
			
			cs.execute();
			
			depositSuccess = 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return depositSuccess;
	}
}
