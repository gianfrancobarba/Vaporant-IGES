package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.AddressBean;
import it.unisa.model.AddressDaoImpl;
import it.unisa.model.UserBean;
import it.unisa.model.UserDaoImpl;


public class AddressControl extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	static AddressDaoImpl addressDao = new AddressDaoImpl();
	static UserDaoImpl userDao = new UserDaoImpl();
       
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
			try {
				addressDao.saveAddress(address);
				response.sendRedirect("Utente.jsp");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
