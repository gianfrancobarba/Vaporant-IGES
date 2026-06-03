import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// TC_5.4_3 — Quantita' > stock (RF_GCR_18) — CASO MANUALE (validazione lato client HTML5 max=stock)
KeywordUtil.markWarning('Caso manuale: nel carrello impostare quantita=30 (stock 25) e cliccare Aggiorna. Atteso: il browser blocca l\'invio (max=25). Robustezza: il server non valida la disponibilita\'.')
