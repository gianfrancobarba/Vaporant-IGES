import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.1_2 — Catalogo vuoto (RF_GC_11) — CASO MANUALE (non automatizzato al baseline)
// Oracolo: con catalogo VUOTO il sistema mostra "Non ci sono prodotti disponibili!".
// Il DB as-delivered ha 4 prodotti seed -> per eseguirlo occorre svuotare temporaneamente la tabella `prodotto`.
// Marcato WARNING per non risultare un falso PASSED.
KeywordUtil.markWarning('Caso manuale: richiede catalogo vuoto (DB svuotato). Non eseguibile al baseline as-delivered.')
