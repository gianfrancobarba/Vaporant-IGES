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

// TC_3.5_2 — Visualizzare gli indirizzi (cliente SENZA indirizzi) (RF_GP_8) — atteso PASSED
// Tutti gli utenti seed hanno indirizzi: per soddisfare la precondizione "cliente senza indirizzi"
// il test crea un nuovo cliente attraverso la UI (registrazione), poi ne apre il profilo.
// (La fixture di reset garantisce che email/CF non collidano.)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.executeJavaScript("document.getElementById('nome').value='Test';" +
	"document.getElementById('cognome').value='SenzaIndirizzi';" +
	"document.getElementById('data_nascita').value='2000-01-01';" +
	"document.getElementById('codice_fiscale').value='TSTSNZ00A01F839X';" +
	"document.getElementById('telefono').value='3330000000';" +
	"document.getElementById('email').value='senza.indirizzi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.click(css('#submit'))
WebUI.delay(2)
// Login col nuovo cliente
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'senza.indirizzi@example.com')
WebUI.setText(css('#password'), 'Password1')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.takeScreenshot()
// Oracolo (UI): il cliente non ha indirizzi -> la sezione "INDIRIZZI" non viene mostrata.
WebUI.verifyTextNotPresent('INDIRIZZI', false)
WebUI.closeBrowser()
