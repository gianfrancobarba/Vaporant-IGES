import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_3.1_2 — Accesso al profilo da ospite (RF_GP_4) — oracolo da requisito (incident atteso)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.takeScreenshot()
// Oracolo da requisito (UI): un ospite deve essere reindirizzato a loginForm.jsp ("Non hai un account").
// REALE: Utente.jsp esegue user.getEmail() con user null -> NullPointerException -> errore 500 -> il testo
// non e' presente -> FAILED = incident (manca il controllo accesso/redirect; hardening CR_02).
WebUI.verifyTextPresent('Non hai un account', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
