import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.1_1 — Visualizzare carrello con prodotti (RF_GCR_15) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('kiwi', false)
WebUI.verifyTextPresent('79.99', false)
WebUI.closeBrowser()
