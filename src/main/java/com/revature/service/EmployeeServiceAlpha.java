package com.revature.service;

import java.util.Set;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepository;
import com.revature.repository.EmployeeRepositoryJdbc;

public class EmployeeServiceAlpha implements EmployeeService {

	@Override
	public Employee authenticate(Employee employee) {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		return null;
	}

	@Override
	public Employee getEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		Set<Employee> employees = repository.selectAll();
		return employees;
	}

	@Override
	public boolean createEmployee(Employee employee) {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		boolean inserted = repository.insert(employee);
		return inserted;
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		boolean updated = repository.update(employee);
		return updated;
	}

	@Override
	public boolean updatePassword(Employee employee) {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		boolean updated = repository.update(employee);
		return updated;
	}

	@Override
	public boolean isUsernameTaken(Employee employee) {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		boolean taken = (repository.select(employee.getUsername()) != null);
		return taken;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		EmployeeToken employeeToken = new EmployeeToken();
		boolean created = repository.insertEmployeeToken(employeeToken);
		return false;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		EmployeeService service = new EmployeeServiceAlpha();
		System.out.println(service.getAllEmployeesInformation());
	}
}
