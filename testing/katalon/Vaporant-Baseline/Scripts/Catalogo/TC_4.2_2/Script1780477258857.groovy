import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_2 — Ordinare catalogo per prezzo (RF_GC_12) — atteso PASSED (ordine valutato dallo screenshot)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=prezzoAttuale')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.closeBrowser()
