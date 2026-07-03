package it.unisa.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test di unità per {@link InvoiceService}: classe stateless, nessuna dipendenza da mockare.
 */
class InvoiceServiceTest {

	private final InvoiceService invoiceService = new InvoiceService();

	@Test
	void calcolaIva_importoTipico_arrotondaScala4() {
		BigDecimal iva = invoiceService.calcolaIva(new BigDecimal("100.00"));
		assertEquals(new BigDecimal("22.0000"), iva);
	}

	@Test
	void calcolaIva_importoZero_ritornaZero() {
		BigDecimal iva = invoiceService.calcolaIva(BigDecimal.ZERO);
		assertEquals(new BigDecimal("0.0000"), iva);
	}

	@Test
	void calcolaIva_importoConCifraLimite_arrotondaHalfUp() {
		// 10.12345 * 22 / 100 = 2.227159 -> arrotondato a 4 decimali HALF_UP = 2.2272
		BigDecimal iva = invoiceService.calcolaIva(new BigDecimal("10.12345"));
		assertEquals(new BigDecimal("2.2272"), iva);
	}

	@Test
	void calcolaIva_importoNegativo_comportamentoRealeNonValidato() {
		// Il metodo non valida il segno dell'imponibile: comportamento osservato, non un requisito.
		BigDecimal iva = invoiceService.calcolaIva(new BigDecimal("-100.00"));
		assertEquals(new BigDecimal("-22.0000"), iva);
	}

	@Test
	void calcolaTotale_sommaImponibileEIva() {
		BigDecimal totale = invoiceService.calcolaTotale(new BigDecimal("100.00"), new BigDecimal("22.0000"));
		assertEquals(new BigDecimal("122.0000"), totale);
	}

	@Test
	void calcolaTotale_zeroPiuZero_ritornaZero() {
		BigDecimal totale = invoiceService.calcolaTotale(BigDecimal.ZERO, BigDecimal.ZERO);
		assertEquals(new BigDecimal("0"), totale);
	}
}
