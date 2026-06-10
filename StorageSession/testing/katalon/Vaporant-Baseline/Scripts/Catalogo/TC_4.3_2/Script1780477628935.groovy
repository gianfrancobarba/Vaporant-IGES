import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.3_2 — Dettaglio con id inesistente 999 (RF_GC_13) — oracolo da requisito (incident atteso)
// Oracolo da requisito: id inesistente -> "Prodotto non trovato" (o pagina di errore gestita).
// REALE: DetailsControl non effettua null-check su doRetrieveByKey(999) -> DetailsView.jsp mostra
// un prodotto di default (nome vuoto, prezzo 0.0, "Prodotto terminato!"). Nessun messaggio di "non trovato".
// FAILED = incident -> CR_02 (null-check in DetailsControl + validazione input).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'details?action=read&id=999')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): sistema deve mostrare "Prodotto non trovato" per id inesistente.
// REALE: "Prodotto non trovato" assente (mostrato prodotto di default) -> FAILED = incident.
WebUI.verifyTextPresent('Prodotto non trovato', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
