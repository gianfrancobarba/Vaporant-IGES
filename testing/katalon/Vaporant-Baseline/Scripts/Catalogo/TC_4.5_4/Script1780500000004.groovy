import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_4 — Filtro fascia di prezzo incoerente: min > max (RF_GC_26) — atteso PASSED
// Regola d'oracolo di progetto: priceMin > priceMax e' un range logicamente vuoto -> catalogo vuoto
// (empty-state), NON pagina d'errore (la pagina d'errore e' riservata ai criteri di ordinamento non ammessi).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMin=100&priceMax=10')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('Non ci sono prodotti disponibili!', false)
WebUI.verifyTextNotPresent('kiwi', false)
WebUI.closeBrowser()
