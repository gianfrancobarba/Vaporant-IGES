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

// TC_2.1_5 — Registrazione con Codice Fiscale non compilato (RF_GR_3) — validazione client (atteso PASSED)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.delay(1)
// Tutti i campi validi tranne Codice Fiscale (lasciato vuoto)
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='mario.rossi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
Object valid = WebUI.executeJavaScript("return document.getElementById('codice_fiscale').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css('#submit'))
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyTextNotPresent('Non hai un account', false)
WebUI.closeBrowser()
