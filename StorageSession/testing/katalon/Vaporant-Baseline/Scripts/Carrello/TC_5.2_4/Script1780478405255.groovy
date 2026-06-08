import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_5.2_4 — Aggiungere al carrello un id inesistente 999 (RF_GCR_16) — oracolo da requisito (incident atteso)
// Oracolo da requisito: prodotto non trovato -> nessuna aggiunta al carrello.
// REALE: ProductModelDM.doRetrieveByKey(999) ritorna un bean di default (code=0) ->
// Cart.addProduct aggiunge il prodotto "vuoto" -> CartView.jsp mostra un elemento .box fantasma.
// FAILED = incident -> CR_02 (doRetrieveByKey -> null + null-check in CartControl).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=999')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): nessun elemento .box nel carrello (nessun prodotto aggiunto).
// REALE: elemento .box presente (bean di default aggiunto) -> FAILED = incident.
WebUI.verifyElementNotPresent(css('.box'), 2, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
