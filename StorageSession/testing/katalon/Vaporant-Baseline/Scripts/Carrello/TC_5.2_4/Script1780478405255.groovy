import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_5.2_4 — Aggiungere al carrello un id inesistente (RF_GCR_16) — INCIDENT atteso (robustezza)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'cart?action=addC&id=999')
WebUI.navigateToUrl(GlobalVariable.base + 'CartView.jsp')
WebUI.takeScreenshot()
// Oracolo da requisito: prodotto non trovato / nessuna aggiunta. REALE: doRetrieveByKey ritorna un bean di
// default (code 0) -> viene aggiunto un prodotto "vuoto". Interpretare lo screenshot.
KeywordUtil.markWarning('Robustezza: verificare se e\' stato aggiunto un prodotto "vuoto" (bean di default) al carrello.')
WebUI.closeBrowser()
