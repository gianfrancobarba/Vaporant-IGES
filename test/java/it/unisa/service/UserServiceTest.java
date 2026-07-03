package it.unisa.service;

import it.unisa.exception.ServiceException;
import it.unisa.model.UserBean;
import it.unisa.model.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test di unità per {@link UserService}: dipendenza {@link UserDAO} mockata (Mockito); BCrypt
 * usato realmente (deterministico via checkpw), non mockato, per verificare anche l'hashing.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserDAO userDao;

	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserService(userDao);
	}

	private UserBean userWithHashedPassword(String plainPassword) {
		UserBean u = new UserBean();
		u.setId(4);
		u.setEmail("t.mansi@studenti.unisa.it");
		u.setPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt(4))); // costo basso, test veloci
		return u;
	}

	private UserBean validRegistrationUser() {
		UserBean u = new UserBean();
		u.setNome("Mario");
		u.setCognome("Rossi");
		u.setNumTelefono("3331234567");
		u.setEmail("mario.rossi@example.com");
		u.setPassword("Password1");
		u.setCodF("RSSMRA00E15F839X"); // 16 caratteri esatti (CHAR(16))
		return u;
	}

	// --- authenticate ---

	@Test
	void authenticate_credenzialiValide_ritornaUtente() throws SQLException {
		UserBean seed = userWithHashedPassword("321CBA.");
		when(userDao.findByCred("t.mansi@studenti.unisa.it")).thenReturn(seed);

		UserBean risultato = userService.authenticate("t.mansi@studenti.unisa.it", "321CBA.");

		assertEquals(seed, risultato);
	}

	@Test
	void authenticate_passwordErrata_ritornaNull() throws SQLException {
		UserBean seed = userWithHashedPassword("321CBA.");
		when(userDao.findByCred("t.mansi@studenti.unisa.it")).thenReturn(seed);

		assertNull(userService.authenticate("t.mansi@studenti.unisa.it", "sbagliata"));
	}

	@Test
	void authenticate_emailInesistente_ritornaNull() throws SQLException {
		when(userDao.findByCred(anyString())).thenReturn(null);

		assertNull(userService.authenticate("inesistente@example.com", "qualsiasi"));
	}

	@Test
	void authenticate_sqlException_propagataComeServiceException() throws SQLException {
		SQLException cause = new SQLException("errore DB");
		when(userDao.findByCred(anyString())).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class,
				() -> userService.authenticate("x@example.com", "psw"));
		assertEquals(cause, ex.getCause());
	}

	// --- register ---

	@Test
	void register_happyPath_salvaPasswordHashataNonInChiaro() throws SQLException {
		UserBean user = validRegistrationUser();
		String passwordInChiaro = user.getPassword();
		when(userDao.saveUser(any(UserBean.class))).thenReturn(1);

		boolean esito = userService.register(user);

		assertTrue(esito);
		ArgumentCaptor<UserBean> captor = ArgumentCaptor.forClass(UserBean.class);
		verify(userDao).saveUser(captor.capture());
		String passwordSalvata = captor.getValue().getPassword();
		assertTrue(BCrypt.checkpw(passwordInChiaro, passwordSalvata),
				"la password salvata deve essere l'hash BCrypt di quella in chiaro, non la password originale");
		assertFalse(passwordSalvata.equals(passwordInChiaro));
	}

	@Test
	void register_daoRitornaZeroRighe_ritornaFalse() throws SQLException {
		UserBean user = validRegistrationUser();
		when(userDao.saveUser(any(UserBean.class))).thenReturn(0);

		assertFalse(userService.register(user));
	}

	@Test
	void register_sqlException_propagataComeServiceException() throws SQLException {
		UserBean user = validRegistrationUser();
		SQLException cause = new SQLException("errore DB");
		when(userDao.saveUser(any(UserBean.class))).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> userService.register(user));
		assertEquals(cause, ex.getCause());
	}

	@Test
	void register_emailDuplicata_propagataComeServiceException() throws SQLException {
		// TC_2.1_15: email già registrata — vincolo UNIQUE del DB, il DAO propaga la violazione.
		UserBean user = validRegistrationUser();
		java.sql.SQLIntegrityConstraintViolationException cause =
				new java.sql.SQLIntegrityConstraintViolationException("Duplicate entry per 'email'");
		when(userDao.saveUser(any(UserBean.class))).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> userService.register(user));
		assertEquals(cause, ex.getCause());
	}

	@Test
	void register_codiceFiscaleDuplicato_propagataComeServiceException() throws SQLException {
		// TC_2.1_16: codice fiscale già registrato — vincolo UNIQUE del DB, il DAO propaga la violazione.
		UserBean user = validRegistrationUser();
		java.sql.SQLIntegrityConstraintViolationException cause =
				new java.sql.SQLIntegrityConstraintViolationException("Duplicate entry per 'codF'");
		when(userDao.saveUser(any(UserBean.class))).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class, () -> userService.register(user));
		assertEquals(cause, ex.getCause());
	}

	// --- updateEmail ---

	@Test
	void updateEmail_happyPath_modificaERicaricaUtente() throws SQLException {
		UserBean user = userWithHashedPassword("321CBA.");
		UserBean aggiornato = userWithHashedPassword("321CBA.");
		aggiornato.setEmail("nuova@example.com");
		when(userDao.findById(user.getId())).thenReturn(aggiornato);

		UserBean risultato = userService.updateEmail(user, "nuova@example.com");

		InOrder ordine = inOrder(userDao);
		ordine.verify(userDao).modifyMail(user, "nuova@example.com");
		ordine.verify(userDao).findById(user.getId());
		assertEquals("nuova@example.com", risultato.getEmail());
	}

	@Test
	void updateEmail_sqlExceptionSuModifica_propagataComeServiceException() throws SQLException {
		UserBean user = userWithHashedPassword("321CBA.");
		SQLException cause = new SQLException("errore DB");
		org.mockito.Mockito.doThrow(cause).when(userDao).modifyMail(any(UserBean.class), anyString());

		ServiceException ex = assertThrows(ServiceException.class,
				() -> userService.updateEmail(user, "nuova@example.com"));
		assertEquals(cause, ex.getCause());
	}

	// --- updateTelefono ---

	@Test
	void updateTelefono_happyPath_modificaERicaricaUtente() throws SQLException {
		UserBean user = userWithHashedPassword("321CBA.");
		UserBean aggiornato = userWithHashedPassword("321CBA.");
		aggiornato.setNumTelefono("3339876543");
		when(userDao.findById(user.getId())).thenReturn(aggiornato);

		UserBean risultato = userService.updateTelefono(user, "3339876543");

		verify(userDao).modifyTelefono(user, "3339876543");
		assertEquals("3339876543", risultato.getNumTelefono());
	}

	@Test
	void updateTelefono_sqlException_propagataComeServiceException() throws SQLException {
		UserBean user = userWithHashedPassword("321CBA.");
		SQLException cause = new SQLException("errore DB");
		org.mockito.Mockito.doThrow(cause).when(userDao).modifyTelefono(any(UserBean.class), anyString());

		ServiceException ex = assertThrows(ServiceException.class,
				() -> userService.updateTelefono(user, "333"));
		assertEquals(cause, ex.getCause());
	}

	// --- updatePassword ---

	@Test
	void updatePassword_happyPath_aggiornaHashDelBeanDiSessione() throws SQLException {
		UserBean user = userWithHashedPassword("vecchia123");
		when(userDao.modifyPsw(anyString(), eq(user))).thenReturn(1);

		boolean esito = userService.updatePassword(user, "nuova456", "vecchia123");

		assertTrue(esito);
		// il bean di sessione deve riflettere il nuovo hash, altrimenti un secondo
		// cambio password consecutivo nella stessa sessione fallirebbe.
		assertTrue(BCrypt.checkpw("nuova456", user.getPassword()));
	}

	@Test
	void updatePassword_vecchiaPasswordErrata_ritornaFalseEDaoMaiChiamato() throws SQLException {
		UserBean user = userWithHashedPassword("vecchia123");

		boolean esito = userService.updatePassword(user, "nuova456", "sbagliata");

		assertFalse(esito);
		verify(userDao, never()).modifyPsw(anyString(), any(UserBean.class));
	}

	@Test
	void updatePassword_daoRitornaZeroRighe_ritornaFalseEBeanNonModificato() throws SQLException {
		UserBean user = userWithHashedPassword("vecchia123");
		String hashOriginale = user.getPassword();
		when(userDao.modifyPsw(anyString(), eq(user))).thenReturn(0);

		boolean esito = userService.updatePassword(user, "nuova456", "vecchia123");

		assertFalse(esito);
		assertEquals(hashOriginale, user.getPassword());
	}

	@Test
	void updatePassword_sqlException_propagataComeServiceException() throws SQLException {
		UserBean user = userWithHashedPassword("vecchia123");
		SQLException cause = new SQLException("errore DB");
		when(userDao.modifyPsw(anyString(), eq(user))).thenThrow(cause);

		ServiceException ex = assertThrows(ServiceException.class,
				() -> userService.updatePassword(user, "nuova456", "vecchia123"));
		assertEquals(cause, ex.getCause());
	}

	// --- updateBillingAddress ---

	@Test
	void updateBillingAddress_happyPath() throws SQLException {
		UserBean user = userWithHashedPassword("321CBA.");

		userService.updateBillingAddress(user, 2);

		verify(userDao).updateAddress(2, user);
	}

	@Test
	void updateBillingAddress_sqlException_propagataComeServiceException() throws SQLException {
		UserBean user = userWithHashedPassword("321CBA.");
		SQLException cause = new SQLException("errore DB");
		org.mockito.Mockito.doThrow(cause).when(userDao).updateAddress(anyInt(), any(UserBean.class));

		ServiceException ex = assertThrows(ServiceException.class,
				() -> userService.updateBillingAddress(user, 2));
		assertEquals(cause, ex.getCause());
	}

	// --- isValidRegistration: category partition, un solo campo invalido per caso ---

	@Test
	void isValidRegistration_tuttiICampiValidi_ritornaTrue() {
		assertTrue(userService.isValidRegistration(validRegistrationUser()));
	}

	@Test
	void isValidRegistration_nomeVuoto_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setNome("");
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_cognomeSoliSpazi_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setCognome("   ");
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_telefonoNull_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setNumTelefono(null);
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_emailVuota_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setEmail("");
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_passwordSoliSpazi_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setPassword(" ");
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_codFNull_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setCodF(null);
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_codFLunghezza15_bvaSottoLimite_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setCodF("RSSMRA00E15F839"); // 15 caratteri, un carattere sotto il limite CHAR(16)
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_codFLunghezza17_bvaSopraLimite_ritornaFalse() {
		UserBean u = validRegistrationUser();
		u.setCodF("RSSMRA00E15F839XY"); // 17 caratteri, un carattere sopra il limite CHAR(16)
		assertFalse(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_codFLunghezza16_bvaConfineValido_ritornaTrue() {
		UserBean u = validRegistrationUser();
		u.setCodF("RSSMRA00E15F839X"); // 16 caratteri, esattamente al limite CHAR(16)
		assertTrue(userService.isValidRegistration(u));
	}

	@Test
	void isValidRegistration_codFSoliSpaziLunghezza16_comportamentoRealeIncNoto() {
		// il metodo verifica solo la LUNGHEZZA del CF (==16), non il
		// formato: un CF di soli spazi lungo 16 caratteri supera questo controllo.
		UserBean u = validRegistrationUser();
		u.setCodF("                "); // 16 spazi
		assertTrue(userService.isValidRegistration(u), "comportamento reale noto (INC-06): bug non corretto qui");
	}

	// --- isValidEmail ---

	@Test
	void isValidEmail_formatoValido_ritornaTrue() {
		assertTrue(userService.isValidEmail("mario.rossi@example.com"));
	}

	@Test
	void isValidEmail_senzaChiocciola_ritornaFalse() {
		assertFalse(userService.isValidEmail("mario.rossi.example.com"));
	}

	@Test
	void isValidEmail_senzaPuntoNelDominio_ritornaFalse() {
		assertFalse(userService.isValidEmail("mario.rossi@examplecom"));
	}

	@Test
	void isValidEmail_conSpaziInterni_ritornaFalse() {
		assertFalse(userService.isValidEmail("mario rossi@example.com"));
	}

	@Test
	void isValidEmail_blank_ritornaFalse() {
		assertFalse(userService.isValidEmail("   "));
	}

	@Test
	void isValidEmail_null_ritornaFalse() {
		assertFalse(userService.isValidEmail(null));
	}

	// --- isValidTelefono / isValidNewPassword ---

	@Test
	void isValidTelefono_valoreNonVuoto_ritornaTrue() {
		assertTrue(userService.isValidTelefono("3331234567"));
	}

	@Test
	void isValidTelefono_soliSpazi_ritornaFalse() {
		assertFalse(userService.isValidTelefono("   "));
	}

	@Test
	void isValidTelefono_null_ritornaFalse() {
		assertFalse(userService.isValidTelefono(null));
	}

	@Test
	void isValidNewPassword_valoreNonVuoto_ritornaTrue() {
		assertTrue(userService.isValidNewPassword("Password1"));
	}

	@Test
	void isValidNewPassword_blank_ritornaFalse() {
		assertFalse(userService.isValidNewPassword(""));
	}

	@Test
	void isValidNewPassword_null_ritornaFalse() {
		assertFalse(userService.isValidNewPassword(null));
	}
}
