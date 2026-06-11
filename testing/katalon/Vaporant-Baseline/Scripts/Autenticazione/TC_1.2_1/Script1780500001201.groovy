import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_1.2_1 — Logout di un utente autenticato (RF_GA_2) — atteso PASSED
// Login, poi clic su "Logout" nell'Header.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.click(css("a[href='logout']"))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo: sessione invalidata, tornato a ProductView come ospite -> compare il link "Login"
WebUI.verifyElementPresent(css("a[href='loginForm.jsp']"), 5)
WebUI.closeBrowser()
