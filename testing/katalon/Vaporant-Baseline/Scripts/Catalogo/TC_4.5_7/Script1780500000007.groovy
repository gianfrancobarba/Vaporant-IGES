import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_7 — Robustezza: prezzo massimo negativo (priceMax=-50) (RF_GC_26) — atteso PASSED
// Normalizzazione criteri: un prezzo negativo viene trattato come assente (bound ignorato), NON applicato
// alla lettera. Se fosse applicato (prezzoAttuale <= -50) il catalogo sarebbe vuoto: si verifica invece
// che il prodotto piu' costoso "dicodes dani" (249.99) sia comunque presente e che non compaia l'empty-state.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMax=-50')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('DICODES DANI', false)
WebUI.verifyTextPresent('KIWI', false)
WebUI.verifyTextNotPresent('Non ci sono prodotti disponibili!', false)
WebUI.closeBrowser()
