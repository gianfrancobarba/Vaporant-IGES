package it.unisa.exception;

/**
 * Eccezione del service layer (logica applicativa). Segnala il fallimento di
 * un'operazione di servizio; puo' incapsulare una {@link DataAccessException}
 * proveniente dal data layer o una violazione di regole applicative.
 *
 * <p>Eccezione non controllata: il controller (Servlet) la gestisce ai bordi
 * (logging + risposta/redirect appropriati) senza propagazione di {@code SQLException}.</p>
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}
