package it.unisa.exception;

/**
 * Eccezione del data layer (DAO). Incapsula gli errori di accesso ai dati
 * (es. {@link java.sql.SQLException}) sollevandoli come eccezione non controllata,
 * cosi' da non propagare {@code SQLException} oltre il confine del DAO.
 *
 * <p>Sostituisce la gestione generica con {@code printStackTrace()} presente nel
 * sistema as-delivered.</p>
 */
public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}
}
