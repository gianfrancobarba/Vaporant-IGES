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

// TC_6.1_4 — Checkout con carrello vuoto/assente (RF_GO_19) — oracolo da requisito (incident atteso)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Accede a checkout SENZA aver aggiunto prodotti al carrello -> cart e' null in sessione.
WebUI.navigateToUrl(GlobalVariable.base + 'checkout.jsp')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito (UI): atteso messaggio/redirect. REALE: cart.getPrezzoTotale() con cart null
// -> NullPointerException -> Tomcat 500 -> il pulsante "Acquista" non e' presente.
// FAILED = incident (manca il null-check su cart prima di usarlo, hardening CR_02).
WebUI.verifyTextPresent('(?i)Acquista', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
