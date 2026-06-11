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

// TC_3.4_3 — Modificare password con vecchia vuota (RF_GP_7) — atteso PASSED (modifica negata, nessuna mutazione)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.click(css('#editPasswordButton'))
WebUI.delay(1)
WebUI.setText(css('#oldPasswordInput'), '')
WebUI.setText(css('#newPasswordInput'), 'NuovaPass1')
WebUI.click(css('#submitPasswordButton'))
WebUI.delay(2)
String msg = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo: la vecchia password vuota non corrisponde -> "Vecchia password errata. Riprova.".
WebUI.verifyMatch(msg, '.*Vecchia password errata.*', true)
WebUI.closeBrowser()
