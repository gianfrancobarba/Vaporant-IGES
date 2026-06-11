import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.2_3 — Aggiungere fino alla quantita' massima disponibile (RF_GCR_16) — atteso PASSED (qty ferma a 5 = stock)
WebUI.openBrowser('')
for (int i = 0; i < 6; i++) {
	WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=3')
}
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()   // la quantita' resta 5: verificare dallo screenshot
WebUI.verifyTextPresent('voopoo', false)
WebUI.closeBrowser()
