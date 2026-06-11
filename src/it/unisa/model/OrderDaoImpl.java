package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OrderDaoImpl implements OrderDAO {

  private static final String TABLE = "Ordine";
  private static final Logger LOGGER = Logger.getLogger(OrderDaoImpl.class.getName());

  private static DataSource ds;

  //connessione al database
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
  public int saveOrder(OrderBean ordine) throws SQLException {

    try (Connection connection = ds.getConnection()) {
      return saveOrder(ordine, connection);
    }
  }

  @Override
  public int saveOrder(OrderBean ordine, Connection connection) throws SQLException {

    String insertSQL =
      "INSERT INTO " +
      OrderDaoImpl.TABLE +
      " (ID_Utente, ID_Indirizzo, prezzoTot, dataAcquisto, metodoPagamento)" +
      " VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setInt(1, ordine.getId_utente());
      preparedStatement.setInt(2, ordine.getId_indirizzo());
      preparedStatement.setBigDecimal(3, ordine.getPrezzoTot());
      preparedStatement.setString(4, ordine.getDataAcquisto().toString());
      preparedStatement.setString(5, ordine.getMetodoPagamento());

      preparedStatement.executeUpdate();

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        }
      }
    }

    return -1;
  }

  @Override
  public int deleteOrder(OrderBean ordine) throws SQLException {

    String deleteSQL = "DELETE FROM " + TABLE + " WHERE ID_Ordine= ?";

    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

      preparedStatement.setInt(1, ordine.getId_ordine());

      return preparedStatement.executeUpdate();
    }
  }

  @Override
  public OrderBean findByKey(int id) throws SQLException {

    String selectSQL = "SELECT * FROM " + TABLE + " WHERE ID_Ordine = ?";

    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

      preparedStatement.setInt(1, id);

      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (!rs.isBeforeFirst()) return null;

        OrderBean ordine = new OrderBean();

        while (rs.next()) {
          ordine.setId_ordine(rs.getInt("ID_Ordine"));
          ordine.setId_utente(rs.getInt("ID_Utente"));
          ordine.setId_indirizzo(rs.getInt("ID_Indirizzo"));
          ordine.setPrezzoTot(rs.getBigDecimal("prezzoTot"));
          ordine.setDataAcquisto(
            LocalDate.parse(rs.getDate("dataAcquisto").toString())
          );
          ordine.setMetodoPagamento(rs.getString("metodoPagamento"));
        }

        return ordine;
      }
    }
  }

@Override
public ArrayList<OrderBean> findByIdUtente(int id) throws SQLException{

  String selectSQL = "SELECT * FROM " + TABLE + " WHERE ID_Utente = ?";

  try (Connection connection = ds.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

    preparedStatement.setInt(1, id);

    try (ResultSet rs = preparedStatement.executeQuery()) {
      if (!rs.isBeforeFirst()) return new ArrayList<OrderBean>();

      ArrayList<OrderBean> ordini = new ArrayList<OrderBean>();

      while (rs.next()) {
        OrderBean ordine = new OrderBean();
        ordine.setId_ordine(rs.getInt("ID_Ordine"));
        ordine.setId_utente(rs.getInt("ID_Utente"));
        ordine.setId_indirizzo(rs.getInt("ID_Indirizzo"));
        ordine.setPrezzoTot(rs.getBigDecimal("prezzoTot"));
        ordine.setDataAcquisto(
          LocalDate.parse(rs.getDate("dataAcquisto").toString()));
        ordine.setMetodoPagamento(rs.getString("metodoPagamento"));
        ordini.add(ordine);
      }

      return ordini;
    }
  }
}
}
