package it.unisa.model;

import java.sql.SQLException;
import java.util.Collection;

public interface ProductModel {
	public void save(ProductBean product) throws SQLException;

	public boolean delete(int id) throws SQLException;

	public ProductBean findByKey(int id) throws SQLException;

	public Collection<ProductBean> findAll(String order) throws SQLException;

	void updateQuantityStorage(ProductBean prod, int quantita) throws SQLException;

}
