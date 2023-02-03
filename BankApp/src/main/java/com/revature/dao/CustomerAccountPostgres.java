package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.util.ConnectionUtil;

public class CustomerAccountPostgres implements CustomerAccountDAO {

	@Override
	public List<Account> getCustomerAccounts(Customer customer) {
		List <Account> customerAccounts = new ArrayList <>();
		ResultSet rs = null;
		
		String sql = "SELECT * FROM bank.account acc INNER JOIN bank.customer_accounts ca ON acc.account_id = ca.account_id"
				+ " INNER JOIN users.customers c ON c.customer_id = ca.customer_id"
				+ " WHERE c.customer_id = ?;";
		
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, customer.getUserId());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("acc_type");
				double balance = rs.getDouble("balance");
				String status = rs.getString("acc_status");
				
				customerAccounts.add(new Account(accountId, accountType, balance, status));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return customerAccounts;
	}

	@Override
	public Account getCustomerAccountById(Customer customer, int customerAccountId) {
		Account customerAccount = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM bank.account a INNER JOIN bank.customer_accounts ca"
				+ " ON a.account_id = ca.account_id"
				+ " INNER JOIN users.customers c ON c.customer_id = ca.customer_id"
				+ " WHERE c.customer_id = ? AND a.account_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, customer.getUserId());
			ps.setInt(2, customerAccountId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("acc_type");
				double balance = rs.getDouble("balance");
				String status = rs.getString("acc_status");
			
				customerAccount = new Account(accountId, accountType, balance, status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return customerAccount;
	}

	@Override
	public int createCustomerAccount(Customer customer, Account account) {
		int customerAccountsCreated = 0;
		
		String sql = "INSERT INTO bank.customer_accounts (customer_id, account_id)"
				+ " VALUES (?, ?);";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, customer.getUserId());
			ps.setInt(2, account.getAccountId());
			
			customerAccountsCreated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customerAccountsCreated;
	}

	@Override
	public int updateCustomerAccount(Customer customer, Account account) {
		
		int customerAccountsUpdated = 0;
		
		String sql = "UPDATE bank.customer_accounts SET customer_id = ?, account_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, customer.getUserId());
			ps.setInt(2, account.getAccountId());
			
			customerAccountsUpdated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customerAccountsUpdated;
	}

	@Override
	public int deleteCustomerAccount(Customer customer, Account account) {
		
		int customerAccountsDeleted = 0;
		
		String sql = "DELETE FROM bank.customer_accounts WHERE customer_id = ? AND account_id = ?";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, customer.getUserId());
			ps.setInt(2, account.getAccountId());
			
			customerAccountsDeleted = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customerAccountsDeleted;
		
	}
	
	

}
