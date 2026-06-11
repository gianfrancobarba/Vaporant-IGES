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

// TC_3.2_1 — Modificare email con valore valido (RF_GP_5) — atteso PASSED
// NB: MUTA-CREDENZIALI (cambia l'email di login di t.mansi). Eseguire su DB ripristinato.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.click(css('#editButton'))
WebUI.delay(1)
WebUI.setText(css('#emailInput'), 'nuova.mail@example.com')
WebUI.click(css('#submitButton'))
WebUI.delay(2)
String msg = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo: email aggiornata, messaggio "Email modificata con successo!".
WebUI.verifyMatch(msg, '.*Email modificata con successo.*', true)
WebUI.closeBrowser()
