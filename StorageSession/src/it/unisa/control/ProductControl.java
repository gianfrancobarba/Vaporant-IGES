package it.unisa.control;

import java.io.IOException; 
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductModel;
import it.unisa.model.ProductModelDM;

public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// ProductModelDS usa il DataSource
	// ProductModelDM usa il DriverManager	
	
	static boolean isDataSource = true;
	
	static ProductModel model = new ProductModelDM();
	
	public ProductControl() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action != null) {

                if (action.equalsIgnoreCase("delete")) 
                {

                    int id = Integer.parseInt(request.getParameter("id"));
                    model.doDelete(id);
                }
                else if(action.equalsIgnoreCase("insert"))
                {
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    int price = Integer.parseInt(request.getParameter("price"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    ProductBean bean = new ProductBean();
                    bean.setName(name);
                    bean.setDescription(description);
                    bean.setPrice(price);
                    bean.setQuantityStorage(quantity);
                    model.doSave(bean);
                }
            }

        } catch (SQLException e) {
                System.out.println("Error:" + e.getMessage());
          }

        String sort = request.getParameter("sort");

        try {
        	
            request.getSession().removeAttribute("products");
            request.getSession().setAttribute("products", model.doRetrieveAll(sort));
        
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
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
