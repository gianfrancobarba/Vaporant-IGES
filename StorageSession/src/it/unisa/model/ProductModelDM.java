package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ProductModelDM implements ProductModel {

	private static final String TABLE_NAME = "prodotto";
	private static final Logger LOGGER = Logger.getLogger(ProductModelDM.class.getName());

	private static DataSource ds;

	// connessione al database
	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/storage");
		} catch (NamingException e) {
			LOGGER.log(Level.SEVERE, "Errore nel lookup del DataSource jdbc/storage", e);
		}
	}

	@Override
	public void doSave(ProductBean product) throws SQLException {

		String insertSQL = "INSERT INTO " + ProductModelDM.TABLE_NAME
				+ " (nome, descrizione, quantita, prezzoAttuale, tipo, colore) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

			preparedStatement.setString(1, product.getName());
			preparedStatement.setString(2, product.getDescription());
			preparedStatement.setInt(3, product.getQuantityStorage());
			preparedStatement.setFloat(4, product.getPrice());
			preparedStatement.setString(5, product.getTipo());
			preparedStatement.setString(6, product.getColore());

			preparedStatement.executeUpdate();
		}
	}

	@Override
	public ProductBean doRetrieveByKey(int id) throws SQLException {

		String selectSQL = "SELECT * FROM " + ProductModelDM.TABLE_NAME + " WHERE ID = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

			preparedStatement.setInt(1, id);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				ProductBean bean = new ProductBean();

				while (rs.next()) {
					bean.setCode(rs.getInt("ID"));
					bean.setName(rs.getString("nome"));
					bean.setDescription(rs.getString("descrizione"));
					bean.setPrice(rs.getFloat("prezzoAttuale"));
					bean.setQuantityStorage(rs.getInt("quantita"));
				}

				return bean;
			}
		}
	}

	@Override
	public boolean doDelete(int id) throws SQLException {

		String deleteSQL = "DELETE FROM " + ProductModelDM.TABLE_NAME + " WHERE ID = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

			preparedStatement.setInt(1, id);

			int result = preparedStatement.executeUpdate();
			return result != 0;
		}
	}

	@Override
	public Collection<ProductBean> doRetrieveAll(String order) throws SQLException {

		String selectSQL = "SELECT * FROM " + ProductModelDM.TABLE_NAME;

		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}

		Collection<ProductBean> products = new LinkedList<ProductBean>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
				ResultSet rs = preparedStatement.executeQuery()) {

			while (rs.next()) {
				ProductBean bean = new ProductBean();

				bean.setCode(rs.getInt("ID"));
				bean.setName(rs.getString("nome"));
				bean.setDescription(rs.getString("descrizione"));
				bean.setPrice(rs.getFloat("prezzoAttuale"));
				bean.setQuantityStorage(rs.getInt("quantita"));

				products.add(bean);
			}
		}

		return products;
	}

	@Override
	public void updateQuantityStorage(ProductBean prod, int quantita) throws SQLException {

		String updateSQL = "UPDATE " + ProductModelDM.TABLE_NAME + " SET quantita = ? WHERE ID = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

			preparedStatement.setInt(1, quantita);
			preparedStatement.setInt(2, prod.getCode());
			preparedStatement.executeUpdate();
		}
	}

}
