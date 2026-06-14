package it.unisa.control;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.exception.ServiceException;
import it.unisa.model.UserBean;
import it.unisa.service.UserService;

public class ModifyControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ModifyControl.class.getName());

public ModifyControl() {
	super();
}
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        UserBean user =  (UserBean) request.getSession().getAttribute("user");
        UserService userService = new UserService();

        if (action.equals("modificaEmail")) {
            String nuovaMail = request.getParameter("nuovaEmail");
            if (!userService.isValidEmail(nuovaMail)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
            	UserBean updated = userService.updateEmail(user, nuovaMail);
            	request.getSession().setAttribute("user", updated);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{ \"email\": \"" + updated.getEmail() + "\" }");
                out.flush();
			} catch (ServiceException e) {
				LOGGER.log(Level.SEVERE, "Errore nella modifica dell'email", e);
			}
        } else if (action.equals("modificaTelefono")) {
            String nuovoTelefono = request.getParameter("nuovoTelefono");
            if (!userService.isValidTelefono(nuovoTelefono)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
				UserBean updated = userService.updateTelefono(user, nuovoTelefono);
				request.getSession().setAttribute("user", updated);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{ \"numTelefono\": \"" + updated.getNumTelefono() + "\" }");
                out.flush();
			} catch (ServiceException e){
				LOGGER.log(Level.SEVERE, "Errore nella modifica del telefono", e);
			}
        }else if(action.equals("modificaPassword")){
        	 String nuovaPsw = request.getParameter("nuovaPassword");
        	 String vecchiaPsw = request.getParameter("vecchiaPassword");

        	    if (!userService.isValidNewPassword(nuovaPsw)) {
        	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	        return;
        	    }

        	    try {
        	        boolean success = userService.updatePassword(user, nuovaPsw, vecchiaPsw);
        	        if (success) {
        	            response.setStatus(HttpServletResponse.SC_OK);
        	        }
        	        String jsonResponse = "{\"success\": " + success + "}";

        	        // Impostazione dei corretti header della risposta JSON
        	        response.setContentType("application/json");
        	        response.setCharacterEncoding("UTF-8");

        	        // Scrittura del JSON come risposta
        	        try (PrintWriter out = response.getWriter()) {
        	            out.print(jsonResponse);
        	        }
				} catch (ServiceException e) {
						LOGGER.log(Level.SEVERE, "Errore nella modifica della password", e);
				}
        	}else {
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	doGet(request, response);
}
}
