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

// TC_3.1_1 — Visualizzare profilo da utente autenticato (RF_GP_4) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.takeScreenshot()
// Oracolo (UI): il profilo mostra i dati anagrafici del cliente. Uso l'email (testo NON trasformato da CSS);
// il titolo "Benvenuto" e' reso in MAIUSCOLO via text-transform e non sarebbe confrontabile in modo affidabile.
WebUI.verifyTextPresent('t.mansi@studenti.unisa.it', false)
WebUI.closeBrowser()
