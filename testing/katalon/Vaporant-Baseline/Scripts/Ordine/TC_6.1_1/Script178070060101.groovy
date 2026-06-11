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

// TC_6.1_1 — Visualizzare checkout con prerequisiti soddisfatti (RF_GO_19) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Aggiunge kiwi (id=2) al carrello
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
WebUI.navigateToUrl(GlobalVariable.base + 'checkout.jsp')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): il checkout mostra il carrello, i dropdown indirizzi e il pulsante Acquista.
// t.mansi ha 2 indirizzi seed -> i select non sono vuoti.
WebUI.verifyTextPresent('(?i)Acquista', true)
WebUI.verifyElementPresent(css('#addressDropdown'), 5)
WebUI.closeBrowser()
