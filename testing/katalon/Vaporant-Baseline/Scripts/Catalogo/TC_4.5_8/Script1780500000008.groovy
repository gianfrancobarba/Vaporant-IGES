import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_8 — Filtro disponibilita': "solo disponibili" (inStock=on) (RF_GC_26) — atteso PASSED
// Con il filtro disponibilita' attivo (quantita > 0) il prodotto seed "eleaf istick" (stock 0) deve essere
// escluso, mentre i prodotti in giacenza (es. "kiwi", stock 25) restano visibili.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?inStock=on')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextNotPresent('eleaf istick', false)
WebUI.verifyTextPresent('kiwi', false)
WebUI.closeBrowser()
