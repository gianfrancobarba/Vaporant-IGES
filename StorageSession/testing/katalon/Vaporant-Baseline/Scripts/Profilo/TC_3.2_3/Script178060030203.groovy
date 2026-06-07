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

// TC_3.2_3 — Modificare email con soli spazi (RF_GP_5) — oracolo da requisito (incident atteso)
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
WebUI.setText(css('#emailInput'), '   ')
WebUI.click(css('#submitButton'))
WebUI.delay(2)
if (WebUI.verifyAlertPresent(3, FailureHandling.OPTIONAL)) {
	WebUI.acceptAlert()
}
// Oracolo da requisito (UI): email di soli spazi RIFIUTATA -> l'email originale resta. REALE: salvata -> FAILED = incident.
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('t.mansi@studenti.unisa.it', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
