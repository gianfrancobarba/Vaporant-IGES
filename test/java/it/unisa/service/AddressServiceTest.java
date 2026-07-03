package it.unisa.service;

import it.unisa.exception.ServiceException;
import it.unisa.model.AddressBean;
import it.unisa.model.AddressDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Test di unità per {@link AddressService}: dipendenza {@link AddressDAO} mockata (Mockito).
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

	@Mock
	private AddressDAO addressDao;

	private AddressService addressService;

	private AddressBean validAddress() {
		AddressBean a = new AddressBean();
		a.setCitta("Salerno");
		a.setVia("Via Roma");
		a.setStato("Italia");
		a.setProvincia("SA");
		a.setCap("84100");
		a.setNumCivico("12A");
		return a;
	}

	@BeforeEach
	void setUp() {
		addressService = new AddressService(addressDao);
	}

	// --- save ---

	@Test
	void save_happyPath_ritornaEsitoDao() throws SQLException {
		AddressBean address = validAddress();
		when(addressDao.saveAddress(address)).thenReturn(1);

		assertEquals(1, addressService.save(address));
	}

	@Test
	void save_sqlException_propagataComeServiceException() throws SQLException {
		AddressBean address = validAddress();
		SQLException cause = new SQLException("errore DB");
		when(addressDao.saveAddress(address)).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> addressService.save(address));
		assertEquals(cause, ex.getCause());
	}

	// --- delete ---

	@Test
	void delete_happyPath_ritornaEsitoDao() throws SQLException {
		AddressBean address = validAddress();
		when(addressDao.deleteAddress(address)).thenReturn(1);

		assertEquals(1, addressService.delete(address));
	}

	@Test
	void delete_sqlException_propagataComeServiceException() throws SQLException {
		AddressBean address = validAddress();
		SQLException cause = new SQLException("errore DB");
		when(addressDao.deleteAddress(address)).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> addressService.delete(address));
		assertEquals(cause, ex.getCause());
	}

	// --- findByUserId ---

	@Test
	void findByUserId_happyPath_listaNonVuota() throws SQLException {
		ArrayList<AddressBean> attesi = new ArrayList<>();
		attesi.add(validAddress());
		when(addressDao.findByUserId(4)).thenReturn(attesi);

		assertEquals(attesi, addressService.findByUserId(4));
	}

	@Test
	void findByUserId_utenteSenzaIndirizzi_listaVuotaNonFiltrata() throws SQLException {
		when(addressDao.findByUserId(anyInt())).thenReturn(new ArrayList<>());

		assertTrue(addressService.findByUserId(999).isEmpty());
	}

	@Test
	void findByUserId_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(addressDao.findByUserId(anyInt())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> addressService.findByUserId(4));
		assertEquals(cause, ex.getCause());
	}

	// --- findById ---

	@Test
	void findById_happyPath_ritornaIndirizzo() throws SQLException {
		AddressBean atteso = validAddress();
		when(addressDao.findById(7)).thenReturn(atteso);

		assertEquals(atteso, addressService.findById(7));
	}

	@Test
	void findById_idInesistente_ritornaNullSenzaNpe() throws SQLException {
		// Convenzione CR_02: doRetrieveByKey -> null (nessun bean di default).
		when(addressDao.findById(anyInt())).thenReturn(null);

		assertNull(addressService.findById(999));
	}

	@Test
	void findById_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(addressDao.findById(anyInt())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> addressService.findById(7));
		assertEquals(cause, ex.getCause());
	}

	// --- isValid: category partition, un solo campo invalido per caso (anti-fault-masking) ---

	@Test
	void isValid_tuttiICampiValidi_ritornaTrue() {
		assertTrue(addressService.isValid(validAddress()));
	}

	@Test
	void isValid_cittaVuota_ritornaFalse() {
		AddressBean a = validAddress();
		a.setCitta("");
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_cittaSoliSpazi_ritornaFalse() {
		AddressBean a = validAddress();
		a.setCitta("   ");
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_cittaNull_ritornaFalse() {
		AddressBean a = validAddress();
		a.setCitta(null);
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_viaVuota_ritornaFalse() {
		AddressBean a = validAddress();
		a.setVia("");
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_viaSoliSpazi_ritornaFalse() {
		AddressBean a = validAddress();
		a.setVia("   ");
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_viaNull_ritornaFalse() {
		AddressBean a = validAddress();
		a.setVia(null);
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_statoSoliSpazi_ritornaFalse() {
		AddressBean a = validAddress();
		a.setStato(" ");
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_statoNull_ritornaFalse() {
		AddressBean a = validAddress();
		a.setStato(null);
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_provinciaNull_ritornaFalse() {
		AddressBean a = validAddress();
		a.setProvincia(null);
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_provinciaLunghezza2_bvaConfineValido_ritornaTrue() {
		AddressBean a = validAddress();
		a.setProvincia("SA"); // CHAR(2), esattamente al limite
		assertTrue(addressService.isValid(a));
	}

	@Test
	void isValid_provinciaLunghezza3_bvaOltreLimite_ritornaFalse() {
		AddressBean a = validAddress();
		a.setProvincia("SAL"); // supera CHAR(2)
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_capNull_ritornaFalse() {
		AddressBean a = validAddress();
		a.setCap(null);
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_capLunghezza5_bvaConfineValido_ritornaTrue() {
		AddressBean a = validAddress();
		a.setCap("84100"); // CHAR(5), esattamente al limite
		assertTrue(addressService.isValid(a));
	}

	@Test
	void isValid_capLunghezza6_bvaOltreLimite_ritornaFalse() {
		AddressBean a = validAddress();
		a.setCap("841000"); // supera CHAR(5)
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_numCivicoNull_ritornaFalse() {
		AddressBean a = validAddress();
		a.setNumCivico(null);
		assertFalse(addressService.isValid(a));
	}

	@Test
	void isValid_numCivicoLunghezza4_bvaConfineValido_ritornaTrue() {
		AddressBean a = validAddress();
		a.setNumCivico("123A"); // CHAR(4), esattamente al limite
		assertTrue(addressService.isValid(a));
	}

	@Test
	void isValid_numCivicoLunghezza5_bvaOltreLimite_ritornaFalse() {
		AddressBean a = validAddress();
		a.setNumCivico("123AB"); // supera CHAR(4)
		assertFalse(addressService.isValid(a));
	}
}
