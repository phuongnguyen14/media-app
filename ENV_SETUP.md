# Environment Setup Guide

## Environment Variables Reference

The `.env.example` file contains all available configuration options with example values.

## Configuration (Supabase Database)

You need to set these environment variables:

```env
DB_HOST=aws-1-ap-south-1.pooler.supabase.com
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=postgres.agbfwppjficzcwdjlacv
DB_PASSWORD=phuongnguyen1110
```

## Running the Application

### Option 1: Set environment variables directly (Windows)

```powershell
$env:DB_HOST="aws-1-ap-south-1.pooler.supabase.com"
$env:DB_PORT="5432"
$env:DB_NAME="postgres"
$env:DB_USERNAME="postgres.agbfwppjficzcwdjlacv"
$env:DB_PASSWORD="phuongnguyen1110"
mvn spring-boot:run
```

### Option 2: Set in IDE (IntelliJ IDEA / VS Code)

Add environment variables in your Run Configuration:
- IntelliJ: Run → Edit Configurations → Environment Variables
- VS Code: Add to `.vscode/launch.json` env section

### Option 3: Use .env file with external tool

If you prefer using a `.env` file, you can use tools like:
- `dotenv-cli`: `dotenv -e .env mvn spring-boot:run`
- IDE plugins that load .env files

## Important Notes

- **NEVER** commit the `.env` file to version control
- Use `.env.example` as a template for team members
- Keep production credentials secure and separate
