package it.unisa.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.unisa.exception.ServiceException;
import it.unisa.model.ProductBean;
import it.unisa.model.ProductModel;
import it.unisa.model.ProductModelDM;

/**
 * Logica applicativa relativa al catalogo prodotti. Incapsula l'accesso a {@link ProductModel},
 * traducendo le {@link SQLException} del data layer in {@link ServiceException}.
 */
public class ProductService {

	private final ProductModel productModel;

	public ProductService() {
		this(new ProductModelDM());
	}

	public ProductService(ProductModel productModel) {
		this.productModel = productModel;
	}

	public Collection<ProductBean> findAll(String sort) {
		try {
			return productModel.findAll(sort);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel recupero del catalogo prodotti", e);
		}
	}

	public ProductBean findByKey(int id) {
		try {
			return productModel.findByKey(id);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel recupero del prodotto " + id, e);
		}
	}

	public void save(ProductBean product) {
		try {
			productModel.save(product);
		} catch (SQLException e) {
			throw new ServiceException("Errore nel salvataggio del prodotto", e);
		}
	}

	public void delete(int id) {
		try {
			productModel.delete(id);
		} catch (SQLException e) {
			throw new ServiceException("Errore nella rimozione del prodotto " + id, e);
		}
	}

	public void updateQuantityStorage(ProductBean prod, int quantita) {
		try {
			productModel.updateQuantityStorage(prod, quantita);
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'aggiornamento della quantita' del prodotto " + prod.getCode(), e);
		}
	}

	/**
	 * Aggiorna la giacenza usando la connessione fornita dal chiamante, senza aprire una
	 * propria connessione: consente al servizio chiamante di includere l'operazione nella
	 * stessa transazione (es. il checkout in {@link OrderService}).
	 */
	public void updateQuantityStorage(ProductBean prod, int quantita, Connection connection) {
		try {
			productModel.updateQuantityStorage(prod, quantita, connection);
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'aggiornamento della quantita' del prodotto " + prod.getCode(), e);
		}
	}

	public List<Map<String, Object>> searchByName(String nome) {
		try {
			return productModel.searchByName(nome);
		} catch (SQLException e) {
			throw new ServiceException("Errore nella ricerca dei prodotti", e);
		}
	}
}
