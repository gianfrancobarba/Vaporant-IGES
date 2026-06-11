package it.unisa.control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.UserBean;

/**
 * Filtro di accesso alle aree riservate (profilo, indirizzi, checkout/ordine, fattura):
 * se in sessione non e' presente un {@link UserBean} autenticato, reindirizza a
 * {@code loginForm.jsp} invece di lasciar proseguire la richiesta verso una risorsa che
 * presupporrebbe un utente loggato.
 */
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		UserBean user = (UserBean) session.getAttribute("user");

		if (user == null) {
			if ("/checkout.jsp".equals(req.getServletPath())) {
				session.setAttribute("action", "checkout");
			}
			resp.sendRedirect("loginForm.jsp");
			return;
		}

		chain.doFilter(request, response);
	}
}
