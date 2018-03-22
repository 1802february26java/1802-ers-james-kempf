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
	
	private static EmployeeService employeeService = EmployeeServiceAlpha.getInstance();
	
	private static Employee employee = new Employee(
			"Zelda",
			"Zebra",
			"zeldaz",
			"p4ssw0rd",
			"zeldaz@gmail.com",
			new EmployeeRole(1,"EMPLOYEE")
			);
	
	private static Employee databaseEmployee;
	
	private static Reimbursement reimbursement = new Reimbursement(
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
	public static void registerUser() {
		employeeService.createEmployee(employee);
		databaseEmployee = employeeService.getEmployeeInformation(employee);
	}
	
	@After
	public void refreshUser() {
		employeeService.updateEmployeeInformation(employee);
	}
	
	@Test
	public void validAuthentication() {
		assertEquals(true, employeeService.authenticate(employee) != null);
	}
	
	@Test
	public void invalidAuthentication() {
		Employee invalidEmployee = new Employee(
				"Zelda",
				"Zebra",
				"zeldaz",
				"Invalid",
				"zeldaz@gmail.com",
				new EmployeeRole(1,"EMPLOYEE")
				);
		assertEquals(null, employeeService.authenticate(invalidEmployee));
	}
	
	@Test
	public void selectEmployeeById() {
		assertEquals(true, employeeService.getEmployeeInformation(databaseEmployee) != null);
	}
	
	@Test
	public void selectEmployeeByUsername() {
		assertEquals(true, employeeService.getEmployeeInformation(employee) != null);
	}
	
	@Test
	public void selectInvalidEmployee() {
		Employee invalidEmployee = new Employee(
				"Zelda",
				"Zebra",
				"Invalid",
				"p4ssw0rd",
				"zeldaz@gmail.com",
				new EmployeeRole(1,"EMPLOYEE")
				);
		assertEquals(null, employeeService.getEmployeeInformation(invalidEmployee));
	}
	
	@Test
	public void selectAllEmployees() {
		assertEquals(false, employeeService.getAllEmployeesInformation().contains(employee));
	}
	
	@Test
	public void updateEmployeeInformation() {
		Employee updatedEmployee = new Employee(
				databaseEmployee.getId(),
				"newFirst",
				"newLast",
				"zeldaz",
				"p4ssw0rd",
				"new@gmail.com",
				new EmployeeRole(2,"MANAGER")
				);
		assertEquals(true, employeeService.updateEmployeeInformation(updatedEmployee));
	}
	
	@Test
	public void updateInvalidEmplyee() {
		Employee invalidEmployee = new Employee(
				0,
				"Invalid",
				"Zebra",
				"Invalid",
				"p4ssw0rd",
				"zeldaz@gmail.com",
				new EmployeeRole(1,"EMPLOYEE")
				);
		assertEquals(false, employeeService.updateEmployeeInformation(invalidEmployee));
	}
	
	@Test
	public void updatePassword() {
		Employee updatedEmployee = new Employee(
				databaseEmployee.getId(),
				"Zelda",
				"Zebra",
				"zeldaz",
				"newp4ss0wrd",
				"zeldaz@gmail.com",
				new EmployeeRole(1,"EMPLOYEE")
				);
		assertEquals(true, employeeService.updatePassword(updatedEmployee));
	}
	
	@Test
	public void invalidUpdatePassword() {
		Employee updatedEmployee = new Employee(
				0,
				"Zelda",
				"Zebra",
				"zeldaz",
				"newp4ss0wrd",
				"zeldaz@gmail.com",
				new EmployeeRole(1,"EMPLOYEE")
				);
		assertEquals(false, employeeService.updatePassword(updatedEmployee));
	}
	
	@Test
	public void usernameTaken() {
		assertEquals(true, employeeService.isUsernameTaken(employee));
	}
	
	@Test
	public void usernameNotTaken() {
		Employee newEmployee = new Employee(
				"Zelda",
				"Zebra",
				"NOTTAKEN",
				"p4ssw0rd",
				"zeldaz@gmail.com",
				new EmployeeRole(1,"EMPLOYEE")
				);
		assertEquals(false, employeeService.isUsernameTaken(newEmployee));
	}
}
