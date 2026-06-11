package it.unisa.control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.exception.ServiceException;
import it.unisa.model.Cart;
import it.unisa.service.CartService;


public class CartControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CartControl.class.getName());


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Cart cart = (Cart)request.getSession().getAttribute("cart");
		if(cart == null)
		{
			cart = new Cart();
			request.getSession().setAttribute("cart", cart);
		}

		CartService cartService = new CartService();
		String action = request.getParameter("action");
		Boolean checkout = false;

		try {
			if (action != null) {
				if (action.equalsIgnoreCase("addC"))
				{
					int id = Integer.parseInt(request.getParameter("id"));
					cartService.addProduct(cart, id);

				} else if (action.equalsIgnoreCase("deleteC"))
					{
						int id = Integer.parseInt(request.getParameter("id"));
						cartService.deleteProduct(cart, id);
					}
					else if(action.equalsIgnoreCase("aggiorna"))
						{
							int id = Integer.parseInt(request.getParameter("id"));
							int quantita = Integer.parseInt(request.getParameter("quantita"));
							cartService.aggiorna(cart, id, quantita);
						}
						else if(action.equalsIgnoreCase("aggiornaCheck"))
							{
								int id = Integer.parseInt(request.getParameter("id"));
								int quantita = Integer.parseInt(request.getParameter("quantita"));
								cartService.aggiorna(cart, id, quantita);
								checkout = true;

							}
			}
		} catch (ServiceException e) {
			LOGGER.log(Level.SEVERE, "Errore nella gestione del carrello", e);
		}


		request.getSession().setAttribute("cart", cart);

		if(checkout)
		{
			response.sendRedirect("checkout.jsp");

		}
		else
			if(action.equalsIgnoreCase("checkout"))
				response.sendRedirect("checkout.jsp");
			else
				response.sendRedirect("CartView.jsp");
		}


		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doGet(request, response);
		}

}
