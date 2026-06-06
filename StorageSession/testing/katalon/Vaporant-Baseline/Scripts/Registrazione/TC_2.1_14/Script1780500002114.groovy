import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// TC_2.1_14 — Email in formato non valido (RF_GR_3) — CASO MANUALE (validazione lato client HTML5 type=email)
KeywordUtil.markWarning('Caso manuale: in SignForm.jsp compilare tutti i campi coi valori di riferimento (Nome=Mario, Cognome=Rossi, Data=2000-05-15, CF=RSSMRA00E15F839X, Telefono=3331234567, Password=Password1) e Email = mario.rossi.example.com (senza @), poi cliccare Registrati. Atteso: il browser blocca l\'invio (formato email non valido, type=email).')
