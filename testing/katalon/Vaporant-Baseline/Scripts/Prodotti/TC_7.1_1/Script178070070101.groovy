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

// TC_7.1_1 — Inserire prodotto con dati validi (RF_GPR_23) — atteso PASSED (doSave chiama commit())
// Login admin: g.barba14 -> LoginControl -> ProductViewAdmin.jsp (bounce via ./product per caricare i prodotti).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Naviga a ./product per caricare i prodotti in sessione -> ProductViewAdmin con form.
WebUI.navigateToUrl(GlobalVariable.base + 'product')
WebUI.delay(2)
// Compila il form di inserimento
WebUI.setText(css("[name='name']"), 'Test Pod')
WebUI.setText(css("[name='description']"), 'Pod di prova')
WebUI.setText(css("[name='tipo']"), 'Svapo')
WebUI.setText(css("[name='colore']"), 'Nero')
WebUI.executeJavaScript("document.querySelector(\"[name='price']\").value='30.00'", null)
WebUI.executeJavaScript("document.querySelector(\"[name='quantity']\").value='10'", null)
WebUI.click(css("input[value='Aggiungi']"))
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): doSave chiama commit() -> prodotto persistito -> ricarico prodotti -> compare nel carosello.
// Il nome e' renderizzato via bean.getName() nella div.name (case-insensitive per sicurezza).
WebUI.verifyTextPresent('(?i)Test Pod', true)
WebUI.closeBrowser()
