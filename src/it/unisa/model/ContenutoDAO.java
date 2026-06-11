package it.unisa.model;
import java.sql.Connection;
import java.sql.SQLException;

public interface ContenutoDAO {

	public int saveContenuto(ContenutoBean contenutoOrdine) throws SQLException; // salva contenuto ordine

	// salva il contenuto sulla connessione fornita (transazione governata dal service)
	public int saveContenuto(ContenutoBean contenutoOrdine, Connection connection) throws SQLException;
	
	public int deleteContenuto(ContenutoBean contenutoOrdine) throws SQLException; // delete contenuto ordine
	
	public ContenutoBean findByKey(int id_ordine, int id_prodotto) throws SQLException; // ricerca contenuto ordine per id ordine e prodotto


}