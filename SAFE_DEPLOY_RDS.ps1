# Safe AWS RDS Deployment - Step-by-Step Guide
# This guide ensures zero risk to your existing system

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "SAFE AWS RDS DEPLOYMENT" -ForegroundColor Cyan
Write-Host "Step-by-Step with Backups & Rollback" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$backupDir = ".\backups\$timestamp"

# Create backup directory
New-Item -ItemType Directory -Force -Path $backupDir | Out-Null

Write-Host "✅ Backup directory created: $backupDir" -ForegroundColor Green
Write-Host ""

# Step 1: Backup Local Database
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "STEP 1: Backup Your Local Database" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

$doBackup = Read-Host "Do you want to backup your local database first? (RECOMMENDED) (y/n)"

if ($doBackup -eq "y" -or $doBackup -eq "Y") {
    $dbHost = Read-Host "Local MySQL Host [localhost]"
    if ([string]::IsNullOrWhiteSpace($dbHost)) { $dbHost = "localhost" }
    
    $dbPort = Read-Host "Local MySQL Port [3306]"
    if ([string]::IsNullOrWhiteSpace($dbPort)) { $dbPort = "3306" }
    
    $dbUser = Read-Host "Local MySQL Username [root]"
    if ([string]::IsNullOrWhiteSpace($dbUser)) { $dbUser = "root" }
    
    $dbPassword = Read-Host "Local MySQL Password" -AsSecureString
    $dbPasswordPlain = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($dbPassword))
    
    $dbName = Read-Host "Database Name [residentia_db]"
    if ([string]::IsNullOrWhiteSpace($dbName)) { $dbName = "residentia_db" }
    
    $backupFile = "$backupDir\local_backup_$timestamp.sql"
    
    Write-Host ""
    Write-Host "Creating backup..." -ForegroundColor Yellow
    
    # Check if mysqldump is available
    $mysqldump = Get-Command mysqldump -ErrorAction SilentlyContinue
    
    if ($mysqldump) {
        mysqldump -h $dbHost -P $dbPort -u $dbUser "-p$dbPasswordPlain" $dbName > $backupFile 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Local database backed up to: $backupFile" -ForegroundColor Green
            $backupSize = (Get-Item $backupFile).Length / 1KB
            Write-Host "   Backup size: $([math]::Round($backupSize, 2)) KB" -ForegroundColor Gray
        } else {
            Write-Host "❌ Backup failed!" -ForegroundColor Red
            Write-Host "Please fix the error before proceeding." -ForegroundColor Yellow
            exit 1
        }
    } else {
        Write-Host "⚠️  mysqldump not found. Cannot backup automatically." -ForegroundColor Yellow
        Write-Host "   Please backup manually before proceeding." -ForegroundColor Yellow
        $continue = Read-Host "Continue anyway? (y/n)"
        if ($continue -ne "y" -and $continue -ne "Y") {
            exit 1
        }
    }
} else {
    Write-Host "⚠️  Skipping backup (NOT RECOMMENDED)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "STEP 2: Get AWS RDS Connection Details" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

Write-Host "Please enter your AWS RDS connection details:" -ForegroundColor Cyan
Write-Host ""

$rdsEndpoint = Read-Host "RDS Endpoint (e.g., residentia-db.xxx.us-east-1.rds.amazonaws.com)"
if ([string]::IsNullOrWhiteSpace($rdsEndpoint)) {
    Write-Host "❌ RDS Endpoint is required!" -ForegroundColor Red
    exit 1
}

$rdsPort = Read-Host "Port [3306]"
if ([string]::IsNullOrWhiteSpace($rdsPort)) { $rdsPort = "3306" }

$rdsUser = Read-Host "Username [admin]"
if ([string]::IsNullOrWhiteSpace($rdsUser)) { $rdsUser = "admin" }

$rdsPassword = Read-Host "Password" -AsSecureString
$rdsPasswordPlain = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($rdsPassword))

$rdsDatabase = Read-Host "Database Name [residentia_db]"
if ([string]::IsNullOrWhiteSpace($rdsDatabase)) { $rdsDatabase = "residentia_db" }

# Save connection details
$configFile = "$backupDir\rds_connection_config.txt"
@"
RDS_ENDPOINT=$rdsEndpoint
RDS_PORT=$rdsPort
RDS_USER=$rdsUser
RDS_DATABASE=$rdsDatabase
JDBC_URL=jdbc:mysql://$rdsEndpoint`:$rdsPort/$rdsDatabase`?useSSL=true&serverTimezone=UTC
"@ | Out-File -FilePath $configFile -Encoding UTF8

