# AWS RDS Database Setup Script for Windows
# PowerShell version

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "AWS RDS Database Setup for Residentia" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Check if mysql client is available
$mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue

if (-not $mysqlPath) {
    Write-Host "❌ MySQL client is not installed!" -ForegroundColor Red
    Write-Host "Please install MySQL client first:" -ForegroundColor Yellow
    Write-Host "Download from: https://dev.mysql.com/downloads/mysql/" -ForegroundColor Yellow
    exit 1
}

# Get RDS connection details
Write-Host "Please enter your AWS RDS connection details:" -ForegroundColor Yellow
Write-Host ""

$RDS_ENDPOINT = Read-Host "RDS Endpoint (e.g., residentia-db.xxx.us-east-1.rds.amazonaws.com)"
$RDS_PORT = Read-Host "Port [3306]"
if ([string]::IsNullOrWhiteSpace($RDS_PORT)) { $RDS_PORT = "3306" }

$RDS_USER = Read-Host "Username [admin]"
if ([string]::IsNullOrWhiteSpace($RDS_USER)) { $RDS_USER = "admin" }

$RDS_PASSWORD = Read-Host "Password" -AsSecureString
$RDS_PASSWORD_PLAIN = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($RDS_PASSWORD))

$RDS_DATABASE = Read-Host "Database Name [residentia_db]"
if ([string]::IsNullOrWhiteSpace($RDS_DATABASE)) { $RDS_DATABASE = "residentia_db" }

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Testing Connection..." -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

# Test connection
$testQuery = "SELECT 'Connected!' AS Status;"
$result = mysql -h $RDS_ENDPOINT -P $RDS_PORT -u $RDS_USER "-p$RDS_PASSWORD_PLAIN" -e $testQuery 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Connection successful!" -ForegroundColor Green
    Write-Host ""
    
    # Ask if user wants to import schema
    $importSchema = Read-Host "Do you want to import the database schema? (y/n)"
    
    if ($importSchema -eq "y" -or $importSchema -eq "Y") {
        $schemaFile = "src\main\resources\schema.sql"
        
        if (Test-Path $schemaFile) {
            Write-Host ""
            Write-Host "============================================" -ForegroundColor Cyan
            Write-Host "Importing Schema..." -ForegroundColor Cyan
            Write-Host "============================================" -ForegroundColor Cyan
            
            Get-Content $schemaFile | mysql -h $RDS_ENDPOINT -P $RDS_PORT -u $RDS_USER "-p$RDS_PASSWORD_PLAIN"
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host "✅ Schema imported successfully!" -ForegroundColor Green
                
                # Verify tables
                Write-Host ""
                Write-Host "Verifying tables..." -ForegroundColor Yellow
                mysql -h $RDS_ENDPOINT -P $RDS_PORT -u $RDS_USER "-p$RDS_PASSWORD_PLAIN" -e "USE $RDS_DATABASE; SHOW TABLES;"
                
            } else {
                Write-Host "❌ Failed to import schema!" -ForegroundColor Red
            }
        } else {
            Write-Host "❌ Schema file not found: $schemaFile" -ForegroundColor Red
        }
    }
    
    # Generate environment variables
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host "Environment Variables" -ForegroundColor Cyan
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Copy and run these commands in PowerShell:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "`$env:SPRING_DATASOURCE_URL=`"jdbc:mysql://$RDS_ENDPOINT`:$RDS_PORT/$RDS_DATABASE`?useSSL=true&serverTimezone=UTC`"" -ForegroundColor White
    Write-Host "`$env:SPRING_DATASOURCE_USERNAME=`"$RDS_USER`"" -ForegroundColor White
    Write-Host "`$env:SPRING_DATASOURCE_PASSWORD=`"$RDS_PASSWORD_PLAIN`"" -ForegroundColor White
    Write-Host ""
    Write-Host "To make them permanent (system-wide):" -ForegroundColor Yellow
    Write-Host "1. Open System Properties → Environment Variables" -ForegroundColor White
    Write-Host "2. Add the above variables" -ForegroundColor White
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host "✅ Setup Complete!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Cyan
    
} else {
    Write-Host "❌ Connection failed!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Yellow
    Write-Host "1. Check your RDS endpoint is correct" -ForegroundColor White
    Write-Host "2. Verify username and password" -ForegroundColor White
    Write-Host "3. Ensure Security Group allows your IP (port 3306)" -ForegroundColor White
    Write-Host "4. Check Public Access is enabled in RDS settings" -ForegroundColor White
    Write-Host "5. Verify RDS instance is in 'Available' status" -ForegroundColor White
    exit 1
}
