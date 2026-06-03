import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.1_1 — Visualizzare catalogo (RF_GC_11) — atteso PASSED
// Oracolo robusto: l'intestazione statica del catalogo (i nomi prodotto sono resi MAIUSCOLI via CSS
// e il carosello ne mostra una parte -> non affidabili come testo; l'evidenza prodotti e' lo screenshot).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('SCOPRI I NOSTRI PRODOTTI', false)
WebUI.closeBrowser()
