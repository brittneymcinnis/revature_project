package com.revature.dao;

import java.util.List;

import com.revature.models.Employee;

public interface EmployeeDAO {
	public List<Employee> getEmployees();
	public Employee getEmployeeById(int id);
	public int createEmployee(Employee employee);
	public int updateEmployee(Employee employee);
	public int deleteEmployee(int id);
	public Employee authenticateEmployee(String username, String password);
}
