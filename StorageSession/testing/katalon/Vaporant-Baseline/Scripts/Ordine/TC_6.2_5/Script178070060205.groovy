import com.kms.katalon.core.model.FailureHandling as FailureHandling
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

// TC_6.2_5 — Verifica decremento magazzino dopo acquisto (RF_GO_20) — atteso PASSED (post-CR_01)
// Il doppio scalo e' stato corretto; updateQuantityStorage chiama commit() -> stock visibile.
// Stock kiwi (id=2) iniziale: 25 (seed). Dopo l'acquisto di 1 unita' deve diventare 24.
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
// Esegue l'acquisto
WebUI.selectOptionByValue(css('#addressDropdown'), '4', false)
WebUI.selectOptionByValue(css('#addressDropdown2'), '4', false)
WebUI.click(css('#paypal'))
// Usa il form contenitore per disambiguare tra "Aggiorna" e "Acquista" (.checkout-btn duplicato).
WebUI.click(css("form[action='Ordine'] .checkout-btn"))
WebUI.delay(3)
// Acquisto completato (su ordine.jsp). Aggiunge kiwi al carrello di nuovo per leggere lo stock.
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'checkout.jsp')
WebUI.delay(2)
// Legge il valore 'max' dell'input quantita: e' pari allo stock corrente in DB (committato).
Object stockAfter = WebUI.executeJavaScript("var el = document.getElementById('quantita'); return el ? el.getAttribute('max') : '-1'", null)
WebUI.takeScreenshot()
// Oracolo (UI/DOM): stock deve essere passato da 25 a 24 (-1 per l'acquisto).
// Post-CR_01 il doppio scalo e' rimosso + updateQuantityStorage committa -> PASSED.
WebUI.verifyMatch(stockAfter as String, '24', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
