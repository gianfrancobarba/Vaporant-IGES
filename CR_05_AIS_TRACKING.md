# CR_05 — AIS Tracking (Containerizzazione Docker)

*Documento vivo di tracciamento progressivo degli interventi per la manutenzione adattiva (CR_05).*

## Unità 1: `pom.xml` (Build Maven)
- **Commit atteso:** `build: introduzione della build Maven per il pacchetto applicativo`
- **File toccati (SIS parziale):**
  - `pom.xml` (N - Nuovo file)
- **Note / Razionale:** 
  Introdotto il descrittore Maven in ottica *additiva* per permettere la compilazione della WAR all'interno del container. Il file rispetta il layout proprietario pre-esistente (`<sourceDirectory>src</sourceDirectory>`, `<warSourceDirectory>WebContent</warSourceDirectory>`). I jar manuali forniti in `WEB-INF/lib` sono stati esclusi dal packaging per evitare conflitti e duplicazioni, delegando la risoluzione a Maven. Le dipendenze Servlet e MySQL sono marcate come `provided` (quest'ultima verrà copiata manualmente nella cartella `lib/` di Tomcat per servire il DataSource JNDI).

## Unità 2: Parametrizzazione credenziali (`docker/context.xml` e `.dockerignore`)
- **Commit atteso:** `feat: parametrizzazione della sorgente dati tramite variabili d'ambiente`
- **File toccati (SIS parziale):**
  - `docker/context.xml` (N - Nuovo file)
  - `.dockerignore` (N - Nuovo file)
- **Note / Razionale:** 
  Creata una variante del file `context.xml` in cui l'URL, l'username e la password del DataSource JNDI sono estratti dall'ambiente tramite placeholder (es. `${DB_USER}`). Questo file sarà iniettato nell'immagine Docker, garantendo la totale assenza di credenziali hardcoded e preservando il `context.xml` originale per lo sviluppo con IntelliJ. È stato inoltre configurato il `.dockerignore` per limitare il contesto di build (escludendo `out/`, `testing/`, `docs/`, ecc.), riducendo tempi di build e dimensioni.

## Unità 3: `Dockerfile` multi-stage
- **Commit atteso:** `feat: containerizzazione dell'applicazione con Dockerfile multi-stage`
- **File toccati (SIS parziale):**
  - `Dockerfile` (N - Nuovo file)
- **Note / Razionale:** 
  Definito il container applicativo con un approccio multi-stage. Lo stage di `build` usa Maven per impacchettare l'applicazione partendo dai sorgenti. Lo stage di `runtime` usa Tomcat 9, ripulisce le webapp di default, copia il connettore MySQL nelle librerie condivise (`$CATALINA_HOME/lib`), e inietta la nostra configurazione JNDI. È stata inoltre abilitata esplicitamente la lettura delle variabili d'ambiente (`EnvironmentPropertySource`) tramite modifica al `catalina.properties`.

## Unità 4: `docker-compose.yml` (Orchestrazione)
- **Commit atteso:** `feat: orchestrazione di applicazione e database con Docker Compose`
- **File toccati (SIS parziale):**
  - `docker-compose.yml` (N - Nuovo file)
- **Note / Razionale:** 
  Creato il file di orchestrazione per avviare l'intero stack applicativo (App Tomcat + Database MySQL 8). Il servizio `db` inizializza automaticamente lo schema `storage` tramite il mount di `db/storage.sql` ed espone un healthcheck per garantire che sia pronto. Il servizio `app` si avvia subordinatamente all'healthcheck del db, mappa le credenziali via variabili d'ambiente (che vengono poi iniettate nel context.xml di Tomcat) ed espone la porta `8080` verso l'host. Inserito un volume nominato per la persistenza dei dati MySQL.
