package it.unisa.service;

import it.unisa.exception.ServiceException;
import it.unisa.model.ContenutoDAO;
import it.unisa.model.OrderBean;
import it.unisa.model.OrderDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Test di unità per {@link OrderService}.
 *
 * <p><b>Scope deliberatamente limitato:</b> {@code checkout()}
 * risolve il {@link javax.sql.DataSource} in uno <i>static block</i> JNDI
 * ({@code java:comp/env/jdbc/storage}), non disponibile fuori da un container Tomcat e non
 * iniettabile senza modificare il codice di produzione. Per non introdurre modifiche di
 * produzione durante un'attività dichiarata "solo test", {@code checkout()} resta
 * <b>non coperto</b> a livello di unità: la sua unica copertura reale è la suite di sistema
 * Katalon (che gira dentro un vero Tomcat, con il JNDI correttamente risolto) — vedi
 * VAPORANT_TSR_V_1.0 per la nota esplicita su questo gap noto.</p>
 *
 * <p>{@code findByIdUtente} non usa il DataSource statico (delega solo a {@link OrderDAO}) ed è
 * quindi pienamente testabile in isolamento.</p>
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderDAO orderDao;
	@Mock
	private ContenutoDAO contenutoDao;
	@Mock
	private ProductService productService;
	@Mock
	private AddressService addressService;
	@Mock
	private UserService userService;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		orderService = new OrderService(orderDao, contenutoDao, productService, addressService, userService);
	}

	@Test
	void findByIdUtente_happyPath_ritornaStoricoOrdini() throws SQLException {
		ArrayList<OrderBean> attesi = new ArrayList<>();
		when(orderDao.findByIdUtente(4)).thenReturn(attesi);

		assertEquals(attesi, orderService.findByIdUtente(4));
	}

	@Test
	void findByIdUtente_utenteSenzaOrdini_listaVuota() throws SQLException {
		when(orderDao.findByIdUtente(anyInt())).thenReturn(new ArrayList<>());

		assertTrue(orderService.findByIdUtente(999).isEmpty());
	}

	@Test
	void findByIdUtente_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(orderDao.findByIdUtente(anyInt())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> orderService.findByIdUtente(4));
		assertEquals(cause, ex.getCause());
	}
}
