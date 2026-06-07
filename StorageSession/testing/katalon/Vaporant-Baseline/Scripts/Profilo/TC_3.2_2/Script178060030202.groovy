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

// TC_3.2_2 — Modificare email con valore vuoto (RF_GP_5) — oracolo da requisito (incident atteso)
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
WebUI.setText(css('#emailInput'), '')
WebUI.click(css('#submitButton'))
WebUI.delay(2)
String msg = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo da requisito (UI): l'email vuota va RIFIUTATA -> NON deve comparire il messaggio di conferma.
// (L'oracolo guarda l'alert, non il profilo: per il bug di sync sessione il profilo mostra comunque il
//  valore di sessione, mascherando la corruzione.) REALE: ModifyControl non valida -> "Email modificata
// con successo" -> FAILED = incident (hardening CR_02).
WebUI.verifyNotMatch(msg, '.*Email modificata con successo.*', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
