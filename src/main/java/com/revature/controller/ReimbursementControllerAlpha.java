package com.revature.controller;

import java.time.LocalDateTime;

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
			return "/login.html";
		} else if (request.getMethod() == "GET") {
			return  "/submitReimbursement.html";
		} else {
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
				return new ClientMessage("Sumbission successful");
			}
		}
		return new ClientMessage("Submission failed");
	}

	@Override
	public Object singleRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
