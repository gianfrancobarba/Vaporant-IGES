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

// TC_3.6_5 — Aggiungere indirizzo con Via non compilata (RF_GP_9) — validazione client (atteso PASSED)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'AddressForm.jsp')
WebUI.delay(1)
// Tutti i campi validi tranne Via (vuota)
WebUI.setText(css('#citta'), 'Salerno')
WebUI.setText(css('#provincia'), 'SA')
WebUI.setText(css('#cap'), '84100')
WebUI.setText(css('#numero_civico'), '12')
WebUI.setText(css('#stato'), 'Italia')
Object valid = WebUI.executeJavaScript("return document.getElementById('via').checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css('.submit-button'))
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyElementPresent(css('#cap'), 5)
WebUI.closeBrowser()