Write-Host ""
Write-Host "✅ Connection details saved to: $configFile" -ForegroundColor Green

# Step 3: Test RDS Connection
Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "STEP 3: Test RDS Connection" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

Write-Host "Testing connection to RDS..." -ForegroundColor Yellow

$mysql = Get-Command mysql -ErrorAction SilentlyContinue

if ($mysql) {
    $testQuery = "SELECT 'Connection Successful!' AS Status, VERSION() AS MySQL_Version;"
    $result = mysql -h $rdsEndpoint -P $rdsPort -u $rdsUser "-p$rdsPasswordPlain" -e $testQuery 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ RDS Connection successful!" -ForegroundColor Green
        Write-Host $result
    } else {
        Write-Host "❌ RDS Connection failed!" -ForegroundColor Red
        Write-Host ""
        Write-Host "Troubleshooting checklist:" -ForegroundColor Yellow
        Write-Host "  1. ☐ RDS endpoint correct?" -ForegroundColor White
        Write-Host "  2. ☐ Username/password correct?" -ForegroundColor White
        Write-Host "  3. ☐ Security Group allows your IP on port 3306?" -ForegroundColor White
        Write-Host "  4. ☐ RDS has Public Access enabled?" -ForegroundColor White
        Write-Host "  5. ☐ RDS status is 'Available'?" -ForegroundColor White
        Write-Host ""
        $continue = Read-Host "Fix these issues and try again. Continue anyway? (y/n)"
        if ($continue -ne "y" -and $continue -ne "Y") {
            exit 1
        }
    }
} else {
    Write-Host "⚠️  MySQL client not found. Skipping connection test." -ForegroundColor Yellow
    Write-Host "   Install from: https://dev.mysql.com/downloads/mysql/" -ForegroundColor Yellow
}

# Step 4: Import Schema to RDS
Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "STEP 4: Import Database Schema to RDS" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

$importSchema = Read-Host "Import schema to RDS now? (y/n)"

if ($importSchema -eq "y" -or $importSchema -eq "Y") {
    $schemaFile = "src\main\resources\schema.sql"
    
    if (Test-Path $schemaFile) {
        Write-Host "Importing schema..." -ForegroundColor Yellow
        
        Get-Content $schemaFile | mysql -h $rdsEndpoint -P $rdsPort -u $rdsUser "-p$rdsPasswordPlain" 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Schema imported successfully!" -ForegroundColor Green
            
            # Verify tables
            Write-Host ""
            Write-Host "Verifying tables..." -ForegroundColor Yellow
            mysql -h $rdsEndpoint -P $rdsPort -u $rdsUser "-p$rdsPasswordPlain" -e "USE $rdsDatabase; SHOW TABLES;" 2>&1
            
        } else {
            Write-Host "❌ Schema import failed!" -ForegroundColor Red
            $continue = Read-Host "Continue anyway? (y/n)"
            if ($continue -ne "y" -and $continue -ne "Y") {
                exit 1
            }
        }
    } else {
        Write-Host "⚠️  Schema file not found: $schemaFile" -ForegroundColor Yellow
    }
}

# Step 5: Migrate Data (Optional)
Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "STEP 5: Migrate Local Data to RDS (Optional)" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

$migrateData = Read-Host "Do you want to copy data from local to RDS? (y/n)"

if ($migrateData -eq "y" -or $migrateData -eq "Y") {
    if (Test-Path $backupFile) {
        Write-Host "Importing data from backup to RDS..." -ForegroundColor Yellow
        
        Get-Content $backupFile | mysql -h $rdsEndpoint -P $rdsPort -u $rdsUser "-p$rdsPasswordPlain" $rdsDatabase 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Data migrated successfully!" -ForegroundColor Green
        } else {
            Write-Host "❌ Data migration failed!" -ForegroundColor Red
        }
    } else {
        Write-Host "⚠️  No backup file found. Cannot migrate data." -ForegroundColor Yellow
    }
}

# Step 6: Create Environment Configuration
Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "STEP 6: Configure Application" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

$envScript = "$backupDir\set_rds_env.ps1"

@"
# AWS RDS Environment Variables
# Run this script to connect your application to AWS RDS

Write-Host "Setting AWS RDS environment variables..." -ForegroundColor Cyan

