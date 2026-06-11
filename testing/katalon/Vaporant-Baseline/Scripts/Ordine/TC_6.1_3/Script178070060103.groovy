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

// TC_6.1_3 — Checkout cliente senza indirizzi (RF_GO_19) — atteso PASSED
// Crea un cliente nuovo (senza indirizzi) via UI, poi porta al checkout.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'SignForm.jsp')
WebUI.executeJavaScript("document.getElementById('nome').value='Cliente';" +
	"document.getElementById('cognome').value='SenzaIndirizzo';" +
	"document.getElementById('data_nascita').value='1999-01-01';" +
	"document.getElementById('codice_fiscale').value='CLTSNI99A01F839X';" +
	"document.getElementById('telefono').value='3310000001';" +
	"document.getElementById('email').value='nessun.indirizzo@example.com';" +
	"document.getElementById('password').value='Password1';", null)
WebUI.click(css('#submit'))
WebUI.delay(2)
// Login con il nuovo cliente
WebUI.setText(css('#email'), 'nessun.indirizzo@example.com')
WebUI.setText(css('#password'), 'Password1')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Aggiunge kiwi al carrello
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'checkout.jsp')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): cliente senza indirizzi -> i dropdown sono vuoti e compare il link "Aggiungi indirizzo..".
WebUI.verifyTextPresent('(?i)Aggiungi indirizzo', true)
WebUI.closeBrowser()
