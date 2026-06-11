import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_4.4_3 — Ricerca senza corrispondenze "zzz" (RF_GC_14) — atteso PASSED ("Nessun risultato trovato")
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.setText(css('#searchInput'), 'zzz')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('Nessun risultato trovato', false)
WebUI.closeBrowser()
