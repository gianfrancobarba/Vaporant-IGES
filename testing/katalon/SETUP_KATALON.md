# SETUP & GUIDA OPERATIVA — Katalon Studio (Testing di baseline Vaporant)

*Laurea Magistrale in Informatica — Università degli Studi di Salerno*
*Corso di Ingegneria, Gestione ed Evoluzione del Software — Prof. Andrea De Lucia*

> **Scopo.** Allestire la suite di test di sistema **end-to-end** con **Katalon Studio** ed **eseguirla** sul
> sistema *as-delivered* (baseline, pre-manutenzione), per raccogliere gli **esiti reali** e le **evidenze
> (screenshot)** che alimenteranno i documenti **VAPORANT_TER_V_1.0** (esecuzione) e **VAPORANT_TIR_V_1.0**
> (incident). La suite è **black-box** (ancorata ai requisiti) → resta valida dopo la reingegnerizzazione (CR_01)
> e si riesegue come **regressione** (test-all) dopo ogni CR.
>
> **Riferimento dei casi di test**: `docs/testing/VAPORANT_TCS_V_1.0/VAPORANT_TCS_V_1.0.md`.
> Questa guida copre **solo** i casi *eseguibili al baseline*; gli altri sono **Blocked** (vedi §5).

---

## 0. Inquadramento — cosa è eseguibile ORA e cosa no

Sul DB `storage` *as-delivered* il **login è rotto** (`indirizzoFatt` assente in `storage.sql` → `SQLException`
in `findByCred`): tutta l'area autenticata è quindi inaccessibile. Di conseguenza, al baseline:

| Gruppo | TC | Eseguibile? | Esito atteso nel TER |
|---|---|---|---|
| Catalogo (ospite) | TC_4.1 – TC_4.4 | ✅ Sì | **Pass** (salvo casi robustezza/sicurezza → Fail/incident) |
| Carrello (ospite) | TC_5.1 – TC_5.4 | ✅ Sì | **Pass** (salvo robustezza) |
| Pagine informative (ospite) | TC_8.1 | ✅ Sì | **Pass** |
| Login | TC_1.1 | ✅ Sì (ma fallisce) | **Fail** — `SQLException: indirizzoFatt` |
| Registrazione | TC_2.1 | ✅ Sì (ma fallisce) | **Fail** — stesso difetto (`saveUser`) |
| Logout, Profilo, Ordine, Prodotti | TC_1.2, TC_3.*, TC_6.*, TC_7.* | ⛔ No (serve login) | **Blocked** — non si scriptano ora |

> ⚠️ **Non correggere il difetto `indirizzoFatt` ora.** È voluto: è il primo incident della baseline (INC-01/02)
> e giustifica la CR_03. Verrà sanato come **primo step di implementazione (3.0)**, dopodiché l'area autenticata
> diventerà eseguibile e questa stessa suite verrà **estesa** in regressione.

---

## 1. Prerequisiti

