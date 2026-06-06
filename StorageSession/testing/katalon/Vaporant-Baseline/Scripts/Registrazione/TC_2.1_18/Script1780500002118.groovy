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

// TC_2.1_18 — Codice Fiscale di 17 caratteri, > 16 (RF_GR_3) — ROBUSTEZZA/BVA CHAR(16) (WARNING)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.executeJavaScript("document.getElementById('nome').value='Mario';" +
	"document.getElementById('cognome').value='Rossi';" +
	"document.getElementById('data_nascita').value='2000-05-15';" +
	"document.getElementById('codice_fiscale').value='RSSMRA00E15F839XY';" +
	"document.getElementById('telefono').value='3331234567';" +
	"document.getElementById('email').value='mario.rossi@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.takeScreenshot()
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Atteso: rifiuto (il CF deve essere di 16 caratteri). Reale: CHAR(16) -> in sql_mode non strict troncamento a 16, in strict errore.
// Eseguire su DB con email/CF di riferimento NUOVI. loginForm.jsp ("Non hai un account") = accettato (troncato) -> incident TIR; SignForm.jsp = rifiutato.
KeywordUtil.markWarning('CF di 17 caratteri (BVA CHAR(16)): interpretare lo screenshot (loginForm = accettato/troncato/incident, SignForm = rifiutato). Eseguire su DB con email/CF di riferimento nuovi.')
WebUI.closeBrowser()
