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

// TC_6.4_1 — Scaricare la fattura PDF (RF_GO_22) — atteso PASSED (post-CR_01: fattura via codice PDFBox)
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
// Su ordine.jsp: clicca il bottone "SCARICA QUI LA TUA FATTURA" (form action='fattura').
WebUI.click(css("form[action='fattura'] button"))
WebUI.delay(3)
WebUI.takeScreenshot()
// Oracolo (UI): FatturaControl genera il PDF (nessuna pagina di errore).
// Post-CR_01: fattura costruita a runtime con PDFBox (niente percorso assoluto).
// Se il PDF e' generato correttamente, la pagina di errore ("Si e' verificato un errore") NON compare.
// Il download del PDF non e' verificabile direttamente via UI; la screenshot e' l'evidenza.
WebUI.verifyTextNotPresent('(?i)Si.*verificato un errore', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
