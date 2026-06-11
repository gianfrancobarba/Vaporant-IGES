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

// TC_2.1_11 — Registrazione con Codice Fiscale di soli spazi (RF_GR_3) — oracolo da requisito (incident atteso)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.delay(1)
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('codice_fiscale').value='                ';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='mario.rossi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito (UI): CF di soli spazi RIFIUTATO -> resta su SignForm. REALE: accettato (CHAR(16)) -> loginForm -> FAILED = incident (CR_02).
WebUI.verifyTextNotPresent('Non hai un account', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
