package it.unisa.service;

import it.unisa.exception.ServiceException;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductFilter;
import it.unisa.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test di unità per {@link ProductService}: dipendenza {@link ProductModel} mockata.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductModel productModel;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productModel);
	}

	private ProductBean product(int id) {
		ProductBean p = new ProductBean();
		p.setCode(id);
		p.setName("prodotto-" + id);
		return p;
	}

	// --- findAll ---

	@Test
	void findAll_happyPath_delegaAlModel() throws SQLException {
		Collection<ProductBean> attesi = List.of(product(1), product(2));
		when(productModel.findAll("nome")).thenReturn(attesi);

		assertEquals(attesi, productService.findAll("nome"));
	}

	@Test
	void findAll_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(productModel.findAll(anyString())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.findAll("nome"));
		assertEquals(cause, ex.getCause());
	}

	// --- findFiltered: category partition su priceMin/priceMax ---

	@Test
	void findFiltered_priceMinNegativo_normalizzatoANull() throws SQLException {
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMin(-10f);
		when(productModel.findFiltered(any(ProductFilter.class))).thenReturn(Collections.emptyList());

		productService.findFiltered(filtro);

		ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
		verify(productModel).findFiltered(captor.capture());
		assertNull(captor.getValue().getPriceMin());
	}

	@Test
	void findFiltered_priceMaxNegativo_normalizzatoANull() throws SQLException {
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMax(-5f);
		when(productModel.findFiltered(any(ProductFilter.class))).thenReturn(Collections.emptyList());

		productService.findFiltered(filtro);

		ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
		verify(productModel).findFiltered(captor.capture());
		assertNull(captor.getValue().getPriceMax());
	}

	@Test
	void findFiltered_minMaggioreDiMax_nessunaChiamataAlModelListaVuota() throws SQLException {
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMin(100f);
		filtro.setPriceMax(10f);

		Collection<ProductBean> risultato = productService.findFiltered(filtro);

		assertTrue(risultato.isEmpty());
		verify(productModel, never()).findFiltered(any(ProductFilter.class));
	}

	@Test
	void findFiltered_minUgualeAMax_bvaRangeDiUnPunto_delegaAlModel() throws SQLException {
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMin(50f);
		filtro.setPriceMax(50f);
		when(productModel.findFiltered(any(ProductFilter.class))).thenReturn(Collections.emptyList());

		productService.findFiltered(filtro);

		verify(productModel).findFiltered(any(ProductFilter.class));
	}

	@Test
	void findFiltered_rangeValido_filtroDelegatoInvariato() throws SQLException {
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMin(10f);
		filtro.setPriceMax(70f);
		filtro.setInStockOnly(true);
		filtro.setSortColumn("prezzoAttuale");
		filtro.setSortDir("ASC");
		when(productModel.findFiltered(any(ProductFilter.class))).thenReturn(Collections.emptyList());

		productService.findFiltered(filtro);

		ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
		verify(productModel).findFiltered(captor.capture());
		ProductFilter delegato = captor.getValue();
		assertEquals(10f, delegato.getPriceMin());
		assertEquals(70f, delegato.getPriceMax());
		assertTrue(delegato.isInStockOnly());
		assertEquals("prezzoAttuale", delegato.getSortColumn());
		assertEquals("ASC", delegato.getSortDir());
	}

	@Test
	void findFiltered_soloPriceMinSenzaMax_delegaAlModel() throws SQLException {
		// TF_Filtro_02: solo priceMin presente, priceMax assente (nessun bound superiore).
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMin(50f);
		when(productModel.findFiltered(any(ProductFilter.class))).thenReturn(Collections.emptyList());

		productService.findFiltered(filtro);

		ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
		verify(productModel).findFiltered(captor.capture());
		assertEquals(50f, captor.getValue().getPriceMin());
		assertNull(captor.getValue().getPriceMax());
	}

	@Test
	void findFiltered_soloPriceMaxSenzaMin_delegaAlModel() throws SQLException {
		// TF_Filtro_03: solo priceMax presente, priceMin assente (nessun bound inferiore).
		ProductFilter filtro = new ProductFilter();
		filtro.setPriceMax(100f);
		when(productModel.findFiltered(any(ProductFilter.class))).thenReturn(Collections.emptyList());

		productService.findFiltered(filtro);

		ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
		verify(productModel).findFiltered(captor.capture());
		assertNull(captor.getValue().getPriceMin());
		assertEquals(100f, captor.getValue().getPriceMax());
	}

	@Test
	void findFiltered_sqlException_propagataComeServiceException() throws SQLException {
		ProductFilter filtro = new ProductFilter();
		SQLException cause = new SQLException("errore DB");
		when(productModel.findFiltered(any(ProductFilter.class))).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.findFiltered(filtro));
		assertEquals(cause, ex.getCause());
	}

	// --- findByKey ---

	@Test
	void findByKey_happyPath_ritornaProdotto() throws SQLException {
		ProductBean atteso = product(2);
		when(productModel.findByKey(2)).thenReturn(atteso);

		assertEquals(atteso, productService.findByKey(2));
	}

	@Test
	void findByKey_idInesistente_ritornaNullSenzaNpe() throws SQLException {
		when(productModel.findByKey(anyInt())).thenReturn(null);

		assertNull(productService.findByKey(999));
	}

	@Test
	void findByKey_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(productModel.findByKey(anyInt())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.findByKey(2));
		assertEquals(cause, ex.getCause());
	}

	// --- save / delete ---

	@Test
	void save_happyPath_delegaAlModel() throws SQLException {
		ProductBean p = product(1);

		productService.save(p);

		verify(productModel).save(p);
	}

	@Test
	void save_sqlException_propagataComeServiceException() throws SQLException {
		ProductBean p = product(1);
		SQLException cause = new SQLException("errore DB");
		org.mockito.Mockito.doThrow(cause).when(productModel).save(any(ProductBean.class));

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.save(p));
		assertEquals(cause, ex.getCause());
	}

	@Test
	void delete_happyPath_delegaAlModel() throws SQLException {
		productService.delete(1);

		verify(productModel).delete(1);
	}

	@Test
	void delete_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(productModel.delete(anyInt())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.delete(1));
		assertEquals(cause, ex.getCause());
	}

	// --- updateQuantityStorage (overload senza Connection) ---

	@Test
	void updateQuantityStorage_senzaConnection_happyPath() throws SQLException {
		ProductBean p = product(1);

		productService.updateQuantityStorage(p, 5);

		verify(productModel).updateQuantityStorage(p, 5);
	}

	@Test
	void updateQuantityStorage_senzaConnection_sqlException_propagataComeServiceException() throws SQLException {
		ProductBean p = product(1);
		SQLException cause = new SQLException("errore DB");
		org.mockito.Mockito.doThrow(cause).when(productModel).updateQuantityStorage(any(ProductBean.class), anyInt());

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.updateQuantityStorage(p, 5));
		assertEquals(cause, ex.getCause());
	}

	// --- updateQuantityStorage (overload con Connection, usato dalla transazione di checkout) ---

	@Test
	void updateQuantityStorage_conConnection_stessaConnessionePropagataAlModel() throws SQLException {
		ProductBean p = product(1);
		Connection connessione = mock(Connection.class);

		productService.updateQuantityStorage(p, 5, connessione);

		verify(productModel).updateQuantityStorage(eq(p), eq(5), org.mockito.ArgumentMatchers.same(connessione));
	}

	@Test
	void updateQuantityStorage_conConnection_sqlException_propagataComeServiceException() throws SQLException {
		ProductBean p = product(1);
		Connection connessione = mock(Connection.class);
		SQLException cause = new SQLException("errore DB");
		org.mockito.Mockito.doThrow(cause).when(productModel)
				.updateQuantityStorage(any(ProductBean.class), anyInt(), any(Connection.class));

		ServiceException ex = assertThrows(ServiceException.class,
				() -> productService.updateQuantityStorage(p, 5, connessione));
		assertEquals(cause, ex.getCause());
	}

	// --- searchByName ---

	@Test
	void searchByName_happyPath_ritornaListaDiMap() throws SQLException {
		List<Map<String, Object>> attesi = new ArrayList<>();
		attesi.add(new HashMap<>());
		when(productModel.searchByName("kiwi")).thenReturn(attesi);

		assertEquals(attesi, productService.searchByName("kiwi"));
	}

	@Test
	void searchByName_nomeSenzaRisultati_listaVuota() throws SQLException {
		when(productModel.searchByName(anyString())).thenReturn(Collections.emptyList());

		assertTrue(productService.searchByName("xyz-inesistente").isEmpty());
	}

	@Test
	void searchByName_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(productModel.searchByName(anyString())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> productService.searchByName("kiwi"));
		assertEquals(cause, ex.getCause());
	}

	// --- isValidName ---

	@Test
	void isValidName_valoreNonVuoto_ritornaTrue() {
		assertTrue(productService.isValidName("noisy creek 2"));
	}

	@Test
	void isValidName_soliSpazi_ritornaFalse() {
		assertFalse(productService.isValidName("   "));
	}

	@Test
	void isValidName_blank_ritornaFalse() {
		assertFalse(productService.isValidName(""));
	}

	@Test
	void isValidName_null_ritornaFalse() {
		assertFalse(productService.isValidName(null));
	}
}
