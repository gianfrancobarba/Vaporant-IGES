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

// TC_7.1_9 — Accesso a ProductViewAdmin da utente non-admin (RF_GPR_23) — atteso PASSED (post-CR_01)
// CR_01 ha corretto il controllo ruolo (!"admin".equals(tipo) con return) -> redirect a ErrorPageAccess.jsp.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Naviga direttamente a ProductViewAdmin.jsp come utente non-admin.
WebUI.navigateToUrl(GlobalVariable.base + 'ProductViewAdmin.jsp')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): accesso negato -> ErrorPageAccess.jsp ("Accesso negato").
WebUI.verifyTextPresent('(?i)Accesso negato', true)
WebUI.closeBrowser()
