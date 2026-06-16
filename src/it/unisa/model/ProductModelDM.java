package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ProductModelDM implements ProductModel {

	private static final String TABLE_NAME = "prodotto";
	private static final Logger LOGGER = Logger.getLogger(ProductModelDM.class.getName());

	/** Valori ammessi per il parametro di ordinamento (colonne della tabella Prodotto). */
	private static final Set<String> ORDER_WHITELIST = new HashSet<>(Arrays.asList(
			"ID", "nome", "descrizione", "quantita", "prezzoAttuale", "tipo", "colore"
	));

	/** Valori ammessi per la direzione di ordinamento (CR_04). */
	private static final Set<String> DIRECTION_WHITELIST = new HashSet<>(Arrays.asList(
			"ASC", "DESC"
	));

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
	public void save(ProductBean product) throws SQLException {

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
	public ProductBean findByKey(int id) throws SQLException {

		String selectSQL = "SELECT * FROM " + ProductModelDM.TABLE_NAME + " WHERE ID = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

			preparedStatement.setInt(1, id);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (!rs.isBeforeFirst()) {
					return null;
				}

				ProductBean bean = new ProductBean();

				while (rs.next()) {
					bean.setCode(rs.getInt("ID"));
					bean.setName(rs.getString("nome"));
					bean.setDescription(rs.getString("descrizione"));
					bean.setPrice(rs.getFloat("prezzoAttuale"));
					bean.setQuantityStorage(rs.getInt("quantita"));
					bean.setTipo(rs.getString("tipo"));
					bean.setColore(rs.getString("colore"));
				}

				return bean;
			}
		}
	}

	@Override
	public boolean delete(int id) throws SQLException {

		String deleteSQL = "DELETE FROM " + ProductModelDM.TABLE_NAME + " WHERE ID = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

			preparedStatement.setInt(1, id);

			int result = preparedStatement.executeUpdate();
			return result != 0;
		}
	}

	@Override
	public Collection<ProductBean> findAll(String order) throws SQLException {

		String selectSQL = "SELECT * FROM " + ProductModelDM.TABLE_NAME;

		// Whitelist anti-SQLi: un criterio di ordinamento non vuoto è ammesso solo se è una
		// colonna nota. Un valore non consentito (colonna inesistente o tentativo di injection)
		// viene rifiutato con eccezione (gestita a monte → pagina d'errore), non eseguito in silenzio.
		if (order != null && !order.trim().isEmpty()) {
			if (!ORDER_WHITELIST.contains(order)) {
				throw new SQLException("Parametro di ordinamento non consentito");
			}
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
				bean.setTipo(rs.getString("tipo"));
				bean.setColore(rs.getString("colore"));

				products.add(bean);
			}
		}

		return products;
	}

	@Override
	public Collection<ProductBean> findFiltered(ProductFilter filter) throws SQLException {

		// Costruzione dinamica del WHERE con soli parametri '?' (mai concatenazione diretta).
		// I valori di ordinamento (colonna e direzione) sono validati via whitelist.
		StringBuilder sql = new StringBuilder("SELECT * FROM ").append(TABLE_NAME);
		List<Object> params = new ArrayList<>();
		List<String> conditions = new ArrayList<>();

		if (filter.getPriceMin() != null) {
			conditions.add("prezzoAttuale >= ?");
			params.add(filter.getPriceMin());
		}
		if (filter.getPriceMax() != null) {
			conditions.add("prezzoAttuale <= ?");
			params.add(filter.getPriceMax());
		}
		if (filter.isInStockOnly()) {
			conditions.add("quantita > 0");
		}

		if (!conditions.isEmpty()) {
			sql.append(" WHERE ").append(String.join(" AND ", conditions));
		}

		// Ordinamento: colonna e direzione devono essere entrambe nella rispettiva whitelist.
		String col = filter.getSortColumn();
		String dir = filter.getSortDir();
		if (col != null && !col.trim().isEmpty()) {
			if (!ORDER_WHITELIST.contains(col)) {
				throw new SQLException("Parametro di ordinamento non consentito: " + col);
			}
			String safeDir = "ASC"; // default sicuro
			if (dir != null && DIRECTION_WHITELIST.contains(dir.toUpperCase())) {
				safeDir = dir.toUpperCase();
			} else if (dir != null && !dir.trim().isEmpty()) {
				throw new SQLException("Direzione di ordinamento non consentita: " + dir);
			}
			sql.append(" ORDER BY ").append(col).append(" ").append(safeDir);
		}

		Collection<ProductBean> products = new LinkedList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql.toString())) {

			for (int i = 0; i < params.size(); i++) {
				Object v = params.get(i);
				if (v instanceof Float) {
					ps.setFloat(i + 1, (Float) v);
				} else {
					ps.setObject(i + 1, v);
				}
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ProductBean bean = new ProductBean();
					bean.setCode(rs.getInt("ID"));
					bean.setName(rs.getString("nome"));
					bean.setDescription(rs.getString("descrizione"));
					bean.setPrice(rs.getFloat("prezzoAttuale"));
					bean.setQuantityStorage(rs.getInt("quantita"));
					bean.setTipo(rs.getString("tipo"));
					bean.setColore(rs.getString("colore"));
					products.add(bean);
				}
			}
		}

		return products;
	}

	@Override
	public void updateQuantityStorage(ProductBean prod, int quantita) throws SQLException {

		try (Connection connection = ds.getConnection()) {
			updateQuantityStorage(prod, quantita, connection);
		}
	}

	@Override
	public void updateQuantityStorage(ProductBean prod, int quantita, Connection connection) throws SQLException {

		String updateSQL = "UPDATE " + ProductModelDM.TABLE_NAME + " SET quantita = ? WHERE ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

			preparedStatement.setInt(1, quantita);
			preparedStatement.setInt(2, prod.getCode());
			preparedStatement.executeUpdate();
		}
	}

	@Override
	public List<Map<String, Object>> searchByName(String nome) throws SQLException {

		String selectSQL = "SELECT * FROM " + ProductModelDM.TABLE_NAME + " WHERE nome LIKE ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

			preparedStatement.setString(1, "%" + nome + "%");

			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<Map<String, Object>> results = new ArrayList<>();
				ResultSetMetaData metaData = rs.getMetaData();
				int colonne = metaData.getColumnCount();

				while (rs.next()) {
					Map<String, Object> riga = new HashMap<>();
					for (int i = 1; i <= colonne; i++) {
						riga.put(metaData.getColumnName(i), rs.getObject(i));
					}
					results.add(riga);
				}

				return results;
			}
		}
	}

}
