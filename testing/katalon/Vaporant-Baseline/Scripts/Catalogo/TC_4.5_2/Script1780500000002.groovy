import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_2 — Filtro fascia di prezzo: solo prezzo minimo (priceMin=70) (RF_GC_26) — atteso PASSED
// BVA sul bordo inferiore: con priceMin=70 sono inclusi "kiwi" (79.99) e "dicodes dani" (249.99);
// "smok nord" (69.99) e' appena sotto il bordo -> escluso (conferma il confronto >= ?).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMin=70')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('KIWI', false)
WebUI.verifyTextPresent('DICODES DANI', false)
WebUI.verifyTextNotPresent('SMOK NORD', false)
WebUI.verifyTextNotPresent('VOOPOO DRAG 2', false)
WebUI.closeBrowser()