1. **Sistema Vaporant in esecuzione** su Tomcat 9 (da IntelliJ, vedi `docs/SETUP_INTELLIJ.md`).
   - Verifica che il catalogo carichi nel browser. URL di riferimento (context di default dell'artifact):
     `http://localhost:8080/Vaporant_IGES_war_exploded/ProductView.jsp`
   - 📌 **Annota il tuo `<context>`** (la parte dopo `:8080/`): in questa guida lo chiamo `<context>`.
     Se l'app è avviata, copialo dalla barra degli indirizzi.
2. **JDK** già installato (lo stesso usato per IntelliJ; Katalon include un proprio runtime, va bene comunque).
3. **Browser Chrome** installato (Katalon usa per default ChromeDriver, già incluso).
4. **Katalon Studio (Free)** — download: <https://katalon.com/download> → "Katalon Studio". Richiede una
   registrazione gratuita (account Katalon) al primo avvio.

---

## 2. Installazione Katalon Studio

1. Scarica l'archivio per Windows ed estrailo in una cartella **senza spazi nel percorso** (es. `C:\Katalon`).
2. Avvia `katalon.exe`. Al primo avvio: accedi/registra l'account gratuito → seleziona il piano **Free**.
3. Attendi l'attivazione (serve connessione internet la prima volta).

---

## 3. Creazione del progetto

1. **File → New → Project**.
   - **Name**: `Vaporant-Baseline`
   - **Location**: `C:\Users\gianf\Documents\Vaporant-IGES\testing\katalon`
     → così il progetto vive **fuori** da `docs/` (che è gitignored) ed è **versionabile**.
   - **Type**: *WebUI*.
2. (Opzionale ma consigliato) **Project → Settings → Execution → Default → Base URL**:
   `http://localhost:8080/<context>` — sostituisci `<context>` col tuo (es. `Vaporant_IGES_war_exploded`).
   Così negli step puoi usare URL relativi.

> **Struttura che Katalon crea** (cartelle del progetto): `Test Cases/`, `Test Suites/`, `Object Repository/`,
> `Profiles/`, `Reports/`. Le **evidenze** delle esecuzioni finiscono in `Reports/`.

---

## 4. Convenzioni di naming (tracciabilità con la TCS)

Per mantenere la tracciabilità **1:1** con la TCS, nomina i Test Case **esattamente** con l'ID del caso fisico:

- Cartelle Test Cases **per sottosistema**: `Test Cases/Catalogo/`, `Test Cases/Carrello/`,
  `Test Cases/Generale/`, `Test Cases/Autenticazione/`.
- Nome del Test Case = ID TCS, es. `TC_4.1_1`, `TC_4.2_5`, `TC_5.2_1`, `TC_8.1_1`, `TC_1.1_1`.
- **Test Suites** = un raggruppamento per sottosistema, es. `TS_Catalogo`, `TS_Carrello`, `TS_Generale`,
  `TS_Autenticazione`. Una **Test Suite Collection** `TSC_Baseline` le esegue tutte (utile per la regressione).

---

## 5. Quali casi registrare adesso (checklist)

Registra **solo** i casi eseguibili. La colonna "Modalità" indica come automatizzarli con Katalon.

| Test Case | Cosa fa | Modalità in Katalon |
|---|---|---|
| **TC_4.1_1** | Catalogo mostra i 4 prodotti seed | Navigazione + verifica testo (es. "kiwi") |
| TC_4.1_2 | Catalogo vuoto → messaggio | ⚠️ richiede DB svuotato → **manuale/opzionale** (non alterare la baseline) |
| **TC_4.2_1/2/3** | Ordinamento per `nome` / `prezzoAttuale` / default (via URL) | Navigazione URL + verifica ordine |
| **TC_4.2_4** | `sort` colonna inesistente → errore | Navigazione URL + verifica errore (atteso Fail/incident) |
| **TC_4.2_5** | `sort` payload SQLi → atteso rifiuto | Navigazione URL + verifica (atteso vulnerabilità → incident) |
| **TC_4.3_1** | Dettaglio prodotto id 2 (kiwi) | Click prodotto / URL + verifica nome+prezzo |
| **TC_4.3_2** | Dettaglio id inesistente (999) | URL + verifica (atteso robustezza → incident) |
| **TC_4.3_3** | Dettaglio id non numerico (`abc`) | URL + verifica errore 500 (incident) |
| **TC_4.4_1/2/3/4** | Ricerca incrementale: `kiwi` / `voo` / `zzz` / vuoto | Digita nel campo ricerca + verifica dropdown |
| **TC_5.1_1/2** | Carrello con/ senza prodotti | Aggiungi → apri carrello → verifica |
| **TC_5.2_1/2/3/4** | Aggiungi al carrello (vari stati) | URL `cart?action=addC&id=..` + verifica |
| **TC_5.3_1/2** | Rimuovi dal carrello | URL `cart?action=deleteC&id=..` + verifica |
| **TC_5.4_1** | Aggiorna quantità a valore valido | Form carrello: imposta qty + "Aggiorna" |
| TC_5.4_2/3/4/5 | Quantità invalida (0 / >stock / vuota / `abc`) | ⚠️ vincolo **client-side** HTML5 → **verifica manuale** (Katalon non intercetta bene il blocco nativo del browser); annotare l'esito |
| **TC_8.1_1/2/3/4** | Pagine info (Contatti, Chi siamo, Privacy, Termini) | Click link nel Footer + verifica pagina |
| **TC_1.1_1** | Login con credenziali valide | Compila form + submit → verifica (atteso **Fail**: errore) |
| **TC_2.1_1** | Registrazione con dati validi | Compila form + submit → verifica (atteso **Fail**: errore) |

> I casi marcati ⚠️ **manuale** o "DB svuotato" non vanno automatizzati: li esegui a mano e mi riporti l'esito;
> nel TER risulteranno come eseguiti manualmente. Tutti gli altri si automatizzano.

---

## 6. Come registrare un flusso (Record/Replay)

Katalon registra le tue azioni nel browser e le trasforma in passi ripetibili.

1. Barra strumenti → **Record Web** (icona ▶ rossa). Si apre Chrome pilotato da Katalon.
2. Nel campo URL della finestra di record, inserisci l'URL iniziale del caso (vedi §7).
3. Esegui le azioni del caso (click, digitazione…). Katalon le cattura come step.
4. **Aggiungi una verifica (oracolo)**: durante o dopo la registrazione, aggiungi uno step
   *Verify Element Text* / *Verify Element Present* sull'elemento che rappresenta l'oracolo
   (es. il testo "kiwi", il totale "79.99 €", il messaggio "Nessun risultato trovato").
5. **Stop** → salva nella cartella/nome corretti (§4). Gli elementi cliccati finiscono in *Object Repository*.
6. **Replay** (▶ verde) per verificare che il caso giri da solo.

> Per i casi "via URL" (ordinamento, dettaglio, addC/deleteC) puoi evitare il record e creare il Test Case a
> mano con due step: `WebUI.openBrowser('')` → `WebUI.navigateToUrl('<url>')` → `WebUI.verifyTextPresent('...')`.
> È più rapido e robusto.

---

## 7. Flussi concreti (URL e oracoli) — sostituisci `<context>`

Base: `http://localhost:8080/<context>/`

### Catalogo (TS_Catalogo)
- **TC_4.1_1** → apri `/<context>/ProductView.jsp` · *oracolo*: presenti i nomi dei prodotti seed (es. `kiwi`, `smok nord`).
- **TC_4.2_1** → `/<context>/product?sort=nome` · *oracolo*: catalogo ordinato per nome.
- **TC_4.2_2** → `/<context>/product?sort=prezzoAttuale` · *oracolo*: ordinato per prezzo.
- **TC_4.2_3** → `/<context>/product` · *oracolo*: ordine di default.
- **TC_4.2_4** → `/<context>/product?sort=colonna_inesistente` · *oracolo atteso*: errore gestito;
  **reale atteso**: pagina d'errore / catalogo non mostrato → **Fail → incident TIR**.
- **TC_4.2_5** (sicurezza) → `/<context>/product?sort=nome;--` · *oracolo atteso*: input rifiutato;
  **reale atteso**: parametro concatenato senza sanificazione → **vulnerabilità SQLi → incident TIR** (CR_02).
- **TC_4.3_1** → `/<context>/details?action=read&id=2` · *oracolo*: nome `kiwi`, prezzo `79.99€`, bottone "Aggiungi al carrello".
- **TC_4.3_2** → `/<context>/details?action=read&id=999` · **reale atteso**: prodotto di default mostrato (no null-check) → **incident**.
- **TC_4.3_3** → `/<context>/details?action=read&id=abc` · **reale atteso**: `NumberFormatException` → **errore 500 → incident**.
- **TC_4.4_1/2/3/4** → nella barra di ricerca dell'Header digita `kiwi` / `voo` / `zzz` / *(vuoto)* ·
  *oracoli*: rispettivamente match `kiwi`; match `voopoo drag 2`; "Nessun risultato trovato"; nessuna ricerca.

### Carrello (TS_Carrello)
> Il carrello è in sessione: per stati ripetibili, **all'inizio del caso** porta il carrello allo stato voluto
> con gli URL `addC`/`deleteC`, poi verifica.
- **TC_5.2_1** → `/<context>/cart?action=addC&id=2` · *oracolo*: `kiwi` qty 1, totale `79.99 €`.
- **TC_5.2_2** → due volte `addC&id=2` · *oracolo*: qty `kiwi` = 2.
- **TC_5.2_3** → porta `voopoo` (id 3) a qty 5 poi `addC&id=3` · *oracolo*: qty resta 5 (stock 5).
- **TC_5.2_4** → `/<context>/cart?action=addC&id=999` · **reale atteso**: aggiunto bean "vuoto" → **incident**.
- **TC_5.1_1** → dopo `addC&id=2`, apri `CartView.jsp` · *oracolo*: `kiwi`, 79.99€, totale 79.99 €.
- **TC_5.1_2** → carrello vuoto, apri `CartView.jsp` · *oracolo*: nessun prodotto, totale `0.00 €`.
- **TC_5.3_1** → con `kiwi` nel carrello: `/<context>/cart?action=deleteC&id=2` · *oracolo*: rimosso, totale 0.00 €.
- **TC_5.3_2** → con `kiwi` nel carrello: `deleteC&id=3` (non presente) · *oracolo*: carrello invariato.
- **TC_5.4_1** → nel carrello imposta quantità `kiwi` = 3 → "Aggiorna" · *oracolo*: qty 3, totale `239.97 €`.
- TC_5.4_2/3/4/5 → **manuale**: prova qty `0` / `30` / vuota / `abc` → il browser blocca l'invio (vincoli HTML5);
  annota se il blocco avviene (oracolo) o se viene aggirato (incident).

### Pagine informative (TS_Generale)
- **TC_8.1_1/2/3/4** → dal Footer apri Contatti / Chi siamo / Privacy Policy / Termini e Condizioni ·
  *oracolo*: la rispettiva pagina si apre correttamente.

### Autenticazione — casi che falliranno (TS_Autenticazione)
- **TC_1.1_1** → apri `/<context>/loginForm.jsp`, inserisci email `g.barba14@studenti.unisa.it` e password
  `ABC123.`, premi "vai" · *oracolo (requisito)*: login riuscito, redirect a homepage da loggato.
  **Reale atteso**: errore (`SQLException: Column 'indirizzoFatt' not found`) → **Fail → INC-01**.
  → step di verifica: *Verify* che NON sei loggato / che compare l'errore/pagina d'errore.
- **TC_2.1_1** → apri `/<context>/SignForm.jsp`, compila tutti i campi con dati validi (usa un'email nuova,
  CF di 16 caratteri, ecc. — vedi TCS §2.2 per i dati), invia · *oracolo (requisito)*: registrazione riuscita.
  **Reale atteso**: errore (stesso `indirizzoFatt` in `saveUser`) → **Fail → INC-02**.

---

## 8. Esecuzione e raccolta evidenze (per il TER)

1. Crea le **Test Suite** (`TS_Catalogo`, `TS_Carrello`, `TS_Generale`, `TS_Autenticazione`) e trascina dentro
   i Test Case corrispondenti.
2. Esegui ogni suite (**Run**). Katalon genera un report in `Reports/` con esito per ogni caso + log.
3. **Screenshot**: abilita la cattura su fallimento (e/o aggiungi step `WebUI.takeScreenshot('...')` nei punti
   chiave). In *Project Settings → Execution* puoi attivare screenshot automatici. Le immagini finiscono nel
   report della run.
4. **Esporta/individua** per ciascun caso: esito (Passed/Failed) + screenshot dell'oracolo. Questi vanno nel TER.

> 📌 **Importante per gli oracoli "difetto"**: casi come TC_4.2_4/5, TC_4.3_2/3, TC_5.2_4 hanno *oracolo da
> requisito* diverso dal comportamento reale → in Katalon risulteranno **Failed** (giusto così): sono i nostri
> **incident**. Cattura lo screenshot dell'errore/comportamento anomalo: è la prova per il TIR.

---

## 9. Cosa riportarmi (così scrivo TER + TIR senza rework)

Per ogni Test Case eseguito, mandami:
1. **Esito**: Passed / Failed / (Manuale: pass/fail) / Blocked.
2. **Screenshot** dell'oracolo (o dell'errore).
3. Per i Failed: il **messaggio d'errore** visibile (e, se utile, la riga del log Tomcat/Katalon).

