package com.revature.services;

import java.util.List;

import com.revature.dao.EmployeeDAO;
import com.revature.dao.EmployeePostgres;
import com.revature.models.Employee;

public class EmployeeService {
	public static EmployeeDAO emplDAO = new EmployeePostgres();
	
	public List<Employee> getEmployees (){
		return emplDAO.getEmployees();
	}
	
	public Employee getEmployeeById (int id) {
		return emplDAO.getEmployeeById(id);
	}
	
	public boolean createEmployee(Employee employee) {
		int result = emplDAO.createEmployee(employee);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean updateEmployee(Employee employee) {
		int result = emplDAO.updateEmployee(employee);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteEmployee(int id) {
		int result = emplDAO.deleteEmployee(id);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Employee authenticateEmployee(String username, String password) {
		return emplDAO.authenticateEmployee(username, password);
	}
}
