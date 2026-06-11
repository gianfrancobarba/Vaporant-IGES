import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.4_1 — Aggiornare la quantita' a un valore valido (RF_GCR_18) — atteso PASSED (totale 239.97)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=aggiorna&id=2&quantita=3')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('239.97', false)
WebUI.closeBrowser()
