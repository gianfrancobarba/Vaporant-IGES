package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductModelDM;

public class DetailsControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DetailsControl.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProductModelDM model = new ProductModelDM();
		String action = request.getParameter("action");

		try {
			if (action != null) {
				if (action.equalsIgnoreCase("read")) {
					ProductBean product = null;
					try {
						int id = Integer.parseInt(request.getParameter("id"));
						product = model.findByKey(id);
					} catch (NumberFormatException e) {
						product = null;
					}
					request.getSession().setAttribute("product", product);
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Errore nel recupero del dettaglio prodotto", e);
		}

		response.sendRedirect("DetailsView.jsp");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
