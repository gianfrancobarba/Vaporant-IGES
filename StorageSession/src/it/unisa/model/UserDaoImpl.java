package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UserDaoImpl implements UserDAO {

	private static final String TABLE = "utente";
	private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class.getName());

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
	public int saveUser(UserBean user) throws SQLException {

        // ID_IndirizzoFatturazione non e' incluso: alla registrazione l'utente non ha ancora
        // indirizzi (FK su Indirizzo), resta NULL finche' non effettua un acquisto.
        String insertSQL = "INSERT INTO " + UserDaoImpl.TABLE
                           + " (nome, cognome, dataNascita, CF, numTelefono, email, psw)"
                           + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, user.getNome());
            preparedStatement.setString(2, user.getCognome());
            preparedStatement.setString(3, user.getDataNascita().toString());
            preparedStatement.setString(4, user.getCodF());
            preparedStatement.setString(5, user.getNumTelefono());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());

            return preparedStatement.executeUpdate();
        }
	}


	@Override
	public int deleteUser(UserBean user) throws SQLException {

        String deleteSQL = "DELETE FROM " + TABLE + " WHERE id = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, user.getId());

            return preparedStatement.executeUpdate();
        }
	}

	@Override
	public UserBean findByCred(String email, String password) throws SQLException {

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE email = ? AND psw = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.isBeforeFirst()) return null;

                UserBean user = new UserBean();

                while (rs.next()) {
                    user.setEmail(rs.getString("email"));
                    user.setCodF(rs.getString("CF"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    user.setNumTelefono(rs.getString("numTelefono"));
                    user.setId(rs.getInt("ID"));
                    user.setPassword(rs.getString("psw"));
                    user.setTipo(rs.getString("tipo"));
                    user.setDataNascita(LocalDate.parse(rs.getDate("dataNascita").toString()));
                    int idIndirizzoFatturazione = rs.getInt("ID_IndirizzoFatturazione");
                    user.setIdIndirizzoFatturazione(rs.wasNull() ? null : idIndirizzoFatturazione);
                }

                return user;
            }
        }
	}

	@Override
	public UserBean findById(int ID) throws SQLException {

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE ID = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, ID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.isBeforeFirst()) return null;

                UserBean user = new UserBean();

                while (rs.next()) {
                    user.setEmail(rs.getString("email"));
                    user.setCodF(rs.getString("CF"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    user.setNumTelefono(rs.getString("numTelefono"));
                    user.setId(rs.getInt("ID"));
                    user.setPassword(rs.getString("psw"));
                    user.setTipo(rs.getString("tipo"));
                    user.setDataNascita(LocalDate.parse(rs.getDate("dataNascita").toString()));
                }

                return user;
            }
        }
	}

	@Override
	public void modifyMail(UserBean user, String email) throws SQLException {

	    String modify = "UPDATE utente SET email = ? WHERE ID = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(modify)) {

            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, user.getId());

            preparedStatement.executeUpdate();
        }
	}

	@Override
	public void modifyTelefono(UserBean user, String cell) throws SQLException {

	    String modify = "UPDATE utente SET numTelefono = ? WHERE ID = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(modify)) {

            preparedStatement.setString(1, cell);
            preparedStatement.setInt(2, user.getId());

            preparedStatement.executeUpdate();
        }
	}

	@Override
	public int modifyPsw(String newPsw, String oldPsw, UserBean user) throws SQLException {

	    if (oldPsw.compareTo(user.getPassword()) != 0) {
	    	return 0;
	    }

	    String modify = "UPDATE utente SET psw = ? WHERE ID = ? AND psw = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(modify)) {

            preparedStatement.setString(1, newPsw);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.setString(3, oldPsw);

            preparedStatement.executeUpdate();
        }
	    return 1;
	}

	@Override
	public void updateAddress(int idIndirizzoFatturazione, UserBean user) throws SQLException {
	    user.setIdIndirizzoFatturazione(idIndirizzoFatturazione);

	    String updateSQL = "UPDATE " + TABLE + " SET ID_IndirizzoFatturazione = ? WHERE ID = ?";

        try (Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setInt(1, idIndirizzoFatturazione);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        }
	}
}
