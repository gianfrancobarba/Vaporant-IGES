package it.unisa.service;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import it.unisa.exception.ServiceException;
import it.unisa.model.UserBean;
import it.unisa.model.UserDAO;
import it.unisa.model.UserDaoImpl;

/**
 * Logica applicativa relativa all'utente: autenticazione, registrazione, modifica del profilo.
 *
 * <p>Nota: la sincronizzazione del bean utente in sessione dopo le modifiche di profilo
 * (caricamento al login, propagazione degli aggiornamenti) e' parte della strategia di
 * gestione della sessione e resta in carico al controller in questa fase.</p>
 */
public class UserService {

	private final UserDAO userDao;

	public UserService() {
		this(new UserDaoImpl());
	}

	public UserService(UserDAO userDao) {
		this.userDao = userDao;
	}

	public UserBean authenticate(String email, String password) {
		try {
			// Il DAO restituisce l'utente per sola email (con l'hash in user.getPassword());
			// la verifica BCrypt avviene qui nel service.
			UserBean user = userDao.findByCred(email);
			if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
				return null;
			}
			return user;
		} catch (SQLException e) {
			throw new ServiceException("Errore durante l'autenticazione", e);
		}
	}

	public boolean register(UserBean user) {
		try {
			// L'hash avviene nel service prima del salvataggio; il DAO riceve e salva l'hash.
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
			return userDao.saveUser(user) > 0;
		} catch (SQLException e) {
			throw new ServiceException("Errore nella registrazione dell'utente", e);
		}
	}

	public UserBean updateEmail(UserBean user, String newEmail) {
		try {
			userDao.modifyMail(user, newEmail);
			return userDao.findById(user.getId());
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'aggiornamento dell'email", e);
		}
	}

	public UserBean updateTelefono(UserBean user, String newTelefono) {
		try {
			userDao.modifyTelefono(user, newTelefono);
			return userDao.findById(user.getId());
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'aggiornamento del numero di telefono", e);
		}
	}

	public boolean updatePassword(UserBean user, String newPsw, String oldPsw) {
		try {
			// Verifica la vecchia password sull'hash corrente del bean di sessione.
			if (!BCrypt.checkpw(oldPsw, user.getPassword())) {
				return false;
			}
			String hashedNewPsw = BCrypt.hashpw(newPsw, BCrypt.gensalt(12));
			int result = userDao.modifyPsw(hashedNewPsw, user);
			if (result != 0) {
				// Aggiorna il bean di sessione con l'hash: senza questo il secondo cambio
				// password consecutivo fallirebbe (checkpw su non-hash → "Invalid salt").
				user.setPassword(hashedNewPsw);
			}
			return result != 0;
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'aggiornamento della password", e);
		}
	}

	/**
	 * Imposta l'indirizzo di fatturazione predefinito dell'utente ({@code Utente.ID_IndirizzoFatturazione},
	 * relazione con {@code Indirizzo}). Da non confondere con {@link AddressService}, che gestisce le
	 * operazioni CRUD sugli indirizzi stessi.
	 */
	public void updateBillingAddress(UserBean user, int idIndirizzoFatturazione) {
		try {
			userDao.updateAddress(idIndirizzoFatturazione, user);
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'aggiornamento dell'indirizzo di fatturazione", e);
		}
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	/**
	 * Valida i dati di registrazione: nessun campo testuale puo' essere vuoto o di soli
	 * spazi e il codice fiscale deve avere lunghezza 16 (Utente.CF CHAR(16)).
	 */
	public boolean isValidRegistration(UserBean user) {
		return !isBlank(user.getNome()) && !isBlank(user.getCognome()) && !isBlank(user.getNumTelefono())
				&& !isBlank(user.getEmail()) && !isBlank(user.getPassword())
				&& user.getCodF() != null && user.getCodF().length() == 16;
	}

	/**
	 * Valida una nuova email di profilo: non vuota/di soli spazi e in formato {x@y.z}.
	 */
	public boolean isValidEmail(String email) {
		return !isBlank(email) && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}

	/**
	 * Valida un nuovo numero di telefono di profilo: non vuoto/di soli spazi.
	 */
	public boolean isValidTelefono(String telefono) {
		return !isBlank(telefono);
	}

	/**
	 * Valida una nuova password di profilo: non vuota/di soli spazi.
	 */
	public boolean isValidNewPassword(String password) {
		return !isBlank(password);
	}
}
