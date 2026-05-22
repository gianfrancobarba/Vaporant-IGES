package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;

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
					cart.addProduct(prod);
										
				} else if (action.equalsIgnoreCase("deleteC")) 
					{
						int id = Integer.parseInt(request.getParameter("id"));
						cart.deleteProduct(model.doRetrieveByKey(id));
					}
					else if(action.equalsIgnoreCase("aggiorna"))
						{
							int id = Integer.parseInt(request.getParameter("id"));
							int quantita = Integer.parseInt(request.getParameter("quantita"));
							cart.aggiorna(model.doRetrieveByKey(id),quantita);
						}
						else if(action.equalsIgnoreCase("aggiornaCheck"))
							{
								int id = Integer.parseInt(request.getParameter("id"));
								int quantita = Integer.parseInt(request.getParameter("quantita"));
								cart.aggiorna(model.doRetrieveByKey(id),quantita);
								checkout = true;
								
							}
			}
		} catch (SQLException e) {
			System.out.println("Error:" + e.getMessage());
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
