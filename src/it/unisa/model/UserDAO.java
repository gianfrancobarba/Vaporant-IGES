package it.unisa.model;

import java.sql.SQLException;

public interface UserDAO {
	
	public int saveUser(UserBean user) throws SQLException; // salva utente
	
	public int deleteUser(UserBean user) throws SQLException; // delete utente
	
	public UserBean findByCred(String email) throws SQLException; // utente per email (verifica BCrypt nel service)	
	
	public UserBean findById(int ID) throws SQLException;

	public void modifyMail(UserBean user, String email) throws SQLException;
	
	public void modifyTelefono(UserBean user, String cell) throws SQLException;

	public void updateAddress(int idIndirizzoFatturazione, UserBean user) throws SQLException;

	public int modifyPsw(String hashedPsw, UserBean user) throws SQLException;
}
