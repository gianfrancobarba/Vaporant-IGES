import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_4 — Ordinamento su colonna inesistente (RF_GC_12) — oracolo da requisito (incident atteso)
// Oracolo da requisito: sort non valido -> errore gestito (whitelist o rifiuto input).
// REALE: ProductControl inghiotte la SQLException (catch + System.out), products rimossi dalla sessione
// poi ricaricati senza sort -> catalogo mostrato normalmente, nessun feedback all'utente.
// FAILED = incident -> CR_02 (gestione eccezioni + validazione input).
WebUI.openBrowser('')
// Visita ProductView.jsp per impostare tipo="guest" in sessione (evita NPE in ProductControl riga 77).
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=colonna_inesistente')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): sistema deve segnalare l'errore di ordinamento ("errore").
// REALE: nessun messaggio di errore -> catalogo mostrato normalmente -> FAILED = incident.
WebUI.verifyTextPresent('errore', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
