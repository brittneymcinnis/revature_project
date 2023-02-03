package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.util.ConnectionUtil;

public class AccountPostgres implements AccountDAO {

	@Override
	public List<Account> getAccounts() {
		List<Account> accounts = new ArrayList <>();
		String sql = "select * from bank.account;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			
			while (rs.next()) {
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("account_type");
				double balance = rs.getDouble("balance");
				String status = rs.getString("acc_status");

				accounts.add(new Account(accountId, accountType, balance, status));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return accounts;
	}

	@Override
	public Account getAccountById(int id) {
		ResultSet rs = null;
		Account account = null;
		
		String sql = "select * from bank.account where account_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("acc_type");
				double balance = rs.getDouble("balance");
				String status = rs.getString("acc_status");
				
				account = new Account(accountId, accountType, balance, status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return account;
	}

	@Override
	public int createAccount(Account account, int customerId) {
		int success = 0;
		String acctType = account.getType();
		
		String sql = " { call bank.create_new_customer_account(?::bank.account_type, ?)} ";
		
		try (
				Connection c = ConnectionUtil.getConnection();
				CallableStatement cs = c.prepareCall(sql)
			){
			cs.setString(1, acctType);
			cs.setInt(2, customerId);
			
			cs.execute();
			success = 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}

	/**
	 *
	 */
	@Override
	public int updateAccount(Account account) {
		int accountsUpdated = 0;
		
		String sql = "update bank.account set acc_type = ?::bank.account_type, balance = ?, acc_status = ?::bank.account_status WHERE account_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, account.getType());
			ps.setDouble(2, account.getBalance());
			ps.setString(3,  account.getStatus());
			ps.setInt(4, (int) account.getAccountId());
			
			accountsUpdated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountsUpdated;
	}

	@Override
	public int deleteAccount(int id) {
		int accountsDeleted = 0;
		
		String sql = "delete from bank.account where account_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()) {
			
			PreparedStatement ps = c.prepareStatement(sql);
			
			ps.setInt(1, id);

			accountsDeleted = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountsDeleted;
	}
	
	@Override
	public List<Account> getPendingAccounts(){
		List <Account> accounts = new ArrayList <>();
		ResultSet rs = null;
		
		String sql = "SELECT * FROM bank.account WHERE acc_status = 'PENDING';";
		
		try (Connection c = ConnectionUtil.getConnection();
				Statement stmt = c.createStatement()){
			
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				
				int accountId = rs.getInt("account_id");
				String accountType = rs.getString("acc_type");
				double balance = rs.getDouble("balance");
				String status = rs.getString("acc_status");
				
				accounts.add(new Account(accountId, accountType, balance, status));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accounts;
	}
}
