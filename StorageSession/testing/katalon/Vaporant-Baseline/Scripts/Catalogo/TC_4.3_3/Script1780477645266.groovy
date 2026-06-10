import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.3_3 — Dettaglio con id non numerico "abc" (RF_GC_13) — oracolo da requisito (incident atteso)
// Oracolo da requisito: id non numerico -> errore gestito (nessuna pagina di errore 500).
// REALE: DetailsControl.Integer.parseInt("abc") lancia NumberFormatException non gestita
// -> Tomcat genera la pagina di errore 500 "Errore del Server".
// FAILED = incident -> CR_02 (validazione input nel service layer, gestione eccezioni).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'details?action=read&id=abc')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): "Errore del Server" NON deve comparire (errore deve essere gestito).
// REALE: NumberFormatException -> 500 -> "Errore del Server" presente -> FAILED = incident.
WebUI.verifyTextNotPresent('Errore del Server', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
