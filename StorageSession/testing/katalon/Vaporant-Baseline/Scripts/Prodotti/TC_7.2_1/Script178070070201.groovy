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

// TC_7.2_1 — Rimuovere un prodotto non referenziato (RF_GPR_24) — atteso PASSED (doDelete chiama commit())
// smok nord (id=4) non e' referenziato in Contenuto -> delete riesce e viene committata.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
// Esegue la delete tramite URL diretto (ProductControl action=delete)
WebUI.navigateToUrl(GlobalVariable.base + 'product?action=delete&id=4')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): smok nord e' stato rimosso -> non compare nel carosello di ProductViewAdmin.
WebUI.verifyTextNotPresent('(?i)smok nord', true)
WebUI.closeBrowser()
