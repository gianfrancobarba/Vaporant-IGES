package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ContenutoDaoImpl implements ContenutoDAO{
	private static final String TABLE = "Contenuto";
	private static final Logger LOGGER = Logger.getLogger(ContenutoDaoImpl.class.getName());

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
	public int saveContenuto(ContenutoBean contenutoOrdine) throws SQLException {

        try (Connection connection = ds.getConnection()) {
            return saveContenuto(contenutoOrdine, connection);
        }
	}

	@Override
	public int saveContenuto(ContenutoBean contenutoOrdine, Connection connection) throws SQLException {

        String insertSQL = "INSERT INTO " + ContenutoDaoImpl.TABLE
                           + " (ID_Ordine, ID_Prodotto, quantita, prezzoAcquisto, ivaAcquisto)"
                           + " VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, contenutoOrdine.getId_ordine());
            preparedStatement.setInt(2, contenutoOrdine.getId_prodotto());
            preparedStatement.setInt(3, contenutoOrdine.getQuantita());
            preparedStatement.setBigDecimal(4, contenutoOrdine.getPrezzoAcquisto());
            preparedStatement.setInt(5, contenutoOrdine.getIvaAcquisto());

            return preparedStatement.executeUpdate();
        }
	}

	@Override
	public int deleteContenuto(ContenutoBean contenutoOrdine) throws SQLException {

        String deleteSQL = "DELETE FROM " + TABLE + " WHERE ID_Ordine = ? AND ID_Prodotto = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, contenutoOrdine.getId_ordine());
            preparedStatement.setInt(2, contenutoOrdine.getId_prodotto());

            return preparedStatement.executeUpdate();
        }
	}

	@Override
	public ContenutoBean findByKey(int id_ordine, int id_prodotto) throws SQLException {

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE ID_Ordine = ? AND ID_Prodotto = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id_ordine);
            preparedStatement.setInt(2, id_prodotto);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.isBeforeFirst()) return null;

                ContenutoBean contenutoOrdine = new ContenutoBean();

                while (rs.next()) {
                    contenutoOrdine.setId_ordine(rs.getInt("ID_Ordine"));
                    contenutoOrdine.setId_prodotto(rs.getInt("ID_Prodotto"));
                    contenutoOrdine.setIvaAcquisto(rs.getInt("ivaAcquisto"));
                    contenutoOrdine.setPrezzoAcquisto(rs.getBigDecimal("prezzoAcquisto"));
                    contenutoOrdine.setQuantita(rs.getInt("quantita"));
                }

                return contenutoOrdine;
            }
        }
	}
}
