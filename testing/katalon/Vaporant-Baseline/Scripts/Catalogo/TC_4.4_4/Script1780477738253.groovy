import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_4.4_4 — Campo di ricerca vuoto (RF_GC_14) — atteso PASSED (nessuna ricerca, area risultati nascosta)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.setText(css('#searchInput'), 'kiwi')
WebUI.delay(1)
WebUI.setText(css('#searchInput'), '')
WebUI.delay(1)
WebUI.takeScreenshot()
WebUI.verifyTextNotPresent('Nessun risultato trovato', false)
WebUI.closeBrowser()