Con questi dati compilo **VAPORANT_TER_V_1.0** (tabella esiti + evidenze + riepilogo conteggi) e
**VAPORANT_TIR_V_1.0** (un incident per fault: INC-01 login, INC-02 registrazione, + robustezza/sicurezza emersi).

---

## 10. Versionamento (i comandi git li esegui TU)

Il progetto Katalon è **versionabile** (suite di regressione riusabile), a differenza di `docs/` (ignorata).
Quando vorrai committarlo (dal branch `baseline-testing`):
- Aggiungi un `.gitignore` dentro `testing/katalon/` per escludere gli output volatili:
  ```
  /Reports/
  /Libs/
  /bin/
  .settings/
  ```
- Versiona invece `Test Cases/`, `Test Suites/`, `Object Repository/`, `Profiles/` e i file `.prj`.

> ⚠️ **Profilo di esecuzione `Profiles/default.glbl` — NON versionato.** Contiene valori macchina-specifici e una
> credenziale (`mysqlExe`, `dbUser`, `dbPass`), quindi è escluso dal versionamento (`.gitignore`). Al **primo
> setup** copia il template versionato `Profiles/default.glbl.example` → `Profiles/default.glbl` e adatta i valori
> al tuo ambiente (path del client `mysql`, utente/password del DB locale). Senza questo file la fixture SeedReset
> non parte.

