# SIW Calcio

SIW Calcio e' una web application per la gestione e consultazione di un portale calcistico. Il progetto permette di vedere squadre, giocatori, tornei e partite, con una sezione amministrativa per inserire e modificare i dati del dominio.

L'applicazione e' sviluppata in Java con Spring Boot, usa PostgreSQL come database relazionale, Thymeleaf per le viste server-side e Docker per l'esecuzione containerizzata. In produzione e' pensata per essere deployata su Azure App Service, con Azure Database for PostgreSQL e Azure Blob Storage per le immagini.

## Funzionalita principali

- Home page con partite e contenuti principali del portale.
- Lista e dettaglio di squadre, con rosa giocatori e logo.
- Lista e dettaglio di tornei, con squadre partecipanti e classifiche/punteggi.
- Lista e dettaglio delle partite, con stato, risultato, squadre, torneo e arbitro.
- Registrazione e login utenti.
- Commenti sulle partite per utenti autenticati.
- Area admin per creare, modificare ed eliminare squadre, giocatori, tornei, partite e arbitri.
- Upload immagini per loghi squadre e foto giocatori.

## Tecnologie

| Area | Tecnologia |
| --- | --- |
| Linguaggio | Java 17 |
| Framework | Spring Boot 4 |
| Web MVC | Spring Web MVC |
| Template engine | Thymeleaf |
| Persistenza | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| Sicurezza | Spring Security, BCrypt |
| Validazione | Spring Validation |
| Storage immagini | File system locale in sviluppo, Azure Blob Storage in produzione |
| Container | Docker, Docker Compose |
| Cloud | Azure App Service, Azure Database for PostgreSQL, Azure Blob Storage |
| Build | Maven Wrapper |

## Come funziona il progetto

Il progetto segue una struttura tipica Spring MVC:

