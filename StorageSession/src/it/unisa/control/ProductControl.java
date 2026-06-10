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
import it.unisa.model.ProductModel;
import it.unisa.model.ProductModelDM;

public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ProductControl.class.getName());

	public ProductControl() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductModel model = new ProductModelDM();
        String action = request.getParameter("action");

        try {
            if (action != null) {

                if (action.equalsIgnoreCase("delete"))
                {

                    int id = Integer.parseInt(request.getParameter("id"));
                    model.delete(id);
                }
                else if(action.equalsIgnoreCase("insert"))
                {
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    float price = Float.parseFloat(request.getParameter("price"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    String tipo = request.getParameter("tipo");
                    String colore = request.getParameter("colore");

                    ProductBean bean = new ProductBean();
                    bean.setName(name);
                    bean.setDescription(description);
                    bean.setPrice(price);
                    bean.setQuantityStorage(quantity);
                    bean.setTipo(tipo);
                    bean.setColore(colore);
                    model.save(bean);
                }
            }

        } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nella gestione del prodotto", e);
          }

        String sort = request.getParameter("sort");

        try {

            request.getSession().removeAttribute("products");
            request.getSession().setAttribute("products", model.findAll(sort));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero dei prodotti", e);
        }

        if(request.getSession().getAttribute("tipo").equals("admin"))
        	response.sendRedirect("ProductViewAdmin.jsp");
        else
        	response.sendRedirect("ProductView.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