> Suggerimento: tieni i **report/screenshot** delle run di baseline anche in una cartella a parte (o allegali al
> TER), perché `Reports/` è ignorato.

---

## 11. Troubleshooting

| Sintomo | Causa probabile | Rimedio |
|---|---|---|
| Katalon non apre Chrome | versione ChromeDriver ≠ Chrome | *Tools → Update WebDrivers* in Katalon |
| URL 404 | `<context>` errato | Verifica il context reale dall'app in esecuzione e correggi la Base URL |
| Pagina bianca / errore 500 ovunque | Tomcat non avviato o DB non connesso | Avvia l'app da IntelliJ; verifica MySQL e `jdbc/storage` |
| Login "non fallisce come atteso" | stai usando un DB già patchato | la baseline richiede `storage.sql` *as-delivered* (con `indirizzoFatt` mancante) |
| Lo step di verifica non trova l'elemento | locator fragile (Object Repository) | usa un locator più stabile (testo/CSS) o *Verify Text Present* sul testo dell'oracolo |

---

## 12. WALKTHROUGH passo-passo (per chi non conosce Katalon)

> Questa sezione è la guida **operativa esplicita**: spiega *cosa cliccare* e dà gli **script pronti** da
> copia-incollare per ogni caso eseguibile. Tutti i locator/URL qui sotto sono **verificati sul codice** di
> Vaporant (non inventati). Prerequisito: **l'app deve essere avviata** su Tomcat (catalogo visibile nel browser).
>
> ⚠️ **AGGIORNAMENTO**: nel progetto `Vaporant-Baseline` gli script sono **GIÀ POPOLATI** (scheda Script di ogni
> Test Case) e l'URL è centralizzato nella **GlobalVariable `base`** (`Profiles/default → base`). Quindi NON
> devi più copia-incollare: apri il progetto in Katalon → **tasto destro sul progetto → Refresh** → vai
> direttamente a **eseguire** le Test Suite (§12.8). Se il context Tomcat è diverso da
> `http://localhost:8080/Vaporant_IGES_war_exploded/`, cambialo **in un solo punto**: la GlobalVariable `base`.
> Le sezioni §12.2–§12.7 restano come riferimento per capire/modificare gli script.

