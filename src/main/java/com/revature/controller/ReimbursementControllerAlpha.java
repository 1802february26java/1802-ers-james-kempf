package com.revature.controller;

import java.time.LocalDateTime;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.ReimbursementService;
import com.revature.service.ReimbursementServiceAlpha;

public class ReimbursementControllerAlpha implements ReimbursementController {

	private ReimbursementService reimbursementService = ReimbursementServiceAlpha.getInstance();
	private static Logger logger = Logger.getLogger(ReimbursementControllerAlpha.class);
	
	@Override
	public Object submitRequest(HttpServletRequest request) {
		logger.trace("Submiting Request");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "login.html";
		} else if (loggedEmployee.getEmployeeRole().getId() == 2) {
			return "403.html";
		} else if (request.getMethod() == "GET") {
			return "submit-reimbursement.html";
		} else {
			try {
				Double.parseDouble(request.getParameter("amount"));
			} catch (NumberFormatException e) {
				return new ClientMessage("Invalid amount", false);
			}
			try {
				Integer.parseInt(request.getParameter("type"));
			} catch (NumberFormatException e) {
				return new ClientMessage("Please select a type", false);
			}
			logger.trace(loggedEmployee.toString());
			Reimbursement reimbursement = new Reimbursement(
					0,
					LocalDateTime.now(),
					null,
					Double.parseDouble(request.getParameter("amount")),
					request.getParameter("description"),
					loggedEmployee,
					null,
					new ReimbursementStatus(1),
					new ReimbursementType(Integer.parseInt(request.getParameter("type")))
					);
			if (reimbursementService.submitRequest(reimbursement)) {
				return new ClientMessage("Submission successful", true);
			}
		}
		return new ClientMessage("Submission failed", false);
	}

	@Override
	public Object singleRequest(HttpServletRequest request) {
		logger.trace("Getting single request");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "/login.html";
		} else {
			Reimbursement reimbursement = reimbursementService.getSingleRequest(
					new Reimbursement(Integer.parseInt(request.getParameter("id")))
					);
			if (reimbursement == null
					|| (loggedEmployee.getEmployeeRole().getId() != 2
							&& loggedEmployee.getId() != reimbursement.getRequester().getId()
							)) {
				return new ClientMessage("Reimbursement not found", false);
			} else {
				return reimbursement;
			}
		}
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		logger.trace("Getting multiple request");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "/login.html";
		} else {
			Set<Reimbursement> reimbursements = null;
			switch (request.getParameter("list")) {
			case "current":
				reimbursements = reimbursementService.getUserPendingRequests(loggedEmployee);
				reimbursements.addAll(reimbursementService.getUserFinalizedRequests(loggedEmployee));
				break;
			case "pending":
				reimbursements = reimbursementService.getUserPendingRequests(loggedEmployee);
				break;
			case "finalized":
				reimbursements = reimbursementService.getUserFinalizedRequests(loggedEmployee);
				break;
			case "userPending":
				if (loggedEmployee.getEmployeeRole().getId() == 2) {
					Employee employee = new Employee(Integer.parseInt(request.getParameter("id")));
					logger.trace(employee.toString());
					reimbursements = reimbursementService.getUserPendingRequests(employee);
				}
				break;
			case "userFinalized":
				if (loggedEmployee.getEmployeeRole().getId() == 2) {
					Employee employee = new Employee(Integer.parseInt(request.getParameter("id")));
					reimbursements = reimbursementService.getUserFinalizedRequests(employee);
				}
				break;
			case "allPending":
				if (loggedEmployee.getEmployeeRole().getId() == 2) {
					reimbursements = reimbursementService.getAllPendingRequests();
				}
				break;
			case "allFinalized":
				if (loggedEmployee.getEmployeeRole().getId() == 2) {
					reimbursements = reimbursementService.getAllResolvedRequests();
				}
				break;
			}
			if (reimbursements == null) {
				return new ClientMessage("Reimbursements not found", false);
			} else {
				return reimbursements;
			}
		}
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		logger.trace("Finalizing request");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "/login.html";
		} else if (loggedEmployee.getEmployeeRole().getId() != 2) {
			return "/403.html";
		} else {
			Reimbursement reimbursement = reimbursementService.getSingleRequest(
					new Reimbursement(Integer.parseInt(request.getParameter("id")))
					);
			if (reimbursement == null) {
				return new ClientMessage("Reimbursement not found", false);
			} else {
				System.out.println(reimbursement.toString());
				reimbursement.setApprover(loggedEmployee);
				reimbursement.setResolved(LocalDateTime.now());
				reimbursement.setStatus(new ReimbursementStatus(Integer.parseInt(request.getParameter("status"))));
				System.out.println(reimbursement.toString());
				if (reimbursementService.finalizeRequest(reimbursement)) {
					return new ClientMessage("Request finalized", true);
				} else {
					return new ClientMessage("Finalization failed", false);
				}
			}
		}
	}

	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		logger.trace("Getting request types");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "/login.html";
		} else {
			Set<ReimbursementType> types = reimbursementService.getReimbursementTypes();
			return types;
		}
	}

}
