# Configuration Migration Summary

## Changes Made

### 1. Environment File (.env)
- ✅ Created `.env` file in `backend/` directory
- ✅ Created `.env.example` as a template
- ✅ Configured with Supabase database credentials:
  - Host: `aws-1-ap-south-1.pooler.supabase.com`
  - Database: `postgres`
  - User: `postgres.agbfwppjficzcwdjlacv`

### 2. Unified Configuration
- ✅ Converted to single `application.yml` file
- ✅ Removed `application-dev.yml` and `application-prod.yml`
- ✅ All configuration now uses environment variables with sensible defaults

### 3. Documentation
- ✅ Created `ENV_SETUP.md` with setup instructions
- ✅ Created `.env.example` as reference template

## How It Works

1. **Spring Boot** loads `application.yml` 
2. Application reads environment variables from your system/IDE
3. If a variable is not set, the default value in `application.yml` is used

## Running the Application

### Set environment variables in your IDE or terminal

**Windows PowerShell:**
```powershell
$env:DB_HOST="aws-1-ap-south-1.pooler.supabase.com"
$env:DB_USERNAME="postgres.agbfwppjficzcwdjlacv"
$env:DB_PASSWORD="phuongnguyen1110"
mvn spring-boot:run
```

**Or configure in your IDE's Run Configuration** (recommended)

## Configuration Variables

All configurable options are now in `.env.example`. Key variables:

- **Database**: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
- **Elasticsearch**: `ES_URIS`, `ES_USERNAME`, `ES_PASSWORD`
- **Security**: `JWT_SECRET`
- **File Upload**: `UPLOAD_PATH`
- **Logging**: `LOG_LEVEL_APP`, `LOG_LEVEL_ROOT`, etc.
- **Features**: `SYNC_ENABLED`, `SEARCH_MODE`, etc.

## Security

- The `.env` file is already in `.gitignore`
- Never commit sensitive credentials to version control
- Use different `.env` files for different environments (dev, staging, production)