### 12.0 I due metodi
- **Metodo A — "Script" (copia-incolla)**: per i casi che si attivano con un **URL** (catalogo, dettaglio,
  carrello, pagine informative). Incolli poche righe e funziona. **Inizia da qui.**
- **Metodo B — "Record" (registrazione)**: per i casi con **form** (login, registrazione) e la **ricerca AJAX**.
  Katalon registra i tuoi click e crea da solo i riferimenti agli elementi (*Object Repository*).

### 12.1 Imposta UNA volta il tuo "context"
Guarda la barra degli indirizzi quando l'app gira, es.
`http://localhost:8080/Vaporant_IGES_war_exploded/ProductView.jsp`.
La parte fino allo `/` finale è la tua **base URL**: `http://localhost:8080/Vaporant_IGES_war_exploded/`.
La incollerai nella variabile `base` di ogni script (sostituendo il valore d'esempio).

### 12.2 Metodo A — il template
Per **ogni** caso via URL: doppio click sul Test Case → linguetta **Script** (in basso) → **cancella tutto** →
incolla questo template e sostituisci le **due** parti marcate `<<...>>`:

```groovy
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String base = 'http://localhost:8080/Vaporant_IGES_war_exploded/'   // <-- il TUO context

WebUI.openBrowser('')
WebUI.navigateToUrl(base + '<<URL_DEL_CASO>>')
WebUI.verifyTextPresent('<<TESTO_ORACOLO>>', false)
WebUI.takeScreenshot()
WebUI.closeBrowser()
```
Poi clicca la **freccia verde ▶ (Run)** in alto → scegli **Chrome**. In basso, nel **Log Viewer**, l'esito è
**PASSED** (verde) o **FAILED** (rosso). Lo screenshot finisce in `Reports/`.

> **Keyword utili**: `verifyTextPresent('testo', false)` = il testo c'è (oracolo positivo);
> `verifyTextNotPresent('testo', false)` = il testo NON c'è; `delay(2)` = attendi 2 secondi (per l'AJAX);
> `takeScreenshot()` = cattura evidenza.

