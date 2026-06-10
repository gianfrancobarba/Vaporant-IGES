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

// TC_6.2_5 — Verifica decremento magazzino dopo acquisto (RF_GO_20) — FAILED = incident (INC-08)
// updateQuantityStorage committa correttamente (25->24), MA la lettura successiva avviene su una
// connessione del DriverManagerConnectionPool (setAutoCommit=false, snapshot REPEATABLE READ
// congelato) che non vede la scrittura committata da un'altra connessione -> riletto 25.
// Stessa radice di TC_3.6_1 (indirizzo aggiunto non visibile). -> CR_02 (consolidamento connessioni
// + gestione transazioni). Non corretto da CR_01 (scope D2 rinviato a CR_02).
// Stock kiwi (id=2) iniziale: 25 (seed). Dopo l'acquisto di 1 unita' dovrebbe diventare 24.
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
// Oracolo da requisito (UI/DOM): dopo l'acquisto lo stock deve essere 24 (25-1).
// REALE: letto 25 (snapshot stantio da connessione pooled) -> FAILED = incident (INC-08, CR_02).
WebUI.verifyMatch(stockAfter as String, '24', false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
