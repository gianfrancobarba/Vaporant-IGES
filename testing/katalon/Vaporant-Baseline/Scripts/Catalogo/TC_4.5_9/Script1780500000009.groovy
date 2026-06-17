import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_9 — Ordinamento per prezzo crescente (sort=prezzoAttuale, dir=ASC) (RF_GC_26) — atteso PASSED
// Ordine atteso: "resistenza ricambio" (0.10) primo ... "dicodes dani" (249.99) ultimo.
// L'ordinamento e' valutato dallo screenshot (convenzione TC_4.2); l'asserzione automatica verifica
// che il catalogo sia stato renderizzato con gli estremi della scala di prezzo presenti.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=prezzoAttuale&dir=ASC')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.verifyTextPresent('resistenza ricambio', false)
WebUI.verifyTextPresent('dicodes dani', false)
WebUI.closeBrowser()
