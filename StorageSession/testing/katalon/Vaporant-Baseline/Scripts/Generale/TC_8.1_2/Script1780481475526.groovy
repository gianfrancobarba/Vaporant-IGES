import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_8.1_2 — Pagina Chi Siamo (RF_GG_25) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ChiSiamo.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('Chi Siamo', false)
WebUI.closeBrowser()
