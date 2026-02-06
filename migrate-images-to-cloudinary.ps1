# Migrate Local Images to Cloudinary
# This script uploads existing local images to Cloudinary and updates the database

$ErrorActionPreference = "Stop"

# Configuration
$cloudName = "doux976kl"
$apiKey = "553679296573887"
$apiSecret = "xdyGM0V8Q6jBkx1I3ZNlZVoZRCc"
$uploadPreset = ""  # Not needed for authenticated uploads
$folder = "residentia/properties"
$localImageDir = "uploads\properties"

# Database configuration
$dbHost = "residentia-db.cp2s0wi2ykmu.ap-south-1.rds.amazonaws.com"
$dbUser = "admin"
$dbPass = "Residentia25"
$dbName = "residentia_db"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Cloudinary Migration Tool" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Get properties with local images from database
Write-Host "Fetching properties from database..." -ForegroundColor Yellow
$query = "SELECT id, property_name, image_url FROM pgs WHERE image_url LIKE '/api/files/images/%'"
$ErrorActionPreference = "Continue"
$rawOutput = mysql -h $dbHost -u $dbUser -p$dbPass $dbName -e $query --batch --skip-column-names 2>&1
$ErrorActionPreference = "Stop"

# Filter out warnings and error objects, keep only data rows
$properties = @()
foreach ($line in $rawOutput) {
    if ($line -is [string] -and $line -notmatch "Warning" -and $line -match '\t') {
        $properties += $line
    }
}

if ($properties.Count -eq 0) {
    Write-Host "✅ No local images to migrate - all done!" -ForegroundColor Green
    exit 0
}

Write-Host "Found $($properties.Count) properties with local images`n" -ForegroundColor Green

$success = 0
$failed = 0

foreach ($row in $properties) {
    $parts = $row -split "`t"
    if ($parts.Count -lt 3) { continue }
    
    $id = $parts[0]
    $propertyName = $parts[1]
    $imageUrl = $parts[2]
    
    # Extract filename from URL (/api/files/images/filename.ext -> filename.ext)
    $filename = $imageUrl -replace '^/api/files/images/', ''
    $localPath = Join-Path $localImageDir $filename
    
    Write-Host "🔄 Processing: $propertyName (ID: $id)" -ForegroundColor Cyan
    Write-Host "   Local file: $filename" -ForegroundColor Gray
    
    if (-not (Test-Path $localPath)) {
        Write-Host "   ❌ File not found: $localPath" -ForegroundColor Red
        $failed++
        continue
    }
    
    try {
        # Get file info
        $fileInfo = Get-Item $localPath
        $fileBytes = [System.IO.File]::ReadAllBytes($localPath)
        $base64 = [Convert]::ToBase64String($fileBytes)
        $extension = $fileInfo.Extension.TrimStart('.')
        
        # Determine mime type
        $mimeType = switch ($extension) {
            "jpg"  { "image/jpeg" }
            "jpeg" { "image/jpeg" }
            "png"  { "image/png" }
            "webp" { "image/webp" }
            "jfif" { "image/jpeg" }
            default { "image/jpeg" }
        }
        
        # Create multipart form data instead of data URI
        Write-Host "   ☁️  Uploading to Cloudinary..." -ForegroundColor Yellow
        
        $timestamp = [int][double]::Parse((Get-Date -UFormat %s))
        $publicId = "residentia/properties/" + [guid]::NewGuid().ToString()
        
        # Create signature (folder, public_id, timestamp)
        $signParams = @("folder=$folder", "public_id=$publicId", "timestamp=$timestamp") -join '&'
        $stringToSign = $signParams + $apiSecret
        $sha1 = [System.Security.Cryptography.SHA1]::Create()
        $signature = $sha1.ComputeHash([System.Text.Encoding]::UTF8.GetBytes($stringToSign))
        $signatureHex = [System.BitConverter]::ToString($signature).Replace("-", "").ToLower()
        
        $uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
        
        # Use file upload instead of data URI
        $boundary = [System.Guid]::NewGuid().ToString()
        $LF = "`r`n"
        
        $bodyLines = @(
            "--$boundary",
            "Content-Disposition: form-data; name=`"file`"; filename=`"$filename`"",
            "Content-Type: $mimeType",
            "",
            [System.Text.Encoding]::UTF8.GetString($fileBytes),
            "--$boundary",
            "Content-Disposition: form-data; name=`"api_key`"",
            "",
            $apiKey,
            "--$boundary",
            "Content-Disposition: form-data; name=`"timestamp`"",
            "",
            $timestamp,
            "--$boundary",
            "Content-Disposition: form-data; name=`"signature`"",
            "",
            $signatureHex,
            "--$boundary",
            "Content-Disposition: form-data; name=`"folder`"",
            "",
            $folder,
            "--$boundary",
            "Content-Disposition: form-data; name=`"public_id`"",
            "",
            $publicId,
            "--$boundary--"
        ) -join $LF
        
        $bodyBytes = [System.Text.Encoding]::UTF8.GetBytes($bodyLines)
        
        $response = Invoke-RestMethod -Uri $uploadUrl -Method Post -ContentType "multipart/form-data; boundary=$boundary" -Body $bodyBytes
        
        $cloudinaryUrl = $response.secure_url
        Write-Host "   ✅ Uploaded: $cloudinaryUrl" -ForegroundColor Green
        
        # Update database
        Write-Host "   💾 Updating database..." -ForegroundColor Yellow
        $escapedUrl = $cloudinaryUrl -replace "'", "''"
        $updateQuery = "UPDATE pgs SET image_url = '$escapedUrl' WHERE id = $id"
        mysql -h $dbHost -u $dbUser -p$dbPass $dbName -e $updateQuery 2>&1 | Out-Null
        
        Write-Host "   ✅ Database updated successfully`n" -ForegroundColor Green
        $success++
        
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
        $failed++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Migration Complete" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Success: $success" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor Red
Write-Host ""
