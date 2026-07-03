package it.unisa.service;

import it.unisa.model.Cart;
import it.unisa.model.ProductBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test di unità per {@link CartService}: dipendenza {@link ProductService} mockata; {@link Cart}
 * e {@link ProductBean} sono POJO reali (nessun mock necessario).
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private ProductService productService;

	private CartService cartService;
	private Cart cart;

	@BeforeEach
	void setUp() {
		cartService = new CartService(productService);
		cart = new Cart();
	}

	private ProductBean product(int id, float price, int quantityStorage) {
		ProductBean p = new ProductBean();
		p.setCode(id);
		p.setName("prodotto-" + id);
		p.setPrice(price);
		p.setQuantityStorage(quantityStorage);
		return p;
	}

	// --- addProduct ---

	@Test
	void addProduct_prodottoTrovato_vieneAggiuntoAlCarrello() {
		ProductBean p = product(1, 29.99f, 12);
		when(productService.findByKey(1)).thenReturn(p);

		cartService.addProduct(cart, 1);

		assertEquals(1, cart.getProducts().size());
		assertTrue(cart.getProducts().contains(p));
	}

	@Test
	void addProduct_prodottoNonTrovato_carrelloInvariatoNessunaEccezione() {
		when(productService.findByKey(999)).thenReturn(null);

		cartService.addProduct(cart, 999);

		assertTrue(cart.getProducts().isEmpty());
	}

	@Test
	void addProduct_prodottoGiaPresenteQuantitaSottoStock_incrementaQuantita() {
		// Cart.addProduct: se il prodotto è già nel carrello e quantity < quantityStorage,
		// la seconda aggiunta incrementa la quantità di 1 anziché duplicare la riga.
		ProductBean p = product(1, 29.99f, 2);
		when(productService.findByKey(1)).thenReturn(p);

		cartService.addProduct(cart, 1);
		cartService.addProduct(cart, 1);

		assertEquals(1, cart.getProducts().size());
		assertEquals(2, cart.getProducts().get(0).getQuantity());
	}

	@Test
	void addProduct_prodottoGiaPresenteQuantitaAlLimiteStock_nonIncrementa() {
		// Cart.addProduct: se quantity == quantityStorage, l'aggiunta ripetuta è un no-op
		// (nessun incremento oltre la disponibilità a magazzino).
		ProductBean p = product(1, 29.99f, 1);
		when(productService.findByKey(1)).thenReturn(p);

		cartService.addProduct(cart, 1);
		cartService.addProduct(cart, 1);

		assertEquals(1, cart.getProducts().size());
		assertEquals(1, cart.getProducts().get(0).getQuantity());
	}

	// --- deleteProduct ---

	@Test
	void deleteProduct_prodottoPresente_vieneRimosso() {
		ProductBean p = product(1, 29.99f, 12);
		when(productService.findByKey(1)).thenReturn(p);
		cartService.addProduct(cart, 1);

		cartService.deleteProduct(cart, 1);

		assertTrue(cart.getProducts().isEmpty());
	}

	@Test
	void deleteProduct_prodottoNonTrovato_noOpNessunaEccezione() {
		when(productService.findByKey(999)).thenReturn(null);

		cartService.deleteProduct(cart, 999);

		assertTrue(cart.getProducts().isEmpty());
	}

	// --- aggiorna ---

	@Test
	void aggiorna_prodottoPresente_quantitaAggiornata() {
		ProductBean p = product(1, 29.99f, 12);
		when(productService.findByKey(1)).thenReturn(p);
		cartService.addProduct(cart, 1);

		cartService.aggiorna(cart, 1, 3);

		assertEquals(3, cart.getProducts().get(0).getQuantity());
	}

	@Test
	void aggiorna_quantitaOltreDisponibilita_validataLatoServer() {
		// Cart.aggiorna clampa la quantita' alla disponibilita' a magazzino (Math.min con lo stock):
		// e' il comportamento REALE e VERIFICATO del collaboratore dopo la manutenzione perfettiva
		// (CR_02, INC-05 "Cart.aggiorna non valida la quantita' lato server" chiuso e verificato in
		// regressione) — non un'assunzione del CartService, che delega interamente a Cart.aggiorna.
		ProductBean p = product(1, 29.99f, 5);
		when(productService.findByKey(1)).thenReturn(p);
		cartService.addProduct(cart, 1);

		cartService.aggiorna(cart, 1, 30);

		assertEquals(5, cart.getProducts().get(0).getQuantity());
	}

	@Test
	void aggiorna_quantitaSottoUno_clampataAUno() {
		// Cart.aggiorna: Math.max(1, ...) impedisce quantita' non positive (0 o negative).
		ProductBean p = product(1, 29.99f, 12);
		when(productService.findByKey(1)).thenReturn(p);
		cartService.addProduct(cart, 1);

		cartService.aggiorna(cart, 1, 0);

		assertEquals(1, cart.getProducts().get(0).getQuantity());
	}

	@Test
	void aggiorna_prodottoNonTrovato_noOpNessunaEccezione() {
		when(productService.findByKey(999)).thenReturn(null);

		cartService.aggiorna(cart, 999, 3);

		assertTrue(cart.getProducts().isEmpty());
	}
}
