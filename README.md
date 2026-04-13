# TEMPORARY

```powershell
Set-Location "C:\Users\$env:USERNAME\Documents\dev\chaturbate-online-model-widget\chaturbate-online-model-widget-backend-java"
```

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

# Spring Boot Backend Template

This project is scaffolded with:

- Java 24
- Spring Boot 3.5
- Spring Data JPA
- Spring Scheduler
- JWT Authentication (Spring Security)
- REST API
- Dockerfile

## Prerequisites

- **JDK 24+** — [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or use a package manager
- **Maven 3.9+** — [Download from Apache](https://maven.apache.org/download.cgi)

> **Windows users**: If Maven is not on your PATH, prefix every `mvn` command with the full path, e.g.
> `C:\Users\<you>\.maven\maven-3.9.14\bin\mvn`

## Run locally

**Linux / macOS**

```bash
export JAVA_HOME=/path/to/jdk-24
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**Windows (PowerShell)** — run each command one at a time

Navigate to the project folder:

```powershell
Set-Location "C:\Users\$env:USERNAME\Documents\dev\chaturbate-online-model-widget\chaturbate-online-model-widget-backend-java"
```

Set JAVA_HOME (adjust path to match your JDK installation):

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"
```

Add Maven to PATH (adjust path to match your Maven installation):

```powershell
$env:PATH = "C:\Users\$env:USERNAME\.maven\maven-3.9.14\bin;$env:PATH"
```

Start the application (note: the `-D` flag must be quoted in PowerShell):

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The server starts on **http://localhost:8080**.
Verify it is running: `GET http://localhost:8080/health` → `{"status":"live"}`

> If you see _"address already in use"_, another process is on port 8080.
> On Windows, find and stop it with:
>
> ```powershell
> $pid = (Get-NetTCPConnection -LocalPort 8080 -State Listen).OwningProcess
> Stop-Process -Id $pid -Force
> ```

## Build

```bash
mvn clean package
```

## API quick start

### 1) Register

`POST /api/auth/register`

```json
{
  "username": "demo",
  "password": "demo123"
}
```

### 2) Login

`POST /api/auth/login`

```json
{
  "username": "demo",
  "password": "demo123"
}
```

### 3) Access protected endpoint

Use token in header:

`Authorization: Bearer <token>`

Then call `GET /api/profile/me`.

## Public endpoints

`GET /health` — returns `{"status":"live"}` when the backend is running

`GET /api/public/ping`

## Environments

This project uses two Spring profiles: `dev` (local) and `prod` (Supabase).

### dev (local development)

Runs against a local PostgreSQL container via Docker Compose.
Config lives in `application-dev.yml`. Credentials are safe to commit here (no real data).

Start the local database:

```powershell
docker compose up -d
```

Then run the app with the dev profile:

```powershell
mvn spring-boot:run `-Dspring-boot.run.profiles=dev`
```

The H2 console is disabled in dev/prod. Use a PostgreSQL client (e.g. DBeaver, TablePlus) pointed at `localhost:5432`.

### prod (Supabase / production)

Config lives in `application-prod.yml`. All secrets are read from environment variables — **never commit real credentials**.

Required environment variables:

| Variable      | Description                                                                    |
| ------------- | ------------------------------------------------------------------------------ |
| `DB_URL`      | Supabase JDBC URL, e.g. `jdbc:postgresql://db.<ref>.supabase.co:5432/postgres` |
| `DB_USERNAME` | Supabase database username                                                     |
| `DB_PASSWORD` | Supabase database password                                                     |
| `JWT_SECRET`  | 64-char hex secret for signing JWT tokens                                      |

See `.env.example` for a template.

---

## Branching & deploy strategy

| Branch      | Purpose                                          |
| ----------- | ------------------------------------------------ |
| `main`      | Production — every push triggers a deploy        |
| `feature/*` | Day-to-day work — open a PR to merge into `main` |

CI/CD is handled by GitHub Actions (`.github/workflows/deploy.yml`):

1. Build JAR with Maven
2. Build Docker image (`eclipse-temurin:25-jre`)
3. Push image to container registry
4. Deploy to hosting platform (Railway / Render / etc.)

Set the secrets listed above as repository secrets in GitHub (`Settings → Secrets and variables → Actions`) so the pipeline can inject them at deploy time.

---

## H2 Console

> H2 is only used as a fallback if no profile is active. It is disabled in both `dev` and `prod`.

- URL: `/h2-console`
- JDBC URL: `jdbc:h2:mem:widgetdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- Username: `sa`
- Password: (empty)

## Docker (optional)

```bash
mvn clean package
docker build -t widget-backend:latest .
docker run -p 8080:8080 widget-backend:latest
```
