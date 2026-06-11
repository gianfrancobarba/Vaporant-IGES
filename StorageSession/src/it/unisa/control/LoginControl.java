package it.unisa.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.exception.ServiceException;
import it.unisa.model.AddressBean;
import it.unisa.model.Cart;
import it.unisa.model.OrderBean;
import it.unisa.model.UserBean;
import it.unisa.service.AddressService;
import it.unisa.service.OrderService;
import it.unisa.service.UserService;

public class LoginControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LoginControl.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String email = req.getParameter("email");
	String password = req.getParameter("password");
	String action = (String) req.getSession().getAttribute("action");
	Cart cart = (Cart) req.getSession().getAttribute("cart");

	UserService userService = new UserService();
	UserBean user = null;

	try {
		user = userService.authenticate(email, password);
	} catch (ServiceException e) {
		LOGGER.log(Level.SEVERE, "Errore durante l'autenticazione", e);
	}

	if(user != null){

		HttpSession session = req.getSession(false);
		if(session != null){
			session.invalidate();
		}

		HttpSession currentSession = req.getSession();
		currentSession.setAttribute("user", user);
		currentSession.setAttribute("tipo", user.getTipo());
		currentSession.setAttribute("cart", cart);

		// carica in sessione gli indirizzi e lo storico ordini dell'utente
		ArrayList<AddressBean> indirizzi = new ArrayList<>();
		ArrayList<OrderBean> ordini = new ArrayList<>();
		try {
			AddressService addressService = new AddressService();
			OrderService orderService = new OrderService();
			indirizzi = addressService.findByUserId(user.getId());
			ordini = orderService.findByIdUtente(user.getId());
		} catch (ServiceException e) {
			LOGGER.log(Level.SEVERE, "Errore nel caricamento di indirizzi/ordini in sessione", e);
		}
		currentSession.setAttribute("indirizzi", indirizzi);
		currentSession.setAttribute("ordini", ordini);

		if(action != null && action.equalsIgnoreCase("checkout"))
			resp.sendRedirect("checkout.jsp");
		else if(user.getTipo().equalsIgnoreCase("admin"))
					resp.sendRedirect("ProductViewAdmin.jsp");
			else
				resp.sendRedirect("ProductView.jsp");

		}
		else {
		resp.sendRedirect("loginForm.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doGet(req,resp);
	}
}
