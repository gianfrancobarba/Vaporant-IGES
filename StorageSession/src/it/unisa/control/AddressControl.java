package it.unisa.control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.exception.ServiceException;
import it.unisa.model.AddressBean;
import it.unisa.model.UserBean;
import it.unisa.service.AddressService;


public class AddressControl extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AddressControl.class.getName());

    public AddressControl() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		AddressBean address = new AddressBean();

		UserBean user = (UserBean) request.getSession().getAttribute("user");

		if(user != null)
		{
			String citta = request.getParameter("citta");
			String prov = request.getParameter("provincia");
			String via = request.getParameter("via");
			address.setCap(request.getParameter("cap"));
			address.setCitta(citta);
			address.setId_utente(user.getId());
			address.setNumCivico(request.getParameter("numero_civico"));
			address.setProvincia(prov);
			address.setStato(request.getParameter("stato"));
			address.setVia(via);

			AddressService addressService = new AddressService();
			try {
				addressService.save(address);
				response.sendRedirect("Utente.jsp");
			} catch (ServiceException e) {
				LOGGER.log(Level.SEVERE, "Errore nel salvataggio dell'indirizzo", e);
			}
		}

    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
