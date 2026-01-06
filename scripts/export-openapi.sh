#!/bin/bash
# Export OpenAPI Specification Script
# This script exports the OpenAPI spec from the running Spring Boot application

API_DOCS_URL="http://localhost:8080/api/api-docs"
OUTPUT_FILE="openapi.json"

echo "Exporting OpenAPI specification from backend..."

# Check if backend is running and export
if curl -s -o "$OUTPUT_FILE" "$API_DOCS_URL"; then
    echo "✓ Successfully exported OpenAPI spec to: $OUTPUT_FILE"
    echo ""
    echo "You can now import this file into Apidog:"
    echo "1. Open Apidog"
    echo "2. Click 'Import' → 'OpenAPI'"
    echo "3. Select the file: $OUTPUT_FILE"
    echo ""
else
    echo "✗ Error: Backend is not running or not accessible"
    echo "Please start the backend first with: mvn spring-boot:run"
    exit 1
fi
