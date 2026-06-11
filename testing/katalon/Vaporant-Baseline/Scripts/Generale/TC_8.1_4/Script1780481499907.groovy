import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_8.1_4 — Pagina Termini e Condizioni (RF_GG_25) — atteso PASSED
// NB: questa pagina NON e' linkata nel Footer -> si apre via URL diretto.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'TerminiCondizioni.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('Termini e Condizioni', false)
WebUI.closeBrowser()
