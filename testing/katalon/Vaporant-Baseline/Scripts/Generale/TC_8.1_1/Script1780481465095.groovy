import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_8.1_1 — Pagina Contatti (RF_GG_25) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'Contatti.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('Informazioni di contatto', false)
WebUI.closeBrowser()
