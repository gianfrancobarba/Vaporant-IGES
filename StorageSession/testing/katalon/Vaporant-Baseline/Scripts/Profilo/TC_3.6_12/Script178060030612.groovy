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

// TC_3.6_12 — Aggiungere indirizzo con Cap oltre 5 caratteri (RF_GP_9) — oracolo da requisito (incident atteso)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'AddressForm.jsp')
WebUI.delay(1)
WebUI.setText(css('#citta'), 'Salerno')
WebUI.setText(css('#provincia'), 'SA')
WebUI.setText(css('#cap'), '841000')
WebUI.setText(css('#via'), 'Via Roma')
WebUI.setText(css('#numero_civico'), '12')
WebUI.setText(css('#stato'), 'Italia')
WebUI.click(css('.submit-button'))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo da requisito (UI): Cap oltre la lunghezza (CHAR(5)) va RIFIUTATO -> si resta sul form. REALE: troncato/errore -> FAILED = incident (CR_02).
WebUI.verifyElementPresent(css('#citta'), 5, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
