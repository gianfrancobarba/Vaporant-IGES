package it.unisa.model;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AddressDAO {

	public int saveAddress(AddressBean address) throws SQLException; // salva indirizzo

	public int deleteAddress(AddressBean address) throws SQLException; // delete indirizzo

	public AddressBean findByCred(String cap, String via, String numCivico) throws SQLException; // ricerca utente per indirizzo (no id)

	ArrayList<AddressBean> findByUserId(int idUtente) throws SQLException; // tutti gli indirizzi di un utente

	public AddressBean findById(int id) throws SQLException; // indirizzo per chiave primaria

}