```text
src/main/java/it/uniroma3/siw/calcio
|-- authentication   # Configurazione Spring Security
|-- config           # Configurazioni app, storage locale, Azure Blob, dati demo
|-- controller       # Controller MVC e routing HTTP
|-- exception        # Gestione errori applicativi
|-- model            # Entita JPA del dominio
|-- repository       # Repository Spring Data JPA
`-- service          # Logica applicativa
```

Le pagine HTML sono in `src/main/resources/templates`, mentre CSS, font e immagini statiche sono in `src/main/resources/static`.

Il dominio principale e' composto da:

- `Team`: squadra, citta, anno di fondazione, logo e rosa.
- `Player`: giocatore, ruolo, numero, altezza, data di nascita, foto e squadra.
- `Tournament`: torneo con anno, descrizione e partecipazioni.
- `Partecipation`: associazione tra squadra e torneo, con punteggio.
- `Match`: partita tra due squadre, torneo, arbitro, risultato, stato e sede.
- `Referee`: arbitro.
- `Comment`: commento utente su una partita.
- `User`: utente applicativo con ruolo `ROLE_USER` o `ROLE_ADMIN`.

## Profili e storage immagini

L'app usa due strategie di storage per le immagini:

- Profilo non `prod`: `LocalImageStorageService` salva i file nella cartella `uploads/` e li espone tramite `/uploads/**`.
- Profilo `prod`: `AzureBlobImageStorageService` carica le immagini su Azure Blob Storage e salva nel database l'URL pubblico del blob.

Sono accettate immagini `.jpg`, `.jpeg`, `.png` e `.webp`, con limite configurato a `5MB`.

In produzione il container Azure Blob configurato viene creato automaticamente se non esiste, ma il livello di accesso pubblico va impostato su Azure. Per l'uso attuale del progetto il container deve consentire la lettura pubblica dei singoli blob, perche' i template Thymeleaf renderizzano direttamente gli URL delle immagini nei tag `<img>`.

## Prerequisiti

- Java 17
- Docker e Docker Compose
- PostgreSQL, se si avvia l'app senza Docker
- Un account Azure, per il deploy cloud

## Configurazione ambiente

Per Docker Compose parti dal file di esempio:

```bash
cp .env.example .env
```

Poi valorizza almeno:

```text
POSTGRES_DB=calcio
POSTGRES_USER=calcio
POSTGRES_PASSWORD=<password-db-locale>
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=<password-admin>
AZURE_STORAGE_CONNECTION_STRING=<connection-string-azure-storage>
AZURE_STORAGE_CONTAINER_NAME=images
SPRING_PROFILES_ACTIVE=prod
```

Il file `.env` contiene segreti locali e non deve essere committato.

## Avvio con Docker Compose

Il `docker-compose.yml` avvia:

- un container PostgreSQL 16;
- il container dell'app Spring Boot;
- un volume persistente per i dati PostgreSQL.

Avvio:

```bash
docker compose up --build
```

L'app sara' disponibile su:

```text
http://localhost:8080
```

PostgreSQL viene esposto sulla porta locale `5433`.

Nota: il compose usa `SPRING_PROFILES_ACTIVE=prod`, quindi richiede una connection string Azure Blob valida. Se vuoi sviluppare senza Azure Blob, avvia l'app con il profilo default e usa lo storage locale in `uploads/`.

## Avvio locale senza Docker

Assicurati di avere PostgreSQL attivo e un database raggiungibile. La configurazione default usa:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/calcio
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

Puoi sovrascrivere queste variabili dal terminale o dall'IDE.

Avvio:

```bash
./mvnw spring-boot:run
```

Su Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Con il profilo default, gli upload vengono salvati nella cartella locale `uploads/`.

## Dati demo e utente admin

L'app puo' creare o aggiornare un utente admin all'avvio se `APP_ADMIN_PASSWORD` e' valorizzata.

Per popolare dati dimostrativi:

```text
APP_SEED_DEMO_DATA=true
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=<password-admin>
```

Il seeding demo viene eseguito solo se le tabelle principali del dominio sono vuote.

In sviluppo con `ddl-auto=create`, Hibernate puo' usare anche `src/main/resources/import.sql` per caricare dati iniziali.

## Test e build

Eseguire i test:

```bash
./mvnw test
```

Su Windows:

```powershell
.\mvnw.cmd test
```

Creare il jar:

```bash
./mvnw clean package
```

Il Dockerfile usa una build multi-stage:

1. immagine `eclipse-temurin:17-jdk` per compilare il progetto;
2. immagine `eclipse-temurin:17-jre` per eseguire il jar finale.

## Deploy su Azure

La configurazione di produzione e' in `src/main/resources/application-prod.properties`.

Il deploy previsto usa:

- Azure App Service per eseguire l'app Java/Spring Boot o il container Docker.
- Azure Database for PostgreSQL come database applicativo.
- Azure Blob Storage per loghi squadre e foto giocatori.

Nelle Environment variables della Web App Azure imposta:

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=<jdbc-url-azure-postgres>
SPRING_DATASOURCE_USERNAME=<utente-postgres>
SPRING_DATASOURCE_PASSWORD=<password-postgres>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
AZURE_STORAGE_CONNECTION_STRING=<connection-string-storage-account>
AZURE_STORAGE_CONTAINER_NAME=images
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=<password-admin>
APP_SEED_DEMO_DATA=false
```

Azure App Service espone la porta tramite `WEBSITES_PORT`; l'app supporta anche `PORT` come fallback.

## Azure Blob Storage

Per lo storage immagini crea uno Storage Account e un container, ad esempio `images`.

Impostazioni consigliate:

- Performance: `Standard`
- Redundancy: `LRS` per costi ridotti, oppure `ZRS` per maggiore resilienza regionale
- Public access: abilitato sullo Storage Account
- Container access level: `Blob`

L'app usa la connection string tramite `AZURE_STORAGE_CONNECTION_STRING`. Non salvare mai questa stringa nel repository.

Guida piu' dettagliata: [docs/azure-blob-storage.md](docs/azure-blob-storage.md).

## Sicurezza

Spring Security gestisce autenticazione e autorizzazione:

- pagine pubbliche: home, login, registrazione, squadre, tornei, partite e asset statici;
- commenti: disponibili agli utenti autenticati;
- area `/admin/**`: accessibile solo a utenti con ruolo `ROLE_ADMIN`;
- password salvate con BCrypt.

## Note operative

- Gli upload locali sono ignorati da Git tramite `uploads/`.
- I segreti devono stare in `.env` locale o nelle Environment variables di Azure.
- In produzione evita `ddl-auto=create`; usa `update` solo per deploy semplici o una strategia di migrazione dedicata per ambienti piu' strutturati.
