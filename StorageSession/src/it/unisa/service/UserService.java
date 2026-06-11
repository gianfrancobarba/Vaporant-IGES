package it.unisa.service;

import java.sql.SQLException;

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
			return userDao.findByCred(email, password);
		} catch (SQLException e) {
			throw new ServiceException("Errore durante l'autenticazione", e);
		}
	}

	public boolean register(UserBean user) {
		try {
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
			return userDao.modifyPsw(newPsw, oldPsw, user) != 0;
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
	 * Valida una nuova password di profilo: non vuota/di soli spazi (le regole di formato/
	 * complessita' sono in carico alla messa in sicurezza).
	 */
	public boolean isValidNewPassword(String password) {
		return !isBlank(password);
	}
}
