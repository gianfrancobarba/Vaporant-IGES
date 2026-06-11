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

// TC_3.3_1 — Modificare numero di telefono con valore valido (RF_GP_6) — atteso PASSED
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
WebUI.setText(css('#phoneInput'), '3399998877')
WebUI.click(css('#submitPhoneButton'))
WebUI.delay(2)
// Esito via alert JS.
String msg = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo: numero aggiornato, messaggio "Numero di cellulare modificato con successo!".
WebUI.verifyMatch(msg, '.*Numero di cellulare modificato con successo.*', true)
WebUI.closeBrowser()
