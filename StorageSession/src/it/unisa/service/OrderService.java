package it.unisa.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.unisa.exception.ServiceException;
import it.unisa.model.AddressBean;
import it.unisa.model.Cart;
import it.unisa.model.ContenutoBean;
import it.unisa.model.ContenutoDAO;
import it.unisa.model.ContenutoDaoImpl;
import it.unisa.model.OrderBean;
import it.unisa.model.OrderDAO;
import it.unisa.model.OrderDaoImpl;
import it.unisa.model.ProductBean;
import it.unisa.model.UserBean;

/**
 * Logica applicativa dell'acquisto: salva l'ordine, le righe del carrello e aggiorna le
 * giacenze di magazzino. Aggiorna inoltre l'indirizzo di fatturazione dell'utente.
 *
 * <p>Il salvataggio dell'ordine, delle righe e l'aggiornamento del magazzino avvengono in
 * un'unica transazione: il servizio apre la connessione, disabilita l'autocommit e passa la
 * stessa {@link Connection} ai DAO; in caso di errore esegue il rollback (acquisto atomico),
 * altrimenti il commit.</p>
 */
public class OrderService {

	private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

	private static DataSource ds;

	// connessione al database (stesso DataSource JNDI usato dai DAO)
	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/storage");
		} catch (NamingException e) {
			LOGGER.log(Level.SEVERE, "Errore nel lookup del DataSource jdbc/storage", e);
		}
	}

	private final OrderDAO orderDao;
	private final ContenutoDAO contenutoDao;
	private final ProductService productService;
	private final AddressService addressService;
	private final UserService userService;

	public OrderService() {
		this(new OrderDaoImpl(), new ContenutoDaoImpl(), new ProductService(), new AddressService(), new UserService());
	}

	public OrderService(OrderDAO orderDao, ContenutoDAO contenutoDao, ProductService productService,
			AddressService addressService, UserService userService) {
		this.orderDao = orderDao;
		this.contenutoDao = contenutoDao;
		this.productService = productService;
		this.addressService = addressService;
		this.userService = userService;
	}

	/**
	 * Esegue il checkout: aggiorna l'indirizzo di fatturazione dell'utente, salva l'ordine
	 * e le righe del carrello, decrementa le giacenze. Ritorna l'ordine salvato (con l'ID
	 * generato dal DB).
	 */
	public OrderBean checkout(UserBean user, Cart cart, int idIndirizzoSpedizione, int idIndirizzoFatturazione,
			String metodoPagamento) {

		// guardia di esistenza: idIndirizzoFatturazione deve essere un Indirizzo.ID valido
		// (FK su Utente.ID_IndirizzoFatturazione); se non lo e' si prosegue comunque il checkout
		AddressBean indirizzoFatturazione = addressService.findById(idIndirizzoFatturazione);
		if (indirizzoFatturazione != null) {
			userService.updateBillingAddress(user, idIndirizzoFatturazione);
		}

		OrderBean order = new OrderBean(user.getId(), idIndirizzoSpedizione, cart.getPrezzoTotale(), LocalDate.now(),
				metodoPagamento);

		try (Connection connection = ds.getConnection()) {
			connection.setAutoCommit(false);

			try {
				int idOrdine = orderDao.saveOrder(order, connection);
				order.setId_ordine(idOrdine);

				for (ProductBean prod : cart.getProducts()) {
					contenutoDao.saveContenuto(new ContenutoBean(idOrdine, prod.getCode(), prod.getQuantity(),
							InvoiceService.IVA_PERCENT, new BigDecimal(String.valueOf(prod.getPrice()))), connection);
					productService.updateQuantityStorage(prod, prod.getQuantityStorage() - prod.getQuantity(), connection);
				}

				connection.commit();
			} catch (SQLException | ServiceException e) {
				connection.rollback();
				throw new ServiceException("Errore durante il salvataggio dell'ordine", e);
			}
		} catch (SQLException e) {
			throw new ServiceException("Errore durante il salvataggio dell'ordine", e);
		}

		return order;
	}

	/**
	 * Recupera lo storico ordini dell'utente (usato per la sincronizzazione della sessione).
	 */
	public ArrayList<OrderBean> findByIdUtente(int idUtente) {
		try {
			return orderDao.findByIdUtente(idUtente);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel recupero degli ordini dell'utente " + idUtente, e);
		}
	}
}
