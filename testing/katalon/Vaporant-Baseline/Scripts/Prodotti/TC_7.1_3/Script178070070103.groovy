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

// TC_7.1_3 — Inserimento con Nome di soli spazi (RF_GPR_23) — oracolo da requisito (incident atteso)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product')
WebUI.delay(2)
WebUI.setText(css("[name='name']"), '   ')
WebUI.setText(css("[name='description']"), 'Pod di prova')
WebUI.setText(css("[name='tipo']"), 'Svapo')
WebUI.setText(css("[name='colore']"), 'Nero')
WebUI.executeJavaScript("document.querySelector(\"[name='price']\").value='30.00'", null)
WebUI.executeJavaScript("document.querySelector(\"[name='quantity']\").value='10'", null)
WebUI.click(css("input[value='Aggiungi']"))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito (UI): un nome di soli spazi va RIFIUTATO -> catalogo invariato (4 prodotti seed).
// REALE: ProductControl non valida -> doSave committa -> prodotto con nome " " inserito -> 5 elementi.
// Verifica: il numero di slide nel carosello deve restare 4
// PRODOTTO DISTINTI (il carosello mostra in loop 4 prodotti)
Object distinti = WebUI.executeJavaScript(
		"return new Set(" +
				"  Array.from(document.querySelectorAll('#image-carousel a[href*=\"details?action=read\"]'))" +
				"       .map(function(a){ return a.getAttribute('href'); })" +
				").size", null)
WebUI.verifyEqual(distinti as Integer, 4, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
