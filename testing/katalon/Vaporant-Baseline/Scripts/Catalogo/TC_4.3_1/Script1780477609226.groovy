import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.3_1 — Dettaglio prodotto esistente, id 2 "kiwi" (RF_GC_13) — atteso PASSED
// NB: il nome e' reso MAIUSCOLO via CSS ("KIWI") -> uso un match case-insensitive con regex (?i).
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'details?action=read&id=2')
WebUI.takeScreenshot()
WebUI.verifyTextPresent('(?i)kiwi', true)
WebUI.verifyTextPresent('79.99', false)
WebUI.closeBrowser()
