import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.1_2 — Visualizzare carrello vuoto (RF_GCR_15) — atteso PASSED (Totale 0.00)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('0.00', false)
WebUI.closeBrowser()
