import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_8.1_3 — Pagina Privacy Policy (RF_GG_25) — atteso PASSED
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'PrivacyPolicy.jsp')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('Informativa sulla privacy', false)
WebUI.closeBrowser()
