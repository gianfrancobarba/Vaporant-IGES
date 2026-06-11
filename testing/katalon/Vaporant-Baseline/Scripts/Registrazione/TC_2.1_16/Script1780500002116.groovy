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

// TC_2.1_16 — Codice Fiscale gia' registrato (RF_GR_3) — atteso PASSED (registrazione rifiutata per unicita')
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
// CF = seed esistente BRBGFR02B15A508B; gli altri campi ai valori di riferimento (email nuova).
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('codice_fiscale').value='BRBGFR02B15A508B';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='mario.rossi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.takeScreenshot()
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// SignControl: saveUser fallisce (vincolo UNIQUE codice fiscale) -> result=0 -> redirect SignForm.jsp.
// "Non hai un account" (solo su loginForm.jsp) ASSENTE conferma il rifiuto (siamo rimasti su SignForm).
WebUI.verifyTextNotPresent('Non hai un account', false)
WebUI.closeBrowser()
