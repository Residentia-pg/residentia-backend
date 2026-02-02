# Test Cloudinary Configuration
# This script helps verify your Cloudinary setup

Write-Host "🧪 Cloudinary Configuration Test" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host ""

# Check if environment variables are set
Write-Host "📋 Checking environment variables..." -ForegroundColor Yellow
Write-Host ""

$cloudName = $env:CLOUDINARY_CLOUD_NAME
$apiKey = $env:CLOUDINARY_API_KEY
$apiSecret = $env:CLOUDINARY_API_SECRET

if (-not $cloudName -or $cloudName -eq "your-cloud-name") {
    Write-Host "❌ CLOUDINARY_CLOUD_NAME is not set or is still placeholder" -ForegroundColor Red
    $hasError = $true
} else {
    Write-Host "✅ CLOUDINARY_CLOUD_NAME = $cloudName" -ForegroundColor Green
}

if (-not $apiKey -or $apiKey -eq "your-api-key") {
    Write-Host "❌ CLOUDINARY_API_KEY is not set or is still placeholder" -ForegroundColor Red
    $hasError = $true
} else {
    Write-Host "✅ CLOUDINARY_API_KEY = $apiKey" -ForegroundColor Green
}

if (-not $apiSecret -or $apiSecret -eq "your-api-secret") {
    Write-Host "❌ CLOUDINARY_API_SECRET is not set or is still placeholder" -ForegroundColor Red
    $hasError = $true
} else {
    Write-Host "✅ CLOUDINARY_API_SECRET = ********" -ForegroundColor Green
}

Write-Host ""

if ($hasError) {
    Write-Host "⚠️  Configuration Issues Found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "📝 To fix this:" -ForegroundColor Yellow
    Write-Host "   1. Edit setup-cloudinary.ps1 with your actual credentials" -ForegroundColor Yellow
    Write-Host "   2. Run: .\setup-cloudinary.ps1" -ForegroundColor Yellow
    Write-Host "   3. Run this test again" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "🔗 Get credentials from: https://cloudinary.com/console" -ForegroundColor Cyan
} else {
    Write-Host "🎉 All environment variables are configured!" -ForegroundColor Green
    Write-Host ""
    Write-Host "✅ Ready to build and run!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📦 Next steps:" -ForegroundColor Cyan
    Write-Host "   1. mvn clean install" -ForegroundColor Yellow
    Write-Host "   2. mvn spring-boot:run" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "🔍 After starting, look for this in the logs:" -ForegroundColor Cyan
    Write-Host "   'Cloudinary configured for cloud: $cloudName'" -ForegroundColor Yellow
}

Write-Host ""
