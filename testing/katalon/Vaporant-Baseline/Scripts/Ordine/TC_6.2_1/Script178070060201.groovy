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

// TC_6.2_1 — Acquisto con tutti i campi selezionati (RF_GO_20) — atteso PASSED
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
// Seleziona indirizzo di spedizione (id=4, seed di t.mansi) e di fatturazione (id=4)
WebUI.selectOptionByValue(css('#addressDropdown'), '4', false)
WebUI.selectOptionByValue(css('#addressDropdown2'), '4', false)
// Seleziona metodo di pagamento PayPal
WebUI.click(css('#paypal'))
WebUI.delay(1)
WebUI.takeScreenshot()
// Invia il form
// Usa il form contenitore per disambiguare: checkout.jsp ha DUE bottoni .checkout-btn
// (uno "Aggiorna" quantita' e uno "Acquista"); senza qualifica verrebbe cliccato il primo nel DOM.
WebUI.click(css("form[action='Ordine'] .checkout-btn"))
WebUI.delay(3)
WebUI.takeScreenshot()
// Oracolo (UI): acquisto registrato -> redirect a ordine.jsp ("Acquisto effettuato!").
// L'esito e' letto dalla sessione -> non dipende dal commit del DAO.
WebUI.verifyTextPresent('(?i)Acquisto effettuato', true)
WebUI.closeBrowser()
