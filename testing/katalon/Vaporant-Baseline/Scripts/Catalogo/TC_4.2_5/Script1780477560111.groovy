import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_5 — SQL injection sul parametro sort (RF_GC_12) — oracolo da requisito (incident atteso)
// Oracolo da requisito: sort validato tramite whitelist; input non valido -> rifiutato con errore.
// REALE: 'sort' e' concatenato direttamente nell'ORDER BY senza sanificazione -> vulnerabilita' SQLi
// confermata per code review (ProductModelDM.doRetrieveAll); con 'nome;--' MySQL esegue ORDER BY nome
// (il ; termina la clausola, -- e' commento) -> catalogo mostrato, nessun rigetto.
// FAILED = incident -> CR_03 (whitelist anti-SQLi su ORDER BY).
WebUI.openBrowser('')
// Visita ProductView.jsp per impostare tipo="guest" in sessione (evita NPE in ProductControl riga 77).
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=nome;--')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): sistema deve segnalare l'input non valido ("errore").
// REALE: nessun messaggio, catalogo mostrato -> FAILED = incident (SQLi confermata per code review).
WebUI.verifyTextPresent('errore', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
