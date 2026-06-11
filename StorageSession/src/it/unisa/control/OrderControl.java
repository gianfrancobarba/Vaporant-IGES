package it.unisa.control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.exception.ServiceException;
import it.unisa.model.Cart;
import it.unisa.model.OrderBean;
import it.unisa.model.UserBean;
import it.unisa.service.OrderService;

public class OrderControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(OrderControl.class.getName());

    public OrderControl() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession();

		Cart cart = (Cart) session.getAttribute("cart");
		UserBean user = (UserBean) session.getAttribute("user");

		String payment = req.getParameter("payment");
		int idIndirizzo = Integer.parseInt(req.getParameter("addressDropdown"));
		int idIndirizzoFatt = Integer.parseInt(req.getParameter("addressDropdown2"));

		OrderService orderService = new OrderService();
		OrderBean order = null;

		try {
			order = orderService.checkout(user, cart, idIndirizzo, idIndirizzoFatt, payment);
			// sincronizza la sessione: Utente.jsp legge "ordini" dalla sessione
			session.setAttribute("ordini", orderService.findByIdUtente(user.getId()));
		} catch (ServiceException e) {
			LOGGER.log(Level.SEVERE, "Errore durante il checkout", e);
		}

		session.setAttribute("order", order);
		session.setAttribute("listaProd", cart.getProducts());

		res.sendRedirect("ordine.jsp");

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		doGet(req, res);
	}

}
