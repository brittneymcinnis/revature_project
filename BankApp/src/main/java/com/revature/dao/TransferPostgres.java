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
import com.revature.models.Customer;
import com.revature.models.Transfer;
import com.revature.util.ConnectionUtil;

public class TransferPostgres implements TransferDAO{

	@Override
	public List<Transfer> getTransfers() {
		List <Transfer> transfers = new ArrayList <>();
		ResultSet rs = null;
		
		String sql = "SELECT tf.trans_status,"
				+ "	tr.transaction_id, tr.amount, tr.transact_type, tr.message, tr.transaction_date_start, tr.transaction_date_complete,"
				+ "	acc_from.account_id as \"account_from_id\", acc_from.acc_type as \"account_from_type\", acc_from.acc_status as \"account_from_status\", acc_from.balance as \"account_from_balance\","
				+ "	acc_to.account_id as \"account_to_id\", acc_to.acc_type as \"account_to_type\", acc_to.acc_status as \"account_to_status\", acc_to.balance as \"account_to_balance\""
				+ " FROM bank.transactions tr INNER JOIN bank.account acc_from ON tr.account_id = acc_from.account_id"
				+ " INNER JOIN bank.transfers tf ON tr.transaction_id = tf.transaction_id INNER JOIN bank.account acc_to ON tf.account_to_transfer = acc_to.account_id;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			Statement s = c.createStatement();
			rs = s.executeQuery(sql);
			
			while (rs.next()) {
				
				String status = rs.getString("trans_status");
				
				int accountFromId = rs.getInt("account_from_id");
				String accountFromType = rs.getString("account_from_type");
				double accountFromBalance = rs.getDouble("account_from_balance");
				String accountFromStatus = rs.getString("account_from_status");
				
				int accountToId = rs.getInt("account_to_id");
				String accountToType = rs.getString("account_to_type");
				double accountToBalance = rs.getDouble("account_to_balance");
				String accountToStatus = rs.getString("account_to_status");
				
				int transactionId = rs.getInt("transaction_id");
				String transactionType = rs.getString("transact_type");
				double amount = rs.getDouble("amount");
				String message = rs.getString("message");
				Timestamp transactionStart = rs.getTimestamp("transaction_date_start");
				Timestamp transactionStop = rs.getTimestamp("transaction_date_complete");
				
				
				transfers.add(new Transfer(transactionId, transactionType, amount, new Account(accountFromId, accountFromType, accountFromBalance, accountFromStatus), message, transactionStart, transactionStop, status, new Account(accountToId, accountToType, accountToBalance, accountToStatus)));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transfers;
	}

	@Override
	public Transfer getTransfersById(int id) {
		Transfer transfer = null;
		ResultSet rs = null;
		
		String sql = "SELECT tf.trans_status,"
				+ "	tr.transaction_id, tr.amount, tr.transact_type, tr.message, tr.transaction_date_start, tr.transaction_date_complete,"
				+ "	acc_from.account_id as \"account_from_id\", acc_from.acc_type as \"account_from_type\", acc_from.acc_status as \"account_from_status\", acc_from.balance as \"account_from_balance\","
				+ "	acc_to.account_id as \"account_to_id\", acc_to.acc_type as \"account_to_type\", acc_to.acc_status as \"account_to_status\", acc_to.balance as \"account_to_balance\""
				+ " FROM bank.transactions tr INNER JOIN bank.account acc_from ON tr.account_id = acc_from.account_id"
				+ " INNER JOIN bank.transfers tf ON tr.transaction_id = tf.transaction_id INNER JOIN bank.account acc_to ON tf.account_to_transfer = acc_to.account_id"
				+ " WHERE tf.transaction_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,  id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				String status = rs.getString("trans_status");
				
				int accountFromId = rs.getInt("account_from_id");
				String accountFromType = rs.getString("account_from_type");
				double accountFromBalance = rs.getDouble("account_from_balance");
				String accountFromStatus = rs.getString("account_from_status");
				
				int accountToId = rs.getInt("account_to_id");
				String accountToType = rs.getString("account_to_type");
				double accountToBalance = rs.getDouble("account_to_balance");
				String accountToStatus = rs.getString("account_to_status");
				
				int transactionId = rs.getInt("transaction_id");
				String transactionType = rs.getString("transact_type");
				double amount = rs.getDouble("amount");
				String message = rs.getString("message");
				Timestamp transactionStart = rs.getTimestamp("transaction_date_start");
				Timestamp transactionStop = rs.getTimestamp("transaction_date_complete");
				
				
				transfer = new Transfer(transactionId, transactionType, amount, new Account(accountFromId, accountFromType, accountFromBalance, accountFromStatus), message, transactionStart, transactionStop, status, new Account(accountToId, accountToType, accountToBalance, accountToStatus));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transfer;
	}

	@Override
	public int createTransfer(Account accountFrom, Account accountTo, double amount) {
		int success = 0;
		
		String sql = " { call bank.post_transfer(?, ?, ?::numeric)} ";
		
		try (
				Connection c = ConnectionUtil.getConnection();
				CallableStatement cs = c.prepareCall(sql)
			){
			cs.setInt(1, accountFrom.getAccountId());
			cs.setInt(2, accountTo.getAccountId());
			cs.setDouble(3, amount);
			
			cs.execute();
			success = 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public int updateTransfer(Transfer transfer) {
		int transfersUpdated = 0;
		
		String sql = "UPDATE bank.transfers SET account_to_transfer = ?, trans_status = ?"
				+ " WHERE transaction_id = ?";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, transfer.getReceivingAccount().getAccountId());
			ps.setString(2, transfer.getStatus());
			ps.setInt(3, transfer.getTransactionId());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transfersUpdated;
	}

	@Override
	public int deleteTransfer(int id) {
		int transfersDeleted = 0;
		
		String sql = "DELETE FROM bank.transfers WHERE transaction_id = ?";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			
			transfersDeleted = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transfersDeleted;
	}

	@Override
	public List <Transfer> getCustomerPendingTransfers(Customer customer) {
		List<Transfer> transfers = new ArrayList <>();
		ResultSet rs = null;
		
		String sql = "SELECT acct_to.account_id as acct_to_id, acct_to.acc_type as acct_to_type, acct_to.balance as acct_to_balance, acct_to.acc_status as acct_to_status,"
				+ " acct_from.account_id as acct_from_id, acct_from.acc_type as acct_from_type, acct_from.balance as acct_from_balance, acct_from.acc_status as acct_from_status,"
				+ " tr.transaction_id, tr.amount, tr.transact_type, tr.message, tr.transaction_date_start, tr.transaction_date_complete,"
				+ " tf.trans_status"
				+ " FROM users.customers c INNER JOIN bank.customer_accounts ca ON c.customer_id = ca.customer_id"
				+ " INNER JOIN bank.account acct_to ON acct_to.account_id = ca.account_id"
				+ " INNER JOIN bank.transfers tf ON acct_to.account_id = tf.account_to_transfer"
				+ " INNER JOIN bank.transactions tr ON tf.transaction_id = tr.transaction_id"
				+ " INNER JOIN bank.account acct_from ON tr.account_id = acct_from.account_id"
				+ " WHERE c.customer_id = ? AND tf.trans_status = 'PENDING';";
		
		try (Connection c = ConnectionUtil.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)){
			
			ps.setInt(1, customer.getUserId());
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				String status = rs.getString("trans_status");
				
				int accountFromId = rs.getInt("acct_from_id");
				String accountFromType = rs.getString("acct_from_type");
				double accountFromBalance = rs.getDouble("acct_from_balance");
				String accountFromStatus = rs.getString("acct_from_status");
				
				int accountToId = rs.getInt("acct_to_id");
				String accountToType = rs.getString("acct_to_type");
				double accountToBalance = rs.getDouble("acct_to_balance");
				String accountToStatus = rs.getString("acct_to_status");
				
				int transactionId = rs.getInt("transaction_id");
				String transactionType = rs.getString("transact_type");
				double amount = rs.getDouble("amount");
				String message = rs.getString("message");
				Timestamp transactionStart = rs.getTimestamp("transaction_date_start");
				Timestamp transactionStop = rs.getTimestamp("transaction_date_complete");
				
				transfers.add(new Transfer(transactionId, transactionType, amount, new Account(accountFromId, accountFromType, accountFromBalance, accountFromStatus), message, transactionStart, transactionStop, status, new Account(accountToId, accountToType, accountToBalance, accountToStatus)));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transfers;
	}

	public int acceptTranfer(Transfer transfer, double newBalanceTo, double newBalanceFrom) {
		int transfersClosed = 0;
		
		String sql = " { call bank.accept_transfer(?, ?, ?, ?::numeric, ?::numeric) } ";
		
		try (Connection c = ConnectionUtil.getConnection();
				CallableStatement cs = c.prepareCall(sql)){
			
			cs.setInt(1, transfer.getTransactionId());
			cs.setInt(2, transfer.getReceivingAccount().getAccountId());
			cs.setInt(3, transfer.getAccount().getAccountId());
			cs.setDouble(4, newBalanceTo);
			cs.setDouble(5, newBalanceFrom);
			
			cs.execute();
			
			transfersClosed = 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transfersClosed;
	}
	
	public int denyTransfer(Transfer transfer) {
		// set transfer status to CANCELLED, set transaction message to TRANSFER CANCELLED
		int transfersClosed = 0;
		
		String sql = " { call bank.deny_transfer(?, ?) } ";
		
		try (Connection c = ConnectionUtil.getConnection();
				CallableStatement cs = c.prepareCall(sql)){
			
			cs.setInt(1, transfer.getTransactionId());
			cs.setString(2, transfer.getMessage());
			
			cs.execute();
			transfersClosed = 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transfersClosed;
	}
}
