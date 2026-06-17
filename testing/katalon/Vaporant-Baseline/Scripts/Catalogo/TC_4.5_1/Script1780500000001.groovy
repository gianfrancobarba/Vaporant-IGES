import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_1 — Filtro fascia di prezzo: range valido [40,70] (RF_GC_26) — atteso PASSED
// Seed (7 prodotti): nel range [40,70] ricadono "voopoo drag 2" (49.99) e "smok nord" (69.99);
// fuori range "noisy creek 2" (29.99, sotto) e "kiwi" (79.99, sopra).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMin=40&priceMax=70')
WebUI.delay(2)
WebUI.takeScreenshot()
// Oracolo (UI): nel catalogo compaiono solo i prodotti con prezzo nel range.
WebUI.verifyTextPresent('VOOPOO DRAG 2', false)
WebUI.verifyTextPresent('SMOK NORD', false)
WebUI.verifyTextNotPresent('KIWI', false)
WebUI.verifyTextNotPresent('NOISY CREEK 2', false)
WebUI.closeBrowser()
