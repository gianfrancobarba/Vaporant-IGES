import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_1.2_2 — Invocazione di /logout da ospite (RF_GA_2) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'logout')
WebUI.delay(1)
WebUI.takeScreenshot()
// Oracolo: redirect a ProductView (ospite) -> presente il link "Login"
WebUI.verifyElementPresent(css("a[href='loginForm.jsp']"), 5)
WebUI.closeBrowser()
