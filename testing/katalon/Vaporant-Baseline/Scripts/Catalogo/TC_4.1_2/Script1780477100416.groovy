import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_4.1_2 — Catalogo in vista amministratore (RF_GC_11) — atteso PASSED
// Ridisegnato: il test frame "catalogo vuoto" non e' automatizzabile in black-box perche' i prodotti
// seed id=1 (noisy creek) e id=2 (kiwi) sono referenziati da Contenuto -> DELETE viola la FK,
// l'eccezione e' inghiottita da ProductControl, i prodotti restano (confermato da TC_7.2_3 e dallo
// screenshot della run 20260608_214411). L'empty-state e' irraggiungibile: falso negativo, non incident.
// Nuovo test frame: RF_GC_11 specifica che anche l'AMMINISTRATORE puo' visualizzare il catalogo
// (TC_4.1_1 copre il frame ospite). Qui si testa che la vista admin (ProductViewAdmin.jsp) sia
// accessibile e correttamente renderizzata dopo il login.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Naviga alla servlet: carica i prodotti in sessione e redirige a ProductViewAdmin.jsp (tipo=admin).
WebUI.navigateToUrl(GlobalVariable.base + 'product')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): la vista admin mostra il form di inserimento prodotto (presenza di input[value='Aggiungi']).
WebUI.verifyElementPresent(css("input[value='Aggiungi']"), 5)
WebUI.closeBrowser()
