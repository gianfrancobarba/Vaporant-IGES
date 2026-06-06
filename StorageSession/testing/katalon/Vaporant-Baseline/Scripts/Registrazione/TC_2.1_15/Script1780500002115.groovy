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

// TC_2.1_15 — Email gia' registrata (RF_GR_3) — atteso PASSED (registrazione rifiutata per unicita')
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
// Email = account seed esistente t.mansi@studenti.unisa.it; gli altri campi ai valori di riferimento.
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('codice_fiscale').value='RSSMRA00E15F839X';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='t.mansi@studenti.unisa.it';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.takeScreenshot()
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// SignControl: saveUser fallisce (vincolo UNIQUE email) -> result=0 -> redirect SignForm.jsp.
// "Non hai un account" e' presente SOLO su loginForm.jsp: la sua ASSENZA conferma il rifiuto (siamo rimasti su SignForm).
WebUI.verifyTextNotPresent('Non hai un account', false)
WebUI.closeBrowser()
