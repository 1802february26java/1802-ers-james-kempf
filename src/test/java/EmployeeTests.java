import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.EmployeeService;
import com.revature.service.EmployeeServiceAlpha;

public class EmployeeTests {
	
	private EmployeeService employeeService = EmployeeServiceAlpha.getInstance();
	
	Employee employee = new Employee(
			"Zelda",
			"Zebra",
			"ZeldaZ",
			"p4ssw0rd",
			"ZeldaZ@gmail.com",
			new EmployeeRole(1,"EMPLOYEE")
			);
	
	Employee invalidEmployee = new Employee(
			"Zelda",
			"Zebra",
			"ZeldaZ",
			"Invalid",
			"ZeldaZ@gmail.com",
			new EmployeeRole(1,"EMPLOYEE")
			);
	
	Employee databaseEmployee;
	
	Reimbursement reimbursement = new Reimbursement(
			0,
			LocalDateTime.now(),
			null,
			100,
			"Lorem Ipsum",
			employee,
			null,
			new ReimbursementStatus(1, "PENDING"),
			new ReimbursementType(1, "OTHER")
			);
	
	@BeforeClass
	public void registerUser() {
		employeeService.createEmployee(employee);
		dataBaseEmployee = employeeService.getEmployeeInformation(employee);
	}
	
	@After
	public void deleteUser() {
		employeeService.updateEmployeeInformation(employee);
	}
	
	@Test
	public void validAuthentication() {
		assertEquals(employee, employeeService.authenticate(employee));
	}
	
	@Test
	public void invalidAuthentication() {
		assertEquals(null, employeeService.authenticate(invalidEmployee));
	}
}
