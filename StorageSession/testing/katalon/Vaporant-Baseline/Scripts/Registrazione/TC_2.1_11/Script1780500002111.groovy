import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// helper: crea un TestObject da un selettore CSS
TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_2.1_11 — Codice Fiscale di soli spazi (RF_GR_3) — ROBUSTEZZA (WARNING, giudizio manuale)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('codice_fiscale').value='                ';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='mario.rossi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.takeScreenshot()
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Atteso: rifiuto. Reale: nessuna validazione dei soli spazi (CHAR(16) accetta gli spazi). Eseguire su DB con email/CF di riferimento NUOVI.
// Interpretare lo screenshot: loginForm.jsp ("Non hai un account") = accettato -> incident TIR; SignForm.jsp = rifiutato.
KeywordUtil.markWarning('Codice Fiscale di soli spazi: interpretare lo screenshot (loginForm = accettato/incident, SignForm = rifiutato). Eseguire su DB con email/CF di riferimento nuovi.')
WebUI.closeBrowser()
