import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_5 — BVA: bordo inferiore del dominio del prezzo (priceMax=0.10) (RF_GC_26) — atteso PASSED
// Il dominio impone prezzoAttuale >= 0.1; il prodotto seed "resistenza ricambio" (0.10) sta esattamente
// sul bordo. Con priceMax=0.10 deve risultare l'unico incluso (confronto <= ? inclusivo del bordo).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMax=0.10')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('resistenza ricambio', false)
WebUI.verifyTextNotPresent('noisy creek 2', false)
WebUI.verifyTextNotPresent('kiwi', false)
WebUI.closeBrowser()
