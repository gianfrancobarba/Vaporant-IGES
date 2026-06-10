package it.unisa.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

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
 * <p>Nota: il salvataggio dell'ordine, delle righe e l'aggiornamento del magazzino sono
 * eseguiti in un unico blocco "tutto o niente" (qualunque errore interrompe il checkout
 * senza restituire l'ordine) in preparazione della gestione esplicita della transazione
 * (commit/rollback) prevista nella fase successiva.</p>
 */
public class OrderService {

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

		AddressBean indirizzoFatturazione = addressService.findById(idIndirizzoFatturazione);
		if (indirizzoFatturazione != null) {
			userService.updateBillingAddress(user, indirizzoFatturazione.toStringScript());
		}

		OrderBean order = new OrderBean(user.getId(), idIndirizzoSpedizione, cart.getPrezzoTotale(), LocalDate.now(),
				metodoPagamento);

		try {
			int idOrdine = orderDao.saveOrder(order);
			order.setId_ordine(idOrdine);

			for (ProductBean prod : cart.getProducts()) {
				contenutoDao.saveContenuto(new ContenutoBean(idOrdine, prod.getCode(), prod.getQuantity(),
						InvoiceService.IVA_PERCENT, new BigDecimal(String.valueOf(prod.getPrice()))));
				productService.updateQuantityStorage(prod, prod.getQuantityStorage() - prod.getQuantity());
			}
		} catch (SQLException e) {
			throw new ServiceException("Errore durante il salvataggio dell'ordine", e);
		}

		return order;
	}
}
