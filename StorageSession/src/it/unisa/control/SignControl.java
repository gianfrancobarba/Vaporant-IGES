package it.unisa.control;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.exception.ServiceException;
import it.unisa.model.UserBean;
import it.unisa.service.UserService;

public class SignControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SignControl.class.getName());

    public SignControl() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	UserBean user = new UserBean();

		user.setNome(request.getParameter("nome"));
		user.setCognome(request.getParameter("cognome"));
		user.setDataNascita(LocalDate.parse(request.getParameter("data_nascita")));
		user.setCodF(request.getParameter("codice_fiscale"));
		user.setNumTelefono(request.getParameter("telefono"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		user.setIndirizzoFatt(request.getParameter("indirizzoFatt"));

		UserService userService = new UserService();
		boolean registered = false;

		if (userService.isValidRegistration(user)) {
			try {
				registered = userService.register(user);
			} catch (ServiceException e) {
				LOGGER.log(Level.SEVERE, "Errore nella registrazione dell'utente", e);
			}
		}

		if(registered)
			response.sendRedirect("loginForm.jsp");
		else
			response.sendRedirect("SignForm.jsp");
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
