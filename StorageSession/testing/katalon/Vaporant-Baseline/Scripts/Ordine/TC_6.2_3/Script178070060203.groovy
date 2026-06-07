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

// TC_6.2_3 — Acquisto senza indirizzo di fatturazione (RF_GO_20) — validazione client (atteso PASSED)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'checkout.jsp')
WebUI.delay(2)
// Seleziona SOLO la spedizione e il pagamento; lascia la fatturazione all'opzione vuota di default.
WebUI.selectOptionByValue(css('#addressDropdown'), '4', false)
WebUI.click(css('#paypal'))
// Oracolo (UI/DOM): #addressDropdown2 ha value="" -> required -> invalido.
Object valid = WebUI.executeJavaScript("return document.getElementById('addressDropdown2').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css("form[action='Ordine'] .checkout-btn"))
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyElementPresent(css('#addressDropdown2'), 5)
WebUI.closeBrowser()
