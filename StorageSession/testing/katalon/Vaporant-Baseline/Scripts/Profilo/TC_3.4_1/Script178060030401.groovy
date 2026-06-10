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

// TC_3.4_1 — Modificare password con vecchia corretta e nuova valida (RF_GP_7) — atteso PASSED
// NB: MUTA-CREDENZIALI (cambia la password di login di t.mansi). Eseguire su DB ripristinato.
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
WebUI.setText(css('#oldPasswordInput'), '321CBA.')
WebUI.setText(css('#newPasswordInput'), 'NuovaPass1')
WebUI.click(css('#submitPasswordButton'))
WebUI.delay(2)
String msg = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo: password aggiornata, messaggio "Password modificata con successo!".
WebUI.verifyMatch(msg, '.*Password modificata con successo.*', true)
WebUI.closeBrowser()
