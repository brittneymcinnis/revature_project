package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Employee;
import com.revature.util.ConnectionUtil;

public class EmployeePostgres implements EmployeeDAO {

	@Override
	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList <>();
		String sql = "select * from users.employees";
		
		try (Connection c = ConnectionUtil.getConnection()){
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			
			while (rs.next()) {
				int employeeId = rs.getInt("employee_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String userName = rs.getString("username");
				String password = rs.getString("pass_word");
				String email = rs.getString("email");
				employees.add(new Employee(employeeId, firstName, lastName, userName, password, email));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return employees;
	}

	@Override
	public Employee getEmployeeById(int id) {
		ResultSet rs = null;
		Employee employee = null;
		
		String sql = "select * from users.customers where customer_id = ?";
		
		try (Connection c = ConnectionUtil.getConnection()){
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int employeeId = rs.getInt("employee_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String userName = rs.getString("username");
				String password = rs.getString("pass_word");
				String email = rs.getString("email");
				employee = new Employee(employeeId, firstName, lastName, userName, password, email);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employee;
	}

	@Override
	public int createEmployee(Employee employee) {
		int employeesCreated = 0;
		
		String sql = "insert into users.employees (first_name, last_name, username, pass_word, email) values (?, ?, ?, ?, ?)";
		
		try (Connection c = ConnectionUtil.getConnection()){
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, employee.getFirstName());
			ps.setString(2, employee.getLastName());
			ps.setString(3, employee.getUserName());
			ps.setString(4, employee.getPassword());
			ps.setString(5, employee.getEmail());
			
			employeesCreated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employeesCreated;
	}

	@Override
	public int updateEmployee(Employee employee) {
		int employeesUpdated = 0;
		
		String sql = "update users.employees set first_name = ?, last_name = ?, username = ?, pass_word = ?, email = ? WHERE employee_id = ?";
		
		try (Connection c = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, employee.getFirstName());
			ps.setString(2, employee.getLastName());
			ps.setString(3, employee.getUserName());
			ps.setString(4, employee.getPassword());
			ps.setString(5, employee.getEmail());
			ps.setInt(6, employee.getUserId());
			
			employeesUpdated = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employeesUpdated;
	}

	@Override
	public int deleteEmployee(int id) {
		int employeesDeleted = 0;
		
		String sql = "delete from users.employees where employee_id = ?";
		
		try (Connection c = ConnectionUtil.getConnection()) {
			
			PreparedStatement ps = c.prepareStatement(sql);
			
			ps.setInt(1, id);

			employeesDeleted = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employeesDeleted;
	}

	public Employee authenticateEmployee(String username, String password) {
		Employee employee = null;
		ResultSet rs = null;
		
		String sql = "SELECT employee_id, first_name, last_name, email FROM users.employees WHERE username = ? AND pass_word = ?;";
		
		try (Connection c = ConnectionUtil.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)){
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int employeeId = rs.getInt("employee_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				employee = new Employee(employeeId, firstName, lastName, username, password, email);
			}
			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employee;
	}
}
