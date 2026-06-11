import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.2_2 — Aggiungere un prodotto gia' presente, sotto la disponibilita' (RF_GCR_16) — atteso PASSED (qty 2)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()   // la quantita' (2) si verifica dallo screenshot
WebUI.verifyTextPresent('kiwi', false)
WebUI.closeBrowser()
