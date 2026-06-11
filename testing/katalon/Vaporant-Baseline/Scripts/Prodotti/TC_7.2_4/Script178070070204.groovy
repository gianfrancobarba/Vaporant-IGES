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

// TC_7.2_4 — Rimozione da utente non-admin (RF_GPR_24) — oracolo da requisito (incident di sicurezza atteso)
// ProductControl NON ha controllo ruolo sull'azione delete -> un non-admin puo' eliminare prodotti via URL.
// smok nord (id=4) non e' referenziato in FK -> doDelete lo elimina e committa.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=4')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito (UI): accesso negato -> attesa pagina "Accesso negato" (ErrorPageAccess.jsp).
// REALE: nessun controllo ruolo -> delete eseguita -> redirect a ProductView.jsp (tipo=user) -> NESSUN "Accesso negato".
// FAILED = incident di sicurezza (hardening CR_02/CR_03).
WebUI.verifyTextPresent('(?i)Accesso negato', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
