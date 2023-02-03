package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Customer;
import com.revature.util.ConnectionUtil;

public class CustomerPostgres implements CustomerDAO{

	@Override
	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList <>();
		String sql = "select * from users.customers";
		
		try (Connection c = ConnectionUtil.getConnection()){
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			
			while (rs.next()) {
				int customerId = rs.getInt("customer_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String userName = rs.getString("username");
				String password = rs.getString("pass_word");
				String email = rs.getString("email");
				
				customers.add(new Customer(customerId, firstName, lastName, userName, password, email));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return customers;
	}

	@Override
	public Customer getCustomerById(int id) {
		ResultSet rs = null;
		Customer customer = null;
		
		String sql = "select * from users.customers where customer_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int customerId = rs.getInt("customer_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String userName = rs.getString("username");
				String password = rs.getString("pass_word");
				String email = rs.getString("email");
				customer = new Customer(customerId, firstName, lastName, userName, password, email);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customer;
	}

	@Override
	public int createCustomer(Customer customer) {
		int customersCreated = 0;
		
		String sql = "insert into users.customers (first_name, last_name, username, pass_word, email) values (?, ?, ?, ?, ?);";
		
		try (Connection c = ConnectionUtil.getConnection()){
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getUserName());
			ps.setString(4, customer.getPassword());
			ps.setString(5, customer.getEmail());
			
			customersCreated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customersCreated;
	}

	@Override
	public int updateCustomer(Customer customer) {
		int customersUpdated = 0;
		
		String sql = "update users.customers set first_name = ?, last_name = ?, username = ?, pass_word = ?, email = ? WHERE customer_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getUserName());
			ps.setString(4, customer.getPassword());
			ps.setString(5, customer.getEmail());
			ps.setInt(6, customer.getUserId());
			
			customersUpdated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customersUpdated;
	}

	@Override
	public int deleteCustomer(int id) {
		int customersDeleted = 0;
		
		String sql = "delete from users.customers where customer_id = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()) {
			
			PreparedStatement ps = c.prepareStatement(sql);
			
			ps.setInt(1, id);

			customersDeleted = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customersDeleted;
	}

	@Override
	public Customer authenticateCustomer(String username, String password) {
		Customer customer = null;
		ResultSet rs = null;
		
		String sql = "SELECT customer_id, first_name, last_name, email FROM users.customers WHERE username = ? AND pass_word = ?;";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int customerId = rs.getInt("customer_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				customer = new Customer(customerId, firstName, lastName, username, password, email);
			}
			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customer;
	}

}
