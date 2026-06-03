import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.2_5 — SQL injection sul parametro di ordinamento (RF_GC_12) — VERIFICA DI SICUREZZA
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'product?sort=nome;--')
WebUI.takeScreenshot()
// 'sort' e' concatenato nell'ORDER BY senza sanificazione -> vulnerabilita' SQLi (intervento CR_02).
// Esito da interpretare sullo screenshot; marcato WARNING.
KeywordUtil.markWarning('Verifica sicurezza SQLi: il parametro sort non e\' sanificato. Interpretare lo screenshot.')
WebUI.closeBrowser()
