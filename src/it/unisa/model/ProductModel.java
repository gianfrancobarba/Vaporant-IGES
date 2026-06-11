package it.unisa.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProductModel {
	public void save(ProductBean product) throws SQLException;

	public boolean delete(int id) throws SQLException;

	public ProductBean findByKey(int id) throws SQLException;

	public Collection<ProductBean> findAll(String order) throws SQLException;

	void updateQuantityStorage(ProductBean prod, int quantita) throws SQLException;

	// aggiorna la giacenza sulla connessione fornita (transazione governata dal service)
	void updateQuantityStorage(ProductBean prod, int quantita, Connection connection) throws SQLException;

	List<Map<String, Object>> searchByName(String nome) throws SQLException;

}
