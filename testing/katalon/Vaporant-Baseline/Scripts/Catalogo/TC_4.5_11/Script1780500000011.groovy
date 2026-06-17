import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_11 — Ordinamento per nome crescente (sort=nome, dir=ASC) (RF_GC_26) — atteso PASSED
// Ordine alfabetico atteso: "dicodes dani" primo ... "voopoo drag 2" ultimo.
// Ordinamento valutato dallo screenshot (convenzione TC_4.2).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=nome&dir=ASC')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.verifyTextPresent('DICODES DANI', false)
WebUI.verifyTextPresent('VOOPOO DRAG 2', false)
WebUI.closeBrowser()
