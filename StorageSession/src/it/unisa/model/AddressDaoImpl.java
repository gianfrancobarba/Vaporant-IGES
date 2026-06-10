package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AddressDaoImpl implements AddressDAO {
	private static final String TABLE = "indirizzo";
	private static final Logger LOGGER = Logger.getLogger(AddressDaoImpl.class.getName());

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
	public int saveAddress(AddressBean address) throws SQLException {

        String insertSQL = "INSERT INTO " + AddressDaoImpl.TABLE
                           + " (ID_Utente, stato, citta, via, numCivico, cap, provincia)"
                           + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, address.getId_utente());
            preparedStatement.setString(2, address.getStato());
            preparedStatement.setString(3, address.getCitta());
            preparedStatement.setString(4, address.getVia());
            preparedStatement.setString(5, address.getNumCivico());
            preparedStatement.setString(6, address.getCap());
            preparedStatement.setString(7, address.getProvincia());

            return preparedStatement.executeUpdate();
        }
	}


	@Override
	public int deleteAddress(AddressBean address) throws SQLException {

        String deleteSQL = "DELETE FROM " + TABLE + " WHERE ID = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, address.getId());

            return preparedStatement.executeUpdate();
        }
	}

	@Override
	public AddressBean findByCred(String cap, String via, String numCivico) throws SQLException {

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE cap = ? AND via = ? AND numCivico = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, cap);
            preparedStatement.setString(2, via);
            preparedStatement.setString(3, numCivico);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.isBeforeFirst()) return null;

                AddressBean address = new AddressBean();

                while (rs.next()) {
                    address.setId(rs.getInt("ID"));
                    address.setCap(rs.getString("cap"));
                    address.setCitta(rs.getString("citta"));
                    address.setId_utente(rs.getInt("ID_Utente"));
                    address.setNumCivico(rs.getString("numCivico"));
                    address.setProvincia(rs.getString("provincia"));
                    address.setStato(rs.getString("stato"));
                    address.setVia(rs.getString("via"));
                }

                return address;
            }
        }
	}


	@Override
    public ArrayList<AddressBean> findByID(int id) throws SQLException {

        String selectSQL = "SELECT * FROM "+ TABLE + " WHERE ID_Utente = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.isBeforeFirst()) return null;

                ArrayList<AddressBean> ListaIndirizzi = new ArrayList<AddressBean>();

                while (rs.next()) {
                    AddressBean address = new AddressBean();
                    address.setId(rs.getInt("ID"));
                    address.setCap(rs.getString("cap"));
                    address.setCitta(rs.getString("citta"));
                    address.setId_utente(rs.getInt("ID_Utente"));
                    address.setNumCivico(rs.getString("numCivico"));
                    address.setProvincia(rs.getString("provincia"));
                    address.setStato(rs.getString("stato"));
                    address.setVia(rs.getString("via"));
                    ListaIndirizzi.add(address);
                }

                return ListaIndirizzi;
            }
        }
    }

    public AddressBean findAddressByID(int id) throws SQLException {

        String selectSQL = "SELECT * FROM "+ TABLE + " WHERE ID = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.isBeforeFirst()) return null;

                AddressBean address = new AddressBean();

                while (rs.next()) {
                    address = new AddressBean();
                    address.setId(rs.getInt("ID"));
                    address.setCap(rs.getString("cap"));
                    address.setCitta(rs.getString("citta"));
                    address.setId_utente(rs.getInt("ID_Utente"));
                    address.setNumCivico(rs.getString("numCivico"));
                    address.setProvincia(rs.getString("provincia"));
                    address.setStato(rs.getString("stato"));
                    address.setVia(rs.getString("via"));
                }

                return address;
            }
        }
    }

}
