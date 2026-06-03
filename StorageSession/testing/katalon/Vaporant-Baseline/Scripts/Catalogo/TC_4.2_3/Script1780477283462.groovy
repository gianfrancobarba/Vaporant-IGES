import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_3 — Catalogo senza parametro di ordinamento (RF_GC_12) — atteso PASSED (ordine di default)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.navigateToUrl(GlobalVariable.base + 'product')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.closeBrowser()
