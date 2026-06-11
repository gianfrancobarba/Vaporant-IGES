import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.3_1 — Rimuovere dal carrello un prodotto presente (RF_GCR_17) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=deleteC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextNotPresent('kiwi', false)
WebUI.closeBrowser()
