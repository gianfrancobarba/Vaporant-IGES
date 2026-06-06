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

// TC_2.1_9 — Nome di soli spazi (RF_GR_3) — ROBUSTEZZA (WARNING, giudizio manuale)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.executeJavaScript("document.getElementById('nome').value='   ';" +
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
// Atteso: rifiuto. Reale: Vaporant non valida i soli spazi -> se l'utente viene creato il dato sporco e' salvato.
// Eseguire su DB con email/CF di riferimento NUOVI, altrimenti il rifiuto sarebbe per unicita' e non per la validazione.
// Interpretare lo screenshot: loginForm.jsp ("Non hai un account") = accettato -> incident TIR (robustezza/CR_02); SignForm.jsp = rifiutato.
KeywordUtil.markWarning('Nome di soli spazi: interpretare lo screenshot (loginForm = accettato/incident, SignForm = rifiutato). Eseguire su DB con email/CF di riferimento nuovi.')
WebUI.closeBrowser()