### 12.3 Catalogo — script pronti (cartella `Test Cases/Catalogo`)
Sostituisci nel template `<<URL_DEL_CASO>>` e `<<TESTO_ORACOLO>>` con questi valori:

| Test Case | `<<URL_DEL_CASO>>` | `<<TESTO_ORACOLO>>` | Esito atteso |
|---|---|---|---|
| **TC_4.1_1** | `ProductView.jsp` | `kiwi` | **PASSED** |
| **TC_4.2_1** | `product?sort=nome` | `kiwi` *(+ screenshot per l'ordine)* | PASSED |
| **TC_4.2_2** | `product?sort=prezzoAttuale` | `kiwi` *(+ screenshot)* | PASSED |
| **TC_4.2_3** | `product` | `kiwi` *(+ screenshot)* | PASSED |
| **TC_4.3_1** | `details?action=read&id=2` | `kiwi` | PASSED |

> Per **TC_4.2_*** l'oracolo vero è *l'ordine* dei prodotti: la verifica testuale conferma solo che il catalogo
> si carichi; **l'ordine lo documenti con lo screenshot** (guardalo a occhio e allega l'immagine).

**Casi "difetto" del Catalogo** (qui l'oracolo da requisito ≠ comportamento reale → l'esito sarà **FAILED**: è
*corretto*, sono i nostri incident). Usa questo script e **fai sempre lo screenshot**:

