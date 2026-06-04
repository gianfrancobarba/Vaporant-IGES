import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.3_2 — Dettaglio con id inesistente, 999 (RF_GC_13) — ROBUSTEZZA (WARNING, giudizio manuale)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'details?action=read&id=999')
WebUI.takeScreenshot()
// Oracolo da requisito: "prodotto non trovato". REALE: manca il null-check -> DetailsView mostra un prodotto
// di default (nome vuoto, 0.0, "Prodotto terminato!"). Robustezza -> incident TIR.
KeywordUtil.markWarning('Id inesistente (999): nessun null-check -> mostrato un prodotto di default. Interpretare lo screenshot (robustezza/TIR).')
WebUI.closeBrowser()
