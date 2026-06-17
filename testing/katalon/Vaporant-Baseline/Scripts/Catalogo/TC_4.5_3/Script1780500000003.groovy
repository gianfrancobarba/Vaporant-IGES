import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// TC_4.5_3 — Filtro fascia di prezzo: solo prezzo massimo (priceMax=30) (RF_GC_26) — atteso PASSED
// Con priceMax=30 sono inclusi "noisy creek 2" (29.99) e "resistenza ricambio" (0.10);
// "eleaf istick" (39.99) e "kiwi" (79.99) sono sopra il massimo -> esclusi.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'ProductView.jsp')
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'product?priceMax=30')
WebUI.delay(2)
WebUI.takeScreenshot()
WebUI.verifyTextPresent('NOISY CREEK 2', false)
WebUI.verifyTextPresent('RESISTENZA RICAMBIO', false)
WebUI.verifyTextNotPresent('ELEAF ISTICK', false)
WebUI.verifyTextNotPresent('KIWI', false)
WebUI.closeBrowser()