`TC_4.2_4` (ordinamento su colonna inesistente):
```groovy
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String base = 'http://localhost:8080/Vaporant_IGES_war_exploded/'
WebUI.openBrowser('')
WebUI.navigateToUrl(base + 'product?sort=colonna_inesistente')
WebUI.takeScreenshot()                          // evidenza dell'errore (incident TIR)
WebUI.verifyTextPresent('kiwi', false)          // atteso PASSED se gestito; REALE: FAILED (catalogo non mostrato)
WebUI.closeBrowser()
```
- `TC_4.2_5` (SQL injection): come sopra ma URL `product?sort=nome;--` → screenshot; è una **verifica di sicurezza**.
- `TC_4.3_2` (id inesistente): URL `details?action=read&id=999` → screenshot (mostra un prodotto "vuoto": robustezza).
- `TC_4.3_3` (id non numerico): URL `details?action=read&id=abc` → screenshot (atteso errore 500).

### 12.4 Carrello — script pronti (cartella `Test Cases/Carrello`)
Il carrello è in **sessione**: ogni script apre un browser pulito (`openBrowser`), quindi **prepara lo stato**
con le navigazioni `addC`/`deleteC` *dentro* lo stesso script, poi verifica. Esempi completi:

`TC_5.2_1` (aggiungi "kiwi"):
```groovy
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String base = 'http://localhost:8080/Vaporant_IGES_war_exploded/'
WebUI.openBrowser('')
WebUI.navigateToUrl(base + 'cart?action=addC&id=2')   // aggiunge kiwi (id 2)
WebUI.navigateToUrl(base + 'CartView.jsp')
WebUI.verifyTextPresent('kiwi', false)
WebUI.verifyTextPresent('79.99', false)
WebUI.takeScreenshot()
WebUI.closeBrowser()
```

| Test Case | Navigazioni (in ordine) | Verifica | Esito |
|---|---|---|---|
| **TC_5.1_1** | `cart?action=addC&id=2` → `CartView.jsp` | `kiwi` + `79.99` | PASSED |
| **TC_5.1_2** | `CartView.jsp` *(carrello vuoto)* | `0.00` | PASSED |
| **TC_5.2_1** | `cart?action=addC&id=2` → `CartView.jsp` | `kiwi` | PASSED |
| **TC_5.2_2** | `addC&id=2` ×2 → `CartView.jsp` | screenshot (quantità = 2) | PASSED |
| **TC_5.2_3** | `addC&id=3` ×6 → `CartView.jsp` | screenshot (quantità ferma a 5 = stock) | PASSED |
| **TC_5.3_1** | `addC&id=2` → `cart?action=deleteC&id=2` → `CartView.jsp` | `verifyTextNotPresent('kiwi')` | PASSED |
| **TC_5.3_2** | `addC&id=2` → `deleteC&id=3` → `CartView.jsp` | `kiwi` (ancora presente) | PASSED |
| **TC_5.4_1** | `addC&id=2` → `cart?action=aggiorna&id=2&quantita=3` → `CartView.jsp` | `239.97` | PASSED |
| **TC_5.2_4** | `cart?action=addC&id=999` → `CartView.jsp` | screenshot (aggiunto bean "vuoto": robustezza) | **FAILED/incident** |

> **TC_5.4_2/3/4/5** (quantità `0` / `30` / vuota / `abc`): sono vincoli **lato browser** (HTML5 `min/max/required/
> number`) e si verificano **a mano** nel carrello, non con lo script. Prova ad aggiornare con quei valori e
> annota: il browser blocca l'invio? (oracolo rispettato) o lo accetta? (incident). Riportami l'esito a parole.

### 12.5 Pagine informative — script pronti (cartella `Test Cases/Generale`)
| Test Case | `<<URL_DEL_CASO>>` | `<<TESTO_ORACOLO>>` | Nota |
|---|---|---|---|
| **TC_8.1_1** | `Contatti.jsp` | *(una parola che vedi sulla pagina, es. `Contatti`)* | — |
| **TC_8.1_2** | `ChiSiamo.jsp` | *(es. `Chi siamo`)* | — |
| **TC_8.1_3** | `PrivacyPolicy.jsp` | *(es. `Privacy`)* | — |
| **TC_8.1_4** | `TerminiCondizioni.jsp` | *(es. `Termini`)* | ⚠️ **non** linkata nel footer → apri via URL |

