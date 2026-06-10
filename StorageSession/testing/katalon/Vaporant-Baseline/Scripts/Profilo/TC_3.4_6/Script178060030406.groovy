import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// helper: crea un TestObject da un selettore CSS
TestObject css(String sel) {
	TestObject t = new TestObject(sel)
	t.addProperty('css', ConditionType.EQUALS, sel)
	return t
}

// TC_3.4_6 — Due modifiche password consecutive (RF_GP_7) — DEFECT-EXPOSING (sincronizzazione sessione-DB)
// NB: MUTA-CREDENZIALI. Eseguire su DB ripristinato.
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.base + 'loginForm.jsp')
WebUI.setText(css('#email'), 't.mansi@studenti.unisa.it')
WebUI.setText(css('#password'), '321CBA.')
WebUI.click(css('#submit'))
WebUI.delay(2)
WebUI.navigateToUrl(GlobalVariable.base + 'Utente.jsp')
WebUI.delay(1)
// 1a modifica: 321CBA. -> NuovaPass1 (attesa: successo)
WebUI.click(css('#editPasswordButton'))
WebUI.delay(1)
WebUI.setText(css('#oldPasswordInput'), '321CBA.')
WebUI.setText(css('#newPasswordInput'), 'NuovaPass1')
WebUI.click(css('#submitPasswordButton'))
WebUI.delay(2)
String msg1 = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.verifyMatch(msg1, '.*Password modificata con successo.*', true, FailureHandling.CONTINUE_ON_FAILURE)
// 2a modifica SENZA ri-login: NuovaPass1 -> AltraPass2 (attesa da requisito: successo)
WebUI.click(css('#editPasswordButton'))
WebUI.delay(1)
WebUI.setText(css('#oldPasswordInput'), 'NuovaPass1')
WebUI.setText(css('#newPasswordInput'), 'AltraPass2')
WebUI.click(css('#submitPasswordButton'))
WebUI.delay(2)
String msg2 = WebUI.getAlertText()
WebUI.acceptAlert()
WebUI.takeScreenshot()
// Oracolo (requisito): anche la 2a modifica riesce. REALE (difetto): ModifyControl confronta con la password
// nel bean di sessione, non aggiornata dopo la 1a modifica -> "Vecchia password errata. Riprova." -> la 2a
// modifica fallisce erroneamente -> FAILED = incident sincronizzazione sessione-DB (intervento CR_02).
WebUI.verifyMatch(msg2, '.*Password modificata con successo.*', true, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.closeBrowser()
