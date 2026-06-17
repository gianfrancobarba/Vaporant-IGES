import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_12 — Ordinamento per nome decrescente (sort=nome, dir=DESC) (RF_GC_26) — atteso PASSED
// Ordine alfabetico inverso atteso: "voopoo drag 2" primo ... "dicodes dani" ultimo.
// Ordinamento valutato dallo screenshot (convenzione TC_4.2).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=nome&dir=DESC')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.verifyTextPresent('VOOPOO DRAG 2', false)
WebUI.verifyTextPresent('DICODES DANI', false)
WebUI.closeBrowser()
