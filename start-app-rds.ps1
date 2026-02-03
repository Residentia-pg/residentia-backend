# Start Residentia Application with AWS RDS
Write-Host "`n========================================" -ForegroundColor Green
Write-Host "  RESIDENTIA - AWS RDS MODE" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Green

# Set AWS RDS environment variables
$env:SPRING_DATASOURCE_URL="jdbc:mysql://database-1.c7mm0eumyni6.ca-central-1.rds.amazonaws.com:3306/residentia_db?useSSL=true&serverTimezone=UTC"
$env:SPRING_DATASOURCE_USERNAME="Residentia"
$env:SPRING_DATASOURCE_PASSWORD="Praju@123"

Write-Host "Database: AWS RDS (Canada)" -ForegroundColor Yellow
Write-Host "Starting application...`n" -ForegroundColor Yellow

# Start using jar file
java -jar target\residentia-backend-1.0.0.jar
