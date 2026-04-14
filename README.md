# TEMPORARY

run docker desktop postgresql
or thru

```powershell
docker compose up -d db
```

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

# Spring Boot Backend Template

This project is scaffolded with:

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Spring Scheduler
- JWT Authentication (Spring Security)
- REST API
- Dockerfile

## Prerequisites

- **JDK 21** — [Download Eclipse Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21) (recommended) or use a package manager
- **Maven 3.9+** — [Download from Apache](https://maven.apache.org/download.cgi)

> **Windows users**: If Maven is not on your PATH, prefix every `mvn` command with the full path, e.g.
> `C:\Users\<you>\.maven\maven-3.9.14\bin\mvn`

## Local Development Database (Docker)

This project uses a local PostgreSQL database for development, managed by Docker Compose.

**Start the database:**

```bash
docker compose up -d db
```

**Database connection (dev profile):**

- Host: `localhost`
- Port: `5433`
- Database: `widgetdb`
- User: `widgetuser`
- Password: `widgetpass`

**Environment variables:**

All secrets and credentials are loaded from environment variables. For local development, simply edit the `.env` file in the project root. The dev profile automatically imports `.env`—you do NOT need to export variables manually.

**.env file example:**

```
DB_URL=jdbc:postgresql://localhost:5433/widgetdb
DB_USERNAME=widgetuser
DB_PASSWORD=widgetpass
JWT_SECRET=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=
JWT_EXPIRATION_MS=86400000
ADMIN_EMAIL=fjasdojf@hotmail.com
ADMIN_PASSWORD=password
```

> **Important:**
>
> - `JWT_SECRET` **must be a BASE64 string that decodes to at least 32 bytes** (for HS256). Example: `MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=`
> - Do NOT wrap values in quotes in `.env`.
> - Never commit real secrets to version control.

**Run the backend:**

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**Linux / macOS:**

The `.env` file is also supported on Linux/macOS. You do NOT need to export variables manually. Just run:

```bash
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

## Production Database (Render)

For production, the backend uses a managed PostgreSQL database on Render. The connection is configured in `application-prod.yml` using environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET` (must match the format above)

Set these variables in your Render dashboard with the credentials provided by Render.

**Example JDBC URL:**

```
jdbc:postgresql://dpg-d7ef7bfaqgkc73fvtet0-a.oregon-postgres.render.com/widget_db_8iwa
```

**Example environment variables:**

```
DB_URL=jdbc:postgresql://dpg-d7ef7bfaqgkc73fvtet0-a.oregon-postgres.render.com/widget_db_8iwa
DB_USERNAME=fjasdojf
DB_PASSWORD=your_render_password
JWT_SECRET=your_base64_32byte_secret
```

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

This project uses two Spring profiles: `dev` (local) and `prod` (production).

### dev (local development)

Runs against a local PostgreSQL container via Docker Compose.
Config lives in `application-dev.yml`. All credentials are loaded from `.env` (never commit real secrets).

Start the local database:

```powershell
docker compose up -d
```

Then run the app with the dev profile:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The H2 console is disabled in dev/prod. Use a PostgreSQL client (e.g. DBeaver, TablePlus) pointed at `localhost:5433`.

### prod (production)

Config lives in `application-prod.yml`. All secrets are read from environment variables — **never commit real credentials**.

Required environment variables:

| Variable      | Description                                                            |
| ------------- | ---------------------------------------------------------------------- |
| `DB_URL`      | Render JDBC URL, e.g. `jdbc:postgresql://...`                          |
| `DB_USERNAME` | Render database username                                               |
| `DB_PASSWORD` | Render database password                                               |
| `JWT_SECRET`  | Base64 secret that decodes to at least 32 bytes for signing JWT tokens |

See `.env` for a template.

---

## Branching & deploy strategy

| Branch      | Purpose                                          |
| ----------- | ------------------------------------------------ |
| `main`      | Production — every push triggers a deploy        |
| `feature/*` | Day-to-day work — open a PR to merge into `main` |

CI/CD is handled by GitHub Actions (`.github/workflows/deploy.yml`):

1. Build JAR with Maven
2. Build Docker image (`eclipse-temurin:21-jre`)
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
