import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_1.1_6 — Login con email malformata (RF_GA_1) — atteso PASSED
// type=email blocca l'invio lato browser: la richiesta non parte e si resta su loginForm.jsp.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi.studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo: invio bloccato -> si resta su loginForm.jsp ("Non hai un account")
WebUI.verifyTextPresent('Non hai un account', false)
WebUI.closeBrowser()
