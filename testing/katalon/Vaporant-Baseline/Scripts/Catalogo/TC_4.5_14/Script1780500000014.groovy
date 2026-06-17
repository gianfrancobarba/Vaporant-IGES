import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_14 — Sicurezza: tentativo di SQL injection sul parametro sort nella ricerca filtrata (RF_GC_26) — atteso PASSED
// Payload "nome;--" sul criterio di ordinamento: la whitelist ammette solo i nomi di colonna noti, percio'
// il valore va rifiutato (niente concatenazione in ORDER BY) -> reindirizzamento alla pagina d'errore.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMin=10&sort=nome;--')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('verificato un errore', false)
WebUI.closeBrowser()
