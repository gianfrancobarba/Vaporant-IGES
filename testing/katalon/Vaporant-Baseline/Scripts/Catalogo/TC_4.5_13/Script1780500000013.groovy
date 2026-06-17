import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_13 — Sicurezza: colonna di ordinamento fuori whitelist nella ricerca filtrata (RF_GC_26) — atteso PASSED
// Presenza di un filtro (priceMin=10) -> percorso findFiltered. Una colonna di ordinamento non ammessa
// (whitelist) deve provocare il rifiuto del criterio e il reindirizzamento alla pagina d'errore.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMin=10&sort=colonna_inesistente')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): viene mostrata la pagina d'errore ("Si e' verificato un errore").
WebUI.verifyTextPresent('verificato un errore', false)
WebUI.closeBrowser()
