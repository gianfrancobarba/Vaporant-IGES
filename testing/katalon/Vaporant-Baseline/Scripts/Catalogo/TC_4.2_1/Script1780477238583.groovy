import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_1 — Ordinare catalogo per nome (RF_GC_12) — atteso PASSED (ordine valutato dallo screenshot)
// NB: visito prima ProductView.jsp per innescare 'tipo=guest' in sessione (ProductControl lo legge: senza,
// /product andrebbe in NPE/500).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=nome')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.closeBrowser()
