import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_5.4_2 — Quantita' < 1 (RF_GCR_18) — validazione client (atteso PASSED)
// Il campo quantita' ha min="1": impostare 0 viola il vincolo HTML5 -> browser blocca l'invio.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
// Il redirect porta su CartView.jsp; la navigazione e' gia' avvenuta.
WebUI.delay(1)
// Imposta quantita=0 via JS (il campo non ha 'id', usa name="quantita").
WebUI.executeJavaScript("document.querySelector('[name=\"quantita\"]').value = '0';", null)
// Oracolo P1: min=1 -> checkValidity() deve restituire false -> PASS reale.
Object valid = WebUI.executeJavaScript("return document.querySelector('[name=\"quantita\"]').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css('button.btn'))
WebUI.delay(1)
WebUI.takeScreenshot()
// Conferma: invio bloccato -> si resta su CartView (titolo "CARRELLO" visibile).
WebUI.verifyTextPresent('CARRELLO', false)
WebUI.closeBrowser()