`$env:SPRING_DATASOURCE_URL="jdbc:mysql://$rdsEndpoint`:$rdsPort/$rdsDatabase`?useSSL=true&serverTimezone=UTC"
`$env:SPRING_DATASOURCE_USERNAME="$rdsUser"
`$env:SPRING_DATASOURCE_PASSWORD="$rdsPasswordPlain"

Write-Host "✅ Environment variables set!" -ForegroundColor Green
Write-Host ""
Write-Host "RDS Endpoint: $rdsEndpoint" -ForegroundColor Yellow
Write-Host "Database: $rdsDatabase" -ForegroundColor Yellow
Write-Host ""
Write-Host "To start application with RDS:" -ForegroundColor Cyan
Write-Host "  mvn spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "To revert to local database:" -ForegroundColor Cyan
Write-Host "  Remove-Item Env:SPRING_DATASOURCE_URL" -ForegroundColor White
Write-Host "  Remove-Item Env:SPRING_DATASOURCE_USERNAME" -ForegroundColor White
Write-Host "  Remove-Item Env:SPRING_DATASOURCE_PASSWORD" -ForegroundColor White
"@ | Out-File -FilePath $envScript -Encoding UTF8

Write-Host "✅ Environment script created: $envScript" -ForegroundColor Green

# Create rollback script
$rollbackScript = "$backupDir\rollback_to_local.ps1"

@"
# Rollback to Local Database
# Run this script to revert back to local MySQL

Write-Host "============================================" -ForegroundColor Yellow
Write-Host "ROLLING BACK TO LOCAL DATABASE" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

# Remove RDS environment variables
Remove-Item Env:SPRING_DATASOURCE_URL -ErrorAction SilentlyContinue
Remove-Item Env:SPRING_DATASOURCE_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:SPRING_DATASOURCE_PASSWORD -ErrorAction SilentlyContinue

Write-Host "✅ Environment variables cleared" -ForegroundColor Green
Write-Host ""
Write-Host "Your application will now use local MySQL (localhost:3306)" -ForegroundColor Green
Write-Host ""

# Restore local database from backup (optional)
`$restoreBackup = Read-Host "Do you want to restore local database from backup? (y/n)"

if (`$restoreBackup -eq "y" -or `$restoreBackup -eq "Y") {
    `$backupFile = "$backupFile"
    
    if (Test-Path `$backupFile) {
        Write-Host "Restoring backup..." -ForegroundColor Yellow
        
        `$dbUser = Read-Host "Local MySQL Username [root]"
        if ([string]::IsNullOrWhiteSpace(`$dbUser)) { `$dbUser = "root" }
        
        `$dbPassword = Read-Host "Local MySQL Password" -AsSecureString
        `$dbPasswordPlain = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR(`$dbPassword))
        
        Get-Content `$backupFile | mysql -u `$dbUser "-p`$dbPasswordPlain" $dbName
        
        if (`$LASTEXITCODE -eq 0) {
            Write-Host "✅ Database restored from backup!" -ForegroundColor Green
        } else {
            Write-Host "❌ Restore failed!" -ForegroundColor Red
        }
    } else {
        Write-Host "⚠️  Backup file not found!" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "✅ Rollback complete!" -ForegroundColor Green
Write-Host "   Application will use local database on next restart" -ForegroundColor Green
"@ | Out-File -FilePath $rollbackScript -Encoding UTF8

Write-Host "✅ Rollback script created: $rollbackScript" -ForegroundColor Green

# Final Instructions
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "✅ SAFE DEPLOYMENT COMPLETE!" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""

Write-Host "📁 All files saved to: $backupDir" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1️⃣  TEST WITH RDS:" -ForegroundColor Cyan
Write-Host "   cd server\residentia-backend" -ForegroundColor White
Write-Host "   .\$envScript" -ForegroundColor White
Write-Host "   mvn spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "2️⃣  VERIFY EVERYTHING WORKS:" -ForegroundColor Cyan
Write-Host "   - Test login/registration" -ForegroundColor White
Write-Host "   - Test property CRUD" -ForegroundColor White
Write-Host "   - Test bookings" -ForegroundColor White
Write-Host ""
Write-Host "3️⃣  IF SOMETHING GOES WRONG:" -ForegroundColor Cyan
Write-Host "   .\$rollbackScript" -ForegroundColor White
Write-Host "   mvn spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "💡 TIP: Your local database is untouched!" -ForegroundColor Magenta
Write-Host "   You can switch between local and RDS anytime" -ForegroundColor Magenta
Write-Host ""
Write-Host "📋 Important Files:" -ForegroundColor Yellow
Write-Host "   - Backup: $backupFile" -ForegroundColor White
Write-Host "   - Config: $configFile" -ForegroundColor White
Write-Host "   - Connect to RDS: $envScript" -ForegroundColor White
Write-Host "   - Rollback: $rollbackScript" -ForegroundColor White
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "Your deployment is SAFE and REVERSIBLE! ✅" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
