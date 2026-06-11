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

// TC_3.3_3 — Modificare telefono con soli spazi (RF_GP_6) — oracolo da requisito (incident atteso)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.click(css('#editPhoneButton'))
WebUI.delay(1)
WebUI.setText(css('#phoneInput'), '   ')
WebUI.click(css('#submitPhoneButton'))
WebUI.delay(2)
String msg = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo da requisito (UI): telefono di soli spazi RIFIUTATO -> NON deve comparire il messaggio di conferma.
// REALE: nessuna validazione -> "Numero di cellulare modificato con successo" -> FAILED = incident (CR_02).
WebUI.verifyNotMatch(msg, '.*Numero di cellulare modificato con successo.*', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
