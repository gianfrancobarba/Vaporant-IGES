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

// TC_7.2_3 — Rimozione di prodotto referenziato (vincolo FK) (RF_GPR_24) — incident: errore silenzioso
// kiwi (id=2) e' in Contenuto -> doDelete lancia SQLException (FK) -> ProductControl la cattura silenziosamente
// -> nessun feedback all'utente -> catalogo mostrato senza kiwi o con kiwi?
// ProductControl: catch SQLException -> System.out -> continua -> doRetrieveAll -> redirect a ProductViewAdmin.
// kiwi NON viene eliminato (FK violation) ma NESSUN messaggio di errore e' mostrato all'utente = incident.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=2')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): kiwi deve essere ancora presente (rimozione impedita dalla FK). PASS su questo.
// Il mancato feedback all'utente (errore silenzioso) e' l'incident -> documentato nel TIR.
WebUI.verifyTextPresent('(?i)kiwi', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
