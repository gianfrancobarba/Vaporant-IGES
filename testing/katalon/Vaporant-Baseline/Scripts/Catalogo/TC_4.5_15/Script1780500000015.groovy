import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_15 — Sicurezza: direzione di ordinamento fuori whitelist (dir=pippo) (RF_GC_26) — atteso PASSED
// Con una colonna valida (sort=nome) ma una direzione non ammessa (solo ASC/DESC), il criterio va rifiutato
// -> reindirizzamento alla pagina d'errore. Verifica la validazione della direzione indipendente dalla colonna.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=nome&dir=pippo')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('verificato un errore', false)
WebUI.closeBrowser()
