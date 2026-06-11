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

// TC_7.1_4 — Inserimento con Descrizione non compilata (RF_GPR_23) — validazione client (atteso PASSED)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product')
WebUI.delay(2)
// Compila tutti tranne Descrizione
WebUI.setText(css("[name='name']"), 'Test Pod')
WebUI.setText(css("[name='tipo']"), 'Svapo')
WebUI.setText(css("[name='colore']"), 'Nero')
WebUI.executeJavaScript("document.querySelector(\"[name='price']\").value='30.00'", null)
WebUI.executeJavaScript("document.querySelector(\"[name='quantity']\").value='10'", null)
// Oracolo (UI/DOM): description e' una textarea required e vuota -> invalida.
Object valid = WebUI.executeJavaScript("return document.querySelector(\"[name='description']\").checkValidity()", null)
WebUI.verifyEqual(valid, false)
WebUI.click(css("input[value='Aggiungi']"))
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyElementPresent(css("input[value='Aggiungi']"), 5)
WebUI.closeBrowser()
