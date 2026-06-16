package it.unisa.control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.exception.ServiceException;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductFilter;
import it.unisa.service.ProductService;
import javax.servlet.http.HttpSession;

public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ProductControl.class.getName());

	public ProductControl() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductService productService = new ProductService();
        String action = request.getParameter("action");

        try {
            if (action != null) {

                // Guard ruolo: inserimento e rimozione prodotto sono operazioni riservate all'amministratore.
                HttpSession session = request.getSession(false);
                if (session == null || !"admin".equalsIgnoreCase((String) session.getAttribute("tipo"))) {
                    response.sendRedirect("ErrorPageAccess.jsp");
                    return;
                }

                if (action.equalsIgnoreCase("delete"))
                {

                    int id = Integer.parseInt(request.getParameter("id"));
                    productService.delete(id);
                }
                else if(action.equalsIgnoreCase("insert"))
                {
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    float price = Float.parseFloat(request.getParameter("price"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    String tipo = request.getParameter("tipo");
                    String colore = request.getParameter("colore");

                    if (productService.isValidName(name)) {
                        ProductBean bean = new ProductBean();
                        bean.setName(name);
                        bean.setDescription(description);
                        bean.setPrice(price);
                        bean.setQuantityStorage(quantity);
                        bean.setTipo(tipo);
                        bean.setColore(colore);
                        productService.save(bean);
                    }
                }
            }

        } catch (ServiceException e) {
                LOGGER.log(Level.SEVERE, "Errore nella gestione del prodotto", e);
          }

        // Routing filtri (CR_04): se e' presente almeno un parametro di filtro o di
        // direzione d'ordinamento si usa findFiltered; altrimenti findAll (RF_GC_12 invariato).
        String sort    = request.getParameter("sort");
        String dir     = request.getParameter("dir");
        String minStr  = request.getParameter("priceMin");
        String maxStr  = request.getParameter("priceMax");
        String inStock = request.getParameter("inStock");

        boolean useFilter = (dir != null && !dir.trim().isEmpty())
                         || (minStr != null && !minStr.trim().isEmpty())
                         || (maxStr != null && !maxStr.trim().isEmpty())
                         || (inStock != null && !inStock.trim().isEmpty());

        try {
            request.getSession().removeAttribute("products");

            if (useFilter) {
                ProductFilter filter = new ProductFilter();
                // Parsing difensivo: valori non numerici ignorati (trattati come assenti)
                if (minStr != null && !minStr.trim().isEmpty()) {
                    try { filter.setPriceMin(Float.parseFloat(minStr)); }
                    catch (NumberFormatException ignored) {}
                }
                if (maxStr != null && !maxStr.trim().isEmpty()) {
                    try { filter.setPriceMax(Float.parseFloat(maxStr)); }
                    catch (NumberFormatException ignored) {}
                }
                filter.setInStockOnly("on".equalsIgnoreCase(inStock) || "true".equalsIgnoreCase(inStock));
                filter.setSortColumn(sort);
                filter.setSortDir(dir);
                request.getSession().setAttribute("products", productService.findFiltered(filter));
            } else {
                request.getSession().setAttribute("products", productService.findAll(sort));
            }

        } catch (ServiceException e) {
            // sort o dir fuori whitelist: l'utente viene avvisato con la pagina d'errore.
            LOGGER.log(Level.SEVERE, "Errore nel recupero dei prodotti", e);
            response.sendRedirect("error-page.jsp");
            return;
        }

        if("admin".equals(request.getSession().getAttribute("tipo")))
        	request.getRequestDispatcher("/ProductViewAdmin.jsp").forward(request, response);
        else
        	request.getRequestDispatcher("/ProductView.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