> Apri prima ogni pagina nel browser, scegli **una parola che vedi** e usala come `<<TESTO_ORACOLO>>` (così
> l'oracolo è fedele, senza inventare testi).

### 12.6 Metodo B — Login e Registrazione (Record)
Questi casi al baseline **falliscono** per il difetto `indirizzoFatt`: l'esito **FAILED è quello atteso** (sono
gli incident INC-01 e INC-02). Fai sempre lo **screenshot** della pagina d'errore.

**Login — `TC_1.1_1`** (cartella `Test Cases/Autenticazione`):
1. Apri `TC_1.1_1` → in alto **Record Web** (▶ rossa) → si apre un browser.
2. Vai su `…/loginForm.jsp`.
3. Clicca il campo **Email** e scrivi `t.mansi@studenti.unisa.it`; campo **Password** e scrivi `321CBA.`; clicca **Accedi**.
4. **Stop** → conferma il salvataggio degli elementi (Object Repository).
5. Aggiungi (opzionale) uno step di verifica e **Run ▶**. *Atteso:* **FAILED** + pagina d'errore → screenshot per il TIR.

**Registrazione — `TC_2.1_1`**: identico, ma su `…/SignForm.jsp`, compilando:
`Nome=Mario`, `Cognome=Rossi`, `Data di Nascita=15/05/2000`, `Codice Fiscale=RSSMRA00E15F839X`,
`Telefono=3331234567`, `Email=mario.rossi@example.com`, `Password=Password1` → **Registrati**. *Atteso:* **FAILED** (INC-02).

### 12.7 Metodo B — Ricerca (Record)
**TC_4.4_1** (cartella `Test Cases/Catalogo`):
1. Apri il caso → **Record Web** → vai su `…/ProductView.jsp`.
2. Clicca la barra **Cerca** in alto e digita `kiwi`. Aspetta che compaia il menù a tendina con il risultato.
3. **Stop** → salva. Aggiungi uno step *Verify Text Present* = `kiwi`. **Run ▶** → PASSED.
- **TC_4.4_2**: testo `voo` → atteso `voopoo drag 2`.
- **TC_4.4_3**: testo `zzz` → atteso `Nessun risultato trovato`.
- **TC_4.4_4**: testo vuoto → nessun risultato mostrato.

### 12.8 Eseguire tutto e raccogliere le evidenze
1. **Crea le Test Suite**: tasto destro su `Test Suites` → *New → Test Suite* → crea `TS_Catalogo`, `TS_Carrello`,
   `TS_Generale`, `TS_Autenticazione`. Apri ognuna e **trascina dentro** i Test Case corrispondenti.
2. Apri una Test Suite → **Run ▶**: Katalon esegue tutti i casi e genera un **report** in `Reports/` con
   esito per caso + log + screenshot.
3. (Consigliato) *Project Settings → Execution* → abilita lo **screenshot automatico** sui fallimenti.

### 12.9 Cosa inviarmi (per scrivere TER + TIR senza rework)
Per ogni Test Case eseguito mandami: **(1)** esito (PASSED / FAILED / Manuale / Blocked), **(2)** lo
**screenshot** dell'oracolo o dell'errore, **(3)** per i FAILED il **messaggio d'errore** visibile (e, se utile,
la riga nel log di Tomcat/Katalon). Con questi dati compilo **VAPORANT_TER_V_1.0** e **VAPORANT_TIR_V_1.0**.

> 💡 **Consiglio di marcia**: parti dalla suite **Catalogo** (Metodo A, i più facili), prendi confidenza col
> ciclo *incolla → Run → leggi esito*, poi passa a Carrello, Generale e infine Autenticazione (Record).
