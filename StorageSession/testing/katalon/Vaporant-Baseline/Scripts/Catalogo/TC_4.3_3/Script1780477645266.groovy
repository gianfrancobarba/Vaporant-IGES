import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.3_3 — Dettaglio con id non numerico "abc" (RF_GC_13) — ROBUSTEZZA (WARNING, giudizio manuale)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'details?action=read&id=abc')
WebUI.takeScreenshot()
// REALE atteso: Integer.parseInt("abc") -> NumberFormatException non gestita -> pagina di errore 500.
// Robustezza -> incident TIR.
KeywordUtil.markWarning('Id non numerico (abc): NumberFormatException -> errore 500. Interpretare lo screenshot (robustezza/TIR).')
WebUI.closeBrowser()
