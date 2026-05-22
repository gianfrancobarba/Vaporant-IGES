package it.unisa.control;
import java.io.IOException;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.UserBean;
import it.unisa.model.UserDaoImpl;

public class ModifyControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
public ModifyControl() {
	super();
}
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        UserBean user =  (UserBean) request.getSession().getAttribute("user");
        UserDaoImpl need = new UserDaoImpl();
        
        if (action.equals("modificaEmail")) {
            String nuovaMail =(String) request.getParameter("nuovaEmail");
            try {
            	need.modifyMail(user, nuovaMail);
            	user = need.findById(user.getId());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{ \"email\": \"" + user.getEmail() + "\" }");
                out.flush();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        } else if (action.equals("modificaTelefono")) {
            String nuovoTelefono = request.getParameter("nuovoTelefono");
            try {
				need.modifyTelefono(user, nuovoTelefono);
				user = need.findById(user.getId());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("{ \"numTelefono\": \"" + user.getNumTelefono() + "\" }");
                out.flush();
			} catch (SQLException e){
				e.printStackTrace();
			}
        }else if(action.equals("modificaPassword")){
        	 String nuovaPsw = request.getParameter("nuovaPassword");
        	 String vecchiaPsw = request.getParameter("vecchiaPassword");

        	    try {
        	    	boolean success = true;
        	        if (need.modifyPsw(nuovaPsw, vecchiaPsw, user) == 0) {
        	            success = false;
        	        } else {
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
				} catch (SQLException e) {
						e.printStackTrace();
				}
        	}else {
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	doGet(request, response);
}
}
