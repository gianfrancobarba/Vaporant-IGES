package it.unisa.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calcoli relativi alla fatturazione. Centralizza l'aliquota IVA, prima duplicata come
 * costante cablata sia in {@code OrderControl} (riga d'ordine) sia in {@code FatturaControl}
 * (totali fattura).
 */
public class InvoiceService {

	/** Aliquota IVA applicata (percentuale). */
	public static final int IVA_PERCENT = 22;

	public BigDecimal calcolaIva(BigDecimal imponibile) {
		return imponibile.multiply(BigDecimal.valueOf(IVA_PERCENT))
				.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
	}

	public BigDecimal calcolaTotale(BigDecimal imponibile, BigDecimal iva) {
		return imponibile.add(iva);
	}
}
