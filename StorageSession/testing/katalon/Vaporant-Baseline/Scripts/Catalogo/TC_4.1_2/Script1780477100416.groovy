import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_4.1_2 — Catalogo vuoto (RF_GC_11) — atteso PASSED
// ProductControl.delete non richiede autenticazione (bug noto -> TC_7.2_4 / CR_03).
// SeedReset ripristina i 4 prodotti seed prima del prossimo test.
WebUI.openBrowser('')
// Visita ProductView.jsp per impostare tipo="guest" in sessione (evita NPE in ProductControl riga 77).
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
// Elimina i 4 prodotti seed (id=1,2,3,4).
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=1')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=2')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=3')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=4')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): catalogo vuoto -> "Non ci sono prodotti disponibili!" (ProductView.jsp ramo else).
WebUI.verifyTextPresent('Non ci sono prodotti disponibili!', false)
WebUI.closeBrowser()
