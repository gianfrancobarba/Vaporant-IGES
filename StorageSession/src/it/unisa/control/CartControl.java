package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.model.Cart;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductModelDM;
import it.unisa.model.UserBean;


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
		
		ProductModelDM model=new ProductModelDM();
		String action = request.getParameter("action");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Boolean checkout = false;
		
		try {
			if (action != null) {
				if (action.equalsIgnoreCase("addC"))
				{
					int id = Integer.parseInt(request.getParameter("id"));
					ProductBean prod = model.doRetrieveByKey(id);
					if (prod != null) {
						cart.addProduct(prod);
					}

				} else if (action.equalsIgnoreCase("deleteC"))
					{
						int id = Integer.parseInt(request.getParameter("id"));
						ProductBean prod = model.doRetrieveByKey(id);
						if (prod != null) {
							cart.deleteProduct(prod);
						}
					}
					else if(action.equalsIgnoreCase("aggiorna"))
						{
							int id = Integer.parseInt(request.getParameter("id"));
							int quantita = Integer.parseInt(request.getParameter("quantita"));
							ProductBean prod = model.doRetrieveByKey(id);
							if (prod != null) {
								cart.aggiorna(prod, quantita);
							}
						}
						else if(action.equalsIgnoreCase("aggiornaCheck"))
							{
								int id = Integer.parseInt(request.getParameter("id"));
								int quantita = Integer.parseInt(request.getParameter("quantita"));
								ProductBean prod = model.doRetrieveByKey(id);
								if (prod != null) {
									cart.aggiorna(prod, quantita);
								}
								checkout = true;

							}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Errore nella gestione del carrello", e);
		}
		

		request.getSession().setAttribute("user", user);
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
