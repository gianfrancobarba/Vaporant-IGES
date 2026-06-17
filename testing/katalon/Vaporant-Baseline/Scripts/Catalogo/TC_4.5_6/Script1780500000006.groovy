import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_6 — Robustezza: parametro di prezzo non numerico (priceMin=abc) (RF_GC_26) — atteso PASSED
// Parsing difensivo lato servlet: un valore non numerico viene ignorato (nessun bound) -> catalogo completo,
// senza errori di sistema. Si verifica la presenza di prodotti agli estremi e l'assenza della pagina d'errore.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMin=abc')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('kiwi', false)
WebUI.verifyTextPresent('resistenza ricambio', false)
WebUI.verifyTextNotPresent('verificato un errore', false)
WebUI.closeBrowser()
