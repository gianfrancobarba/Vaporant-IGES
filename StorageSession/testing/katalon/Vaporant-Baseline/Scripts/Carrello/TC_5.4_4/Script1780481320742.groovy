import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_5.4_4 — Quantita' vuota (RF_GCR_18) — validazione client (atteso PASSED)
// Il campo quantita' ha required: svuotarlo viola il vincolo HTML5 -> browser blocca l'invio.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
WebUI.delay(1)
// Svuota il campo quantita' via JS.
WebUI.executeJavaScript("document.querySelector('[name=\"quantita\"]').value = '';", null)
// Oracolo P1: required -> checkValidity() deve restituire false -> PASS reale.
Object valid = WebUI.executeJavaScript("return document.querySelector('[name=\"quantita\"]').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css('button.btn'))
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('CARRELLO', false)
WebUI.closeBrowser()
