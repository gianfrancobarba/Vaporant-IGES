import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// TC_2.1_3 — Cognome non compilato (RF_GR_3) — CASO MANUALE (validazione lato client HTML5 required)
KeywordUtil.markWarning('Caso manuale: in SignForm.jsp compilare tutti i campi coi valori di riferimento (Nome=Mario, Data=2000-05-15, CF=RSSMRA00E15F839X, Telefono=3331234567, Email=mario.rossi@example.com, Password=Password1) lasciando Cognome VUOTO, poi cliccare Registrati. Atteso: il browser blocca l\'invio (campo obbligatorio).')
