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
