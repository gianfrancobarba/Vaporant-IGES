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

// TC_6.3_1 — Visualizzare il riepilogo dell'ordine effettuato (RF_GO_21) — atteso PASSED
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
WebUI.selectOptionByValue(css('#addressDropdown'), '4', false)
WebUI.selectOptionByValue(css('#addressDropdown2'), '4', false)
WebUI.click(css('#paypal'))
// Usa il form contenitore per disambiguare tra "Aggiorna" e "Acquista" (.checkout-btn duplicato).
WebUI.click(css("form[action='Ordine'] .checkout-btn"))
WebUI.delay(3)
WebUI.takeScreenshot()
// Oracolo (UI): ordine.jsp mostra "Acquisto effettuato!" e il prodotto acquistato (kiwi).
// I dati vengono dalla sessione -> non dipendono dal commit.
WebUI.verifyTextPresent('(?i)Acquisto effettuato', true)
WebUI.verifyTextPresent('(?i)kiwi', true)
WebUI.closeBrowser()
