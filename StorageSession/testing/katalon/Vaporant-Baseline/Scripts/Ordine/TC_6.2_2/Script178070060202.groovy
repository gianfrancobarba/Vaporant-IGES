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

// TC_6.2_2 — Acquisto senza indirizzo di spedizione (RF_GO_20) — validazione client (atteso PASSED)
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
// Seleziona SOLO la fatturazione e il pagamento; lascia la spedizione all'opzione vuota di default.
WebUI.selectOptionByValue(css('#addressDropdown2'), '4', false)
WebUI.click(css('#paypal'))
// Oracolo (UI/DOM): #addressDropdown ha value="" (opzione "Seleziona un indirizzo") -> required -> invalido.
Object valid = WebUI.executeJavaScript("return document.getElementById('addressDropdown').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css("form[action='Ordine'] .checkout-btn"))
WebUI.delay(1)
WebUI.takeScreenshot()
// Conferma: il form non e' stato inviato -> si resta su checkout.jsp (il select e' ancora presente).
WebUI.verifyElementPresent(css('#addressDropdown'), 5)
WebUI.closeBrowser()
