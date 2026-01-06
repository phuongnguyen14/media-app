# Export OpenAPI Specification Script
# This script exports the OpenAPI spec from the running Spring Boot application

$apiDocsUrl = "http://localhost:8080/api/api-docs"
$outputFile = "openapi.json"

Write-Host "Exporting OpenAPI specification from backend..." -ForegroundColor Cyan

try {
    # Check if backend is running
    $response = Invoke-WebRequest -Uri $apiDocsUrl -Method Get -ErrorAction Stop
    
    # Save to file
    $response.Content | Out-File -FilePath $outputFile -Encoding UTF8
    
    Write-Host "Successfully exported OpenAPI spec to: $outputFile" -ForegroundColor Green
    Write-Host ""
    Write-Host "You can now import this file into Apidog:" -ForegroundColor Yellow
    Write-Host "1. Open Apidog" -ForegroundColor Yellow
    Write-Host "2. Click Import > OpenAPI" -ForegroundColor Yellow
    Write-Host "3. Select the file: $outputFile" -ForegroundColor Yellow
    Write-Host ""
    
} catch {
    Write-Host "Error: Backend is not running or not accessible" -ForegroundColor Red
    Write-Host "Please start the backend first with: mvn spring-boot:run" -ForegroundColor Yellow
    exit 1
}
