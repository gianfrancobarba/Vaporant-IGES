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

// TC_6.1_2 — Checkout da ospite: atteso redirect a loginForm (RF_GO_19) — atteso PASSED
// checkout.jsp: se user==null -> sendRedirect("loginForm.jsp") -> browser segue il redirect.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'checkout.jsp')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): ospite rediretto a loginForm.jsp -> il testo "Non hai un account" e' presente.
WebUI.verifyTextPresent('(?i)Non hai un account', true)
WebUI.closeBrowser()
