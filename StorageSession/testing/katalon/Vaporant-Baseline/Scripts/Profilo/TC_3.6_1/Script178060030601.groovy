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

// TC_3.6_1 — Aggiungere un indirizzo con dati validi (RF_GP_9) — atteso PASSED
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
WebUI.setText(css('#cap'), '84100')
WebUI.setText(css('#via'), 'Via Roma')
WebUI.setText(css('#numero_civico'), '12')
WebUI.setText(css('#stato'), 'Italia')
WebUI.click(css('.submit-button'))
WebUI.delay(2)
// AddressControl salva e reindirizza a Utente.jsp. Ricarico esplicitamente e attendo per evitare race sul render.
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.waitForElementPresent(css('table'), 10)
WebUI.takeScreenshot()
// Oracolo (UI): il nuovo indirizzo compare tra gli indirizzi del cliente.
WebUI.verifyTextPresent('Via Roma', false)
WebUI.closeBrowser()
