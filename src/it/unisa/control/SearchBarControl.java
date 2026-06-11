package it.unisa.control;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.unisa.exception.ServiceException;
import it.unisa.service.ProductService;

public class SearchBarControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchBarControl.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome");

        ProductService productService = new ProductService();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Map<String, Object>> results = productService.searchByName(nome);
            resp.getWriter().write(new Gson().toJson(results));
        } catch (ServiceException e) {
            LOGGER.log(Level.SEVERE, "Errore nella ricerca dei prodotti", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doPost(req,resp);
    }
}
