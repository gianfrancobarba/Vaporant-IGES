import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// TC_2.1_4 — Data di Nascita non compilata (RF_GR_3) — CASO MANUALE (validazione lato client HTML5 required)
KeywordUtil.markWarning('Caso manuale: in SignForm.jsp compilare tutti i campi coi valori di riferimento (Nome=Mario, Cognome=Rossi, CF=RSSMRA00E15F839X, Telefono=3331234567, Email=mario.rossi@example.com, Password=Password1) lasciando Data di Nascita VUOTA, poi cliccare Registrati. Atteso: il browser blocca l\'invio (campo obbligatorio).')
