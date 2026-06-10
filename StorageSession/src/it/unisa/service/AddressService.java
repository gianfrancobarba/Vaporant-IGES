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
}
