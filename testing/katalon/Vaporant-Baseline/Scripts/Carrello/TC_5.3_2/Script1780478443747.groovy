import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.3_2 — Rimuovere un prodotto non presente nel carrello (RF_GCR_17) — atteso PASSED (carrello invariato)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=deleteC&id=3')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('kiwi', false)
WebUI.closeBrowser()
