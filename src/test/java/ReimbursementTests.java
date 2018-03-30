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
import com.revature.repository.ReimbursementRepository;
import com.revature.repository.ReimbursementRepositoryJdbc;
import com.revature.service.ReimbursementService;
import com.revature.service.ReimbursementServiceAlpha;

public class ReimbursementTests {

	private static ReimbursementService reimbursementService = ReimbursementServiceAlpha.getInstance();
	private static ReimbursementRepository reimbursementRepository = ReimbursementRepositoryJdbc.getInstance();
	
	private static Employee employee = new Employee(
			88,
			"Zelda",
			"Zebra",
			"zeldaz",
			"p4ssw0rd",
			"zeldaz@gmail.com",
			new EmployeeRole(1,"EMPLOYEE")
			);
	
	private static Reimbursement databaseReimbursement;
	
	private static Reimbursement reimbursement = new Reimbursement(
			99,
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
	public static void insertReimbursement() {
		reimbursementRepository.update(reimbursement);
		databaseReimbursement = reimbursementService.getUserPendingRequests(employee).iterator().next();
	}
	
	@After
	public void refreshReimbursement() {
		reimbursementRepository.update(reimbursement);
		databaseReimbursement = reimbursementService.getSingleRequest(reimbursement);
	}
	
	@Test
	public void approveReimbursement() {
		databaseReimbursement.setStatus(new ReimbursementStatus(3, "APPROVED"));
		assertEquals(true, reimbursementService.finalizeRequest(databaseReimbursement));
	}
	
	@Test
	public void denyReimbursement() {
		databaseReimbursement.setStatus(new ReimbursementStatus(2, "DECLINED"));
		assertEquals(true, reimbursementService.finalizeRequest(databaseReimbursement));
	}
	
	@Test
	public void finalizedInvalidReimbursement() {
		databaseReimbursement.setStatus(new ReimbursementStatus(2, "DECLINED"));
		databaseReimbursement.setId(0);
		assertEquals(false, reimbursementService.finalizeRequest(databaseReimbursement));
	}
	
	@Test
	public void getUserPendingRequests() {
		assertEquals(true, reimbursementService.getUserPendingRequests(employee) != null);
	}
	
	@Test
	public void getUserFinalizedRequests() {
		assertEquals(true, reimbursementService.getUserFinalizedRequests(employee) != null);
	}
	
	@Test
	public void getInvalidPendingRequests() {
		Employee invalidEmployee = new Employee(0);
		assertEquals(true, reimbursementService.getUserPendingRequests(invalidEmployee).isEmpty());
	}
	
	@Test
	public void getInvalidFinalizedRequests() {
		Employee invalidEmployee = new Employee(0);
		assertEquals(true, reimbursementService.getUserFinalizedRequests(invalidEmployee).isEmpty());
	}
	
	@Test
	public void getAllPendingRequests() {
		assertEquals(true, reimbursementService.getAllPendingRequests() != null);
	}
	
	@Test
	public void getAllResolvedRequests() {
		assertEquals(true, reimbursementService.getAllResolvedRequests() != null);
	}
	
	@Test
	public void getTypes() {
		assertEquals(true, reimbursementService.getReimbursementTypes().size() == 4);
	}
}
