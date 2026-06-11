import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_5.4_5 — Quantita' non numerica (RF_GCR_18) — validazione client (atteso PASSED)
// Il campo quantita' ha type="number": un valore non numerico ('abc') viola il vincolo HTML5 -> browser
// blocca l'invio. Su input[type=number], .value='abc' e' sanitizzato a '' (badInput=true) -> checkValidity()=false.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
WebUI.delay(1)
// Imposta valore non numerico via JS. type=number sanifica a '' (badInput=true).
WebUI.executeJavaScript("document.querySelector('[name=\"quantita\"]').value = 'abc';", null)
// Oracolo P1: type=number/required -> checkValidity() deve restituire false -> PASS reale.
Object valid = WebUI.executeJavaScript("return document.querySelector('[name=\"quantita\"]').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css('button.btn'))
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('CARRELLO', false)
WebUI.closeBrowser()