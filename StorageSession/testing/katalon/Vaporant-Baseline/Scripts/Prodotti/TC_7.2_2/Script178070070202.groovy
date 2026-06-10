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

// TC_7.2_2 — Rimozione di un id inesistente (RF_GPR_24) — atteso PASSED (catalogo invariato)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// id=999 non esiste -> doDelete restituisce false, nessuna riga eliminata.
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=999')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): catalogo invariato -> tutti i 4 prodotti seed presenti. Verifica con kiwi.
WebUI.verifyTextPresent('(?i)kiwi', true)
WebUI.closeBrowser()
