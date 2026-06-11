import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_1.1_2 — Login con password errata (RF_GA_1) — atteso PASSED (accesso negato)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), 'Sbagliata99')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo: accesso negato -> l'utente resta su loginForm.jsp ("Non hai un account")
WebUI.verifyTextPresent('Non hai un account', false)
WebUI.closeBrowser()
