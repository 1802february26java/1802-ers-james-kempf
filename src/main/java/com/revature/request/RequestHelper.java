package com.revature.request;

import javax.servlet.http.HttpServletRequest;

import com.revature.controller.EmployeeInformationControllerAlpha;
import com.revature.controller.ErrorControllerAlpha;
import com.revature.controller.HomeControllerAlpha;
import com.revature.controller.LoginControllerAlpha;
import com.revature.controller.ReimbursementControllerAlpha;

/**
 * The RequestHelper class is consulted by the MasterServlet and provides
 * him with a view URL or actual data that needs to be transferred to the
 * client.
 * 
 * It will execute a controller method depending on the requested URI.
 * 
 * Recommended to change this logic to consume a ControllerFactory.
 * 
 * @author Revature LLC
 */
public class RequestHelper {
	private static RequestHelper requestHelper;

	private RequestHelper() {}

	public static RequestHelper getRequestHelper() {
		if(requestHelper == null) {
			return new RequestHelper();
		}
		else {
			return requestHelper;
		}
	}
	
	/**
	 * Checks the URI within the request object passed by the MasterServlet
	 * and executes the right controller with a switch statement.
	 * 
	 * @param request
	 * 		  The request object which contains the solicited URI.
	 * @return A String containing the URI where the user should be
	 * forwarded, or data (any object) for AJAX requests.
	 */
	public Object process(HttpServletRequest request) {
		switch(request.getRequestURI())
		{
		case "/ERS/login.do":
			return new LoginControllerAlpha().login(request);
		case "/ERS/logout.do":
			return new LoginControllerAlpha().logout(request);
		case "/ERS/home.do":
			return new HomeControllerAlpha().showEmployeeHome(request);
		case "/ERS/register.do":
			return new EmployeeInformationControllerAlpha().registerEmployee(request);
		case "/ERS/update-employee.do":
			return new EmployeeInformationControllerAlpha().updateEmployee(request);
		case "/ERS/employee-info.do":
			return new EmployeeInformationControllerAlpha().viewEmployeeInformation(request);
		case "/ERS/list-employees.do":
			return new EmployeeInformationControllerAlpha().viewAllEmployees(request);
		case "/ERS/username-taken.do":
			return new EmployeeInformationControllerAlpha().usernameExists(request);
		case "/ERS/submit-reimbursement.do":
			return new ReimbursementControllerAlpha().submitRequest(request);
		case "/ERS/select-reimbursement.do":
			return new ReimbursementControllerAlpha().singleRequest(request);
		case "/ERS/select-multiple-reimbursement.do":
			return new ReimbursementControllerAlpha().multipleRequests(request);
		case "/ERS/finalize.do":
			return new ReimbursementControllerAlpha().finalizeRequest(request);
		case "/ERS/types.do":
			return new ReimbursementControllerAlpha().getRequestTypes(request);
		default:
			return new ErrorControllerAlpha().showError(request);
		}
	}
}
