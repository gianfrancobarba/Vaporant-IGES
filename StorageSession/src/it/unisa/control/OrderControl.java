package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.AddressDaoImpl;
import it.unisa.model.Cart;
import it.unisa.model.ContenutoBean;
import it.unisa.model.ContenutoDaoImpl;
import it.unisa.model.OrderBean;
import it.unisa.model.OrderDaoImpl;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductModelDM;
import it.unisa.model.UserBean;
import it.unisa.model.UserDaoImpl;



public class OrderControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static OrderDaoImpl orderDao = new OrderDaoImpl();
	private static ContenutoDaoImpl contDao = new ContenutoDaoImpl();
	private static UserDaoImpl userDao = new UserDaoImpl();
	private static AddressDaoImpl addressDao = new AddressDaoImpl();
	private static ProductModelDM productDao = new ProductModelDM();
	
    public OrderControl() {
        super();
      
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	
		HttpSession session = req.getSession();
		
		Cart cart = (Cart) session.getAttribute("cart");
		UserBean user = (UserBean) session.getAttribute("user");
		
		int idUtente = user.getId();
		System.out.println("order " + user.getId());
		
		String payment = req.getParameter("payment");
		int idIndirizzo = Integer.parseInt(req.getParameter("addressDropdown"));
		int idIndirizzoFatt = Integer.parseInt(req.getParameter("addressDropdown2"));
		
		
		
		String indirizzoFatt = null;
		try {
			indirizzoFatt = addressDao.findAddressByID(idIndirizzoFatt).toStringScript();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			userDao.updateAddress(indirizzoFatt, user);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OrderBean order = new OrderBean(idUtente,idIndirizzo, cart.getPrezzoTotale(), LocalDate.now(), payment);
		
		try {
			orderDao.saveOrder(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int idOrdine = -1;
		
		try {
			idOrdine = orderDao.getIdfromDB();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		
		int i = 0;
		for(ProductBean prod : cart.getProducts())
		{
			try {
				contDao.saveContenuto(new ContenutoBean(idOrdine,prod.getCode(),prod.getQuantity(),22,prod.getPrice()));
				System.out.println("prodotto " +  i++ + prod.toString());
				productDao.updateQuantityStorage(prod, prod.getQuantityStorage() - prod.getQuantity());
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		session.setAttribute("order", order);
		session.setAttribute("user", user);
		session.setAttribute("listaProd", cart.getProducts());
		
		res.sendRedirect("ordine.jsp");
		
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			
		doGet(req, res);
	}

}
;