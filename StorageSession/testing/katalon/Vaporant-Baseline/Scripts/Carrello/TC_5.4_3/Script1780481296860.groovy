import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.4_3 — Quantita' > stock, bypass client (RF_GCR_18) — oracolo da requisito (incident atteso)
// Bypass dell'HTML5 max="25" tramite GET diretto: CartControl.aggiorna e' esposta via doGet
// e non valida la disponibilita' lato server.
// Prodotto id=2 ("kiwi"), stock seed = 25 unita'.
// Oracolo da requisito: quantita' deve essere <= disponibilita' a magazzino (25).
// REALE: Cart.aggiorna imposta 30 senza validare -> FAILED = incident -> CR_02 (validazione server).
WebUI.openBrowser('')
// Aggiunge prodotto al carrello.
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=2')
WebUI.delay(1)
// Bypass diretto tramite GET: imposta quantita=30 (> stock 25) senza passare dal form HTML5.
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=aggiorna&id=2&quantita=30')
WebUI.delay(2)
WebUI.takeScreenshot()
// Legge la quantita' visualizzata nel campo input (name="quantita").
Object qta = WebUI.executeJavaScript("return document.querySelector('[name=\"quantita\"]').value", null)
// Oracolo: quantita' deve essere <= 25 (non superare lo stock).
// REALE: Cart.aggiorna non valida -> 30 accettato -> FAILED = incident.
WebUI.verifyEqual(Integer.parseInt(qta.toString()) <= 25, true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
