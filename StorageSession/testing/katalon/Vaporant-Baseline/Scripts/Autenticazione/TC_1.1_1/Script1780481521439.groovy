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

// TC_1.1_1 — Login con credenziali valide (RF_GA_1) — INCIDENT atteso INC-01 (FAILED)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito: login riuscito -> compare il link "Logout" (utente autenticato in Header).
// REALE atteso: SQLException "Column 'indirizzoFatt' not found" in findByCred -> login fallisce.
// L'asserzione fallisce -> FAILED = INC-01 (CONTINUE_ON_FAILURE per chiudere comunque il browser).
WebUI.verifyElementPresent(css("a[href='logout']"), 5, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
