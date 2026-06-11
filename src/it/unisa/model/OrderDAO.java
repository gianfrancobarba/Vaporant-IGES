package it.unisa.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO {

	public int saveOrder(OrderBean ordine) throws SQLException; // salva ordine

	// salva ordine sulla connessione fornita (transazione governata dal service)
	public int saveOrder(OrderBean ordine, Connection connection) throws SQLException;
	
	public int deleteOrder(OrderBean ordine) throws SQLException; // delete ordine
	
	public OrderBean findByKey(int id) throws SQLException; // ricerca ordine per id
	
	public ArrayList<OrderBean> findByIdUtente(int id) throws SQLException;

}