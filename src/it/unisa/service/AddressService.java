package it.unisa.service;

import java.sql.SQLException;
import java.util.ArrayList;

import it.unisa.exception.ServiceException;
import it.unisa.model.AddressBean;
import it.unisa.model.AddressDAO;
import it.unisa.model.AddressDaoImpl;

/**
 * Logica applicativa relativa agli indirizzi di spedizione/fatturazione.
 */
public class AddressService {

	private final AddressDAO addressDao;

	public AddressService() {
		this(new AddressDaoImpl());
	}

	public AddressService(AddressDAO addressDao) {
		this.addressDao = addressDao;
	}

	public int save(AddressBean address) {
		try {
			return addressDao.saveAddress(address);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel salvataggio dell'indirizzo", e);
		}
	}

	public int delete(AddressBean address) {
		try {
			return addressDao.deleteAddress(address);
		} catch (SQLException e) {
			throw new ServiceException("Errore nella rimozione dell'indirizzo", e);
		}
	}

	public ArrayList<AddressBean> findByUserId(int idUtente) {
		try {
			return addressDao.findByUserId(idUtente);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel recupero degli indirizzi dell'utente " + idUtente, e);
		}
	}

	public AddressBean findById(int id) {
		try {
			return addressDao.findById(id);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel recupero dell'indirizzo " + id, e);
		}
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	/**
	 * Valida un indirizzo: citta'/via/stato non vuoti o di soli spazi; provincia/cap/numero
	 * civico entro la lunghezza dei campi dello schema (rispettivamente
	 * {CHAR(2)}, {CHAR(5)}, {CHAR(4)}).
	 */
	public boolean isValid(AddressBean address) {
		return !isBlank(address.getCitta()) && !isBlank(address.getVia()) && !isBlank(address.getStato())
				&& address.getProvincia() != null && address.getProvincia().length() <= 2
				&& address.getCap() != null && address.getCap().length() <= 5
				&& address.getNumCivico() != null && address.getNumCivico().length() <= 4;
	}
}
