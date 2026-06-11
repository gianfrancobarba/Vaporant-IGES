package it.unisa.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderBean {
	private int id_ordine, id_utente, id_indirizzo;
	private BigDecimal prezzoTot;
	private LocalDate dataAcquisto;
	private String metodoPagamento;

	public OrderBean() {

	}

	public OrderBean(int idUtente, int idIndirizzo, BigDecimal pTot, LocalDate dataAcq, String payment)
	{
		id_utente = idUtente;
		id_indirizzo = idIndirizzo;
		prezzoTot = pTot;
		dataAcquisto = dataAcq;
		metodoPagamento = payment;
	}

	public int getId_ordine() {
		return id_ordine;
	}

	public void setId_ordine(int id_ordine) {
		this.id_ordine = id_ordine;
	}

	public int getId_utente() {
		return id_utente;
	}

	public void setId_utente(int id_utente) {
		this.id_utente = id_utente;
	}

	public int getId_indirizzo() {
		return id_indirizzo;
	}

	public void setId_indirizzo(int id_indirizzo) {
		this.id_indirizzo = id_indirizzo;
	}

	public BigDecimal getPrezzoTot() {
		return prezzoTot;
	}

	public void setPrezzoTot(BigDecimal prezzoTot) {
		this.prezzoTot = prezzoTot;
	}

	public LocalDate getDataAcquisto() {
		return dataAcquisto;
	}

	public void setDataAcquisto(LocalDate dataAcquisto) {
		this.dataAcquisto = dataAcquisto;
	}

	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

public String toString() {

		String s = "";

		s += id_ordine;
		s += " - ";

	    s += id_utente;
		s += " - ";

		s += id_indirizzo;
		s += " - ";

	    s += prezzoTot;
		s += " - ";

		s += dataAcquisto;
		s += " - ";

	    s += metodoPagamento;
		s += "\n";

		return s;
	}

}
