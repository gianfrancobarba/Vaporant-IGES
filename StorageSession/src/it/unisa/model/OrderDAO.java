package it.unisa.model;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO {
	
	public int saveOrder(OrderBean ordine) throws SQLException; // salva ordine
	
	public int deleteOrder(OrderBean ordine) throws SQLException; // delete ordine
	
	public OrderBean findByKey(int id) throws SQLException; // ricerca ordine per id
	
	public ArrayList<OrderBean> findByIdUtente(int id) throws SQLException;

	int getIdfromDB() throws SQLException; 
	
}