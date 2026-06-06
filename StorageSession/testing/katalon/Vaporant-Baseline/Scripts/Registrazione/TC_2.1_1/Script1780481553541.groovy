import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// helper: crea un TestObject da un selettore CSS
TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_2.1_1 — Registrazione con dati validi (RF_GR_3) — INCIDENT atteso INC-02 (FAILED)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
// Compilo tutti i campi (la data via JS perche' e' un input type=date)
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('codice_fiscale').value='RSSMRA00E15F839X';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='mario.rossi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.takeScreenshot()
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito: registrazione riuscita -> redirect a loginForm.jsp (testo unico "Non hai un account").
// REALE: SignControl cattura la SQLException 'indirizzoFatt' (result=0) -> sendRedirect("SignForm.jsp").
// "Non hai un account" e' SOLO su loginForm.jsp -> assente su SignForm -> FAILED = INC-02.
// (Il precedente oracolo "Accedi" dava falso PASSED perche' "Accedi qui" e' anche su SignForm.jsp.)
WebUI.verifyTextPresent('Non hai un account', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
