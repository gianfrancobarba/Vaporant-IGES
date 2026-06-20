# CR_05 — AIS Tracking (Containerizzazione Docker)

*Documento vivo di tracciamento progressivo degli interventi per la manutenzione adattiva (CR_05).*

## Unità 1: `pom.xml` (Build Maven)
- **Commit atteso:** `build: introduzione della build Maven per il pacchetto applicativo`
- **File toccati (SIS parziale):**
  - `pom.xml` (N - Nuovo file)
- **Note / Razionale:** 
  Introdotto il descrittore Maven in ottica *additiva* per permettere la compilazione della WAR all'interno del container. Il file rispetta il layout proprietario pre-esistente (`<sourceDirectory>src</sourceDirectory>`, `<warSourceDirectory>WebContent</warSourceDirectory>`). I jar manuali forniti in `WEB-INF/lib` sono stati esclusi dal packaging per evitare conflitti e duplicazioni, delegando la risoluzione a Maven. Le dipendenze Servlet e MySQL sono marcate come `provided` (quest'ultima verrà copiata manualmente nella cartella `lib/` di Tomcat per servire il DataSource JNDI).
