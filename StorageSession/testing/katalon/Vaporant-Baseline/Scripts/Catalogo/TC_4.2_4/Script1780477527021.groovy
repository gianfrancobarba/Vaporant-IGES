import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_4 — Ordinamento su colonna inesistente (RF_GC_12) — ROBUSTEZZA (WARNING, giudizio manuale)
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=colonna_inesistente')
WebUI.takeScreenshot()
// ProductControl inghiotte la SQLException (catch + System.out) e ignora il sort -> il catalogo viene comunque
// mostrato in ordine di default. Eccezione non gestita verso l'utente -> da valutare nel TIR (robustezza/CR_02).
KeywordUtil.markWarning('Sort con colonna inesistente: SQLException inghiottita, sort ignorato. Interpretare lo screenshot (robustezza/CR_02).')
WebUI.closeBrowser()
