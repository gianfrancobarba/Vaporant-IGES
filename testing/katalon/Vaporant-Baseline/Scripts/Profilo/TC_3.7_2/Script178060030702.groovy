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

// TC_3.7_2 — Visualizzare lo storico ordini (utente SENZA ordini) (RF_GP_10) — atteso PASSED
// Gli ordini seed appartengono tutti a t.mansi (ID 4); g.barba14 (admin) non ha ordini.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 'g.barba14@studenti.unisa.it')
WebUI.setText(css('#password'), 'ABC123.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
WebUI.takeScreenshot()
// Oracolo: l'utente non ha ordini -> la sezione "ORDINI EFFETTUATI" non viene mostrata.
WebUI.verifyTextNotPresent('ORDINI EFFETTUATI', false)
WebUI.closeBrowser()
