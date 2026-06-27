# Vaporant

E-commerce di sigarette elettroniche — progetto del corso **Ingegneria, Gestione ed Evoluzione del
Software (IGES)**, Laurea Magistrale in Informatica, Università degli Studi di Salerno.

---

## Descrizione

Vaporant è un'applicazione web **Java EE** (Servlet + JSP + DAO) per la vendita di sigarette elettroniche e
accessori. Nasce come **prototipo accademico** (ex progetto del corso TSW, triennale) e nel corso IGES è
oggetto di un intervento di **manutenzione ed evoluzione** di un sistema esistente.

Funzionalità offerte dal sistema:

- **Autenticazione** e **registrazione** degli utenti (cliente / amministratore);
- **Catalogo** prodotti con **filtri** (fascia di prezzo, disponibilità), **ordinamento** direzionale e
  **ricerca per nome**;
- **Carrello** e **checkout** con creazione dell'ordine;
- generazione della **fattura in PDF**;
- **area amministratore** per la gestione dei prodotti.

---

## Contesto accademico

- **Corso**: Ingegneria, Gestione ed Evoluzione del Software (IGES) — LM Informatica, UniSA.
- **Docente**: Prof. Andrea De Lucia · **Tutor**: Giusy Annunziata.
- **Tipologia di progetto**: manutenzione ed evoluzione di un sistema software esistente.
- **Team**: Gianfranco Barba (0522502025) · Francesco Corcione (0522502027).

---

## Stack tecnologico

| Ambito | Tecnologia |
|---|---|
| Linguaggio / piattaforma | Java 17, Java EE (Servlet + JSP) |
| Persistenza | MySQL 8 (database `storage`), pattern DAO, DataSource JNDI |
| Application server | Apache Tomcat 9 |
| Build | Maven (immagine Docker) · IntelliJ IDEA (sviluppo) |
| Librerie | gson, mysql-connector-java, Apache PDFBox, jBCrypt |
| Testing | Katalon Studio |

---

## Architettura

Il sistema è organizzato secondo il pattern **MVC a livelli**:

| Livello | Posizione | Ruolo |
|---|---|---|
| **Control** | `src/it/unisa/control` | Servlet: ricezione richieste e instradamento |
| **Service** | `src/it/unisa/service` | Logica applicativa (livello introdotto con il re-engineering) |
| **Model** | `src/it/unisa/model` | Bean di dominio e DAO di accesso ai dati |
| **View** | `WebContent/` | Pagine JSP |

L'accesso al database avviene tramite **DataSource JNDI** `jdbc/storage`; lo schema e i dati di esempio sono
in `db/storage.sql`. Le funzionalità sono organizzate in **8 sottosistemi** (25 requisiti funzionali):
Autenticazione, Registrazione, Profilo, Catalogo, Carrello, Ordine, Prodotti, Generale.

---

## Il lavoro di manutenzione — le 5 Change Request

L'evoluzione del sistema è stata condotta tramite cinque Change Request prioritizzate, classificate secondo
la norma **ISO/IEC 14764**. La versione pre-manutenzione *as-delivered* è congelata al tag `v1.0-baseline`.

| Change Request | Tipo (ISO/IEC 14764) | Esito |
|---|---|---|
| **CR_01 — Correzione difetti** | Correttiva | `v1.1` |
| **CR_02 — Re-engineering architetturale** | Perfettiva | `v1.2` |
| **CR_03 — Messa in sicurezza** | Preventiva | `v1.3` |
| **CR_04 — Filtri e ricerca avanzata del catalogo** | Evolutiva | `v1.4` |
| **CR_05 — Containerizzazione con Docker** | Adattiva | `v1.5` |

> Ordine di esecuzione: CR_01 → CR_02 ∥ CR_03 → CR_04 → CR_05.

---

## Testing & V&V

La verifica e validazione è **trasversale** alle Change Request (non è essa stessa una CR):

- **test di sistema black-box** ancorati ai requisiti funzionali;
- **oracoli requirement-based**, osservati esclusivamente **via interfaccia utente**;
- fixture **SeedReset** che ripristina i dati di esempio (DELETE + INSERT da `storage.sql`) prima di ogni
  caso di test, garantendo indipendenza e ripetibilità;
- famiglia documentale conforme a **ISO/IEC/IEEE 29119-3** e **IEEE 829**.

La suite di test è realizzata con **Katalon Studio** e versionata in `testing/katalon/`.

---

## Avvio con Docker *(consigliato)*

Lo stack completo (applicazione + database MySQL) si avvia in container, **senza installare nulla sull'host**
(niente IntelliJ, Tomcat o MySQL locali).

**Prerequisiti:** [Docker](https://www.docker.com/) con Docker Compose.

```bash
# 1. Configura le credenziali del database
cp .env.example .env        # poi imposta DB_PASSWORD nel file .env

# 2. Avvia lo stack (al primo avvio costruisce l'immagine)
docker compose up --build
```

All'avvio il database viene inizializzato automaticamente da `db/storage.sql` (schema + dati di esempio) e
l'applicazione è servita da Tomcat. L'app diventa raggiungibile quando il container risulta *healthy*.

- **Applicazione**: http://localhost:8080/Vaporant_IGES_war_exploded/

```bash
docker compose down       # arresta lo stack (i dati del DB persistono)
docker compose down -v    # arresta e reinizializza il DB da storage.sql
```

---

## Avvio in sviluppo (IntelliJ IDEA)

Percorso alternativo per chi sviluppa:

1. **IntelliJ IDEA Ultimate** + **Tomcat 9** (non 10) + **JDK 17** + **MySQL 8**.
2. Creare il database importando `db/storage.sql` (lo script esegue `CREATE DATABASE storage;`).
3. Configurare il **DataSource JNDI** `jdbc/storage` (definito in `WebContent/META-INF/context.xml`).
4. Copiare `mysql-connector-java` nella cartella `lib` di Tomcat.
5. Configurare l'artifact "**Web Application: Exploded**" ed eseguire su Tomcat.
6. Applicazione raggiungibile su http://localhost:8080/Vaporant_IGES_war_exploded/

---

## Credenziali di test

| Ruolo | Username | Password |
|---|---|---|
| Cliente | `t.mansi` | `321CBA.` |
| Amministratore | `g.barba14` | `ABC123.` |

---

## Struttura del repository

```
.
├── src/it/unisa/        # codice Java
│   ├── control/         # Servlet (livello control)
│   ├── service/         # logica applicativa (livello service)
│   └── model/           # bean di dominio e DAO
├── WebContent/          # pagine JSP e configurazione web
├── db/storage.sql       # schema del database + dati di esempio
├── testing/katalon/     # suite di test Katalon
├── docker/context.xml   # DataSource parametrizzato per il container
├── Dockerfile           # immagine multi-stage (build Maven + runtime Tomcat)
├── docker-compose.yml   # orchestrazione app + database
├── .env.example         # modello per le credenziali locali (.env)
└── pom.xml              # build Maven (pacchetto WAR)
```
