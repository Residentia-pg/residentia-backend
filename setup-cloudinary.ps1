# Cloudinary Environment Setup Script for Windows
# Run this script to set environment variables before starting the server

# Replace these with your actual Cloudinary credentials
# Get them from: https://cloudinary.com/console

Write-Host "🔧 Setting up Cloudinary environment variables..." -ForegroundColor Cyan

# Set your Cloudinary credentials here
$env:CLOUDINARY_CLOUD_NAME = "your-cloud-name"
$env:CLOUDINARY_API_KEY = "your-api-key"
$env:CLOUDINARY_API_SECRET = "your-api-secret"

Write-Host "✅ Environment variables set:" -ForegroundColor Green
Write-Host "   CLOUDINARY_CLOUD_NAME = $env:CLOUDINARY_CLOUD_NAME" -ForegroundColor Yellow
Write-Host "   CLOUDINARY_API_KEY = $env:CLOUDINARY_API_KEY" -ForegroundColor Yellow
Write-Host "   CLOUDINARY_API_SECRET = ********" -ForegroundColor Yellow

Write-Host ""
Write-Host "📝 Note: These are temporary and only valid for this PowerShell session." -ForegroundColor Magenta
Write-Host "   For permanent setup, add them to your System Environment Variables." -ForegroundColor Magenta
Write-Host ""
Write-Host "🚀 Now run: mvn spring-boot:run" -ForegroundColor Cyan
