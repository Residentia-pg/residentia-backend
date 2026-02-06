# Cloudinary Configuration Script
# Run this before starting your application

Write-Host "`n☁️  Configuring Cloudinary..." -ForegroundColor Cyan

# Set Cloudinary credentials
$env:CLOUDINARY_CLOUD_NAME = "doux976kl"
$env:CLOUDINARY_API_KEY = "553679296573887"
$env:CLOUDINARY_API_SECRET = "YOUR_ACTUAL_SECRET_HERE"  # Replace with your actual secret

Write-Host "✅ Cloudinary environment variables set!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Configuration:" -ForegroundColor Cyan
Write-Host "   Cloud Name: $env:CLOUDINARY_CLOUD_NAME" -ForegroundColor Gray
Write-Host "   API Key: $env:CLOUDINARY_API_KEY" -ForegroundColor Gray
Write-Host "   API Secret: ********** (hidden)" -ForegroundColor Gray
Write-Host ""
Write-Host "🚀 Starting Spring Boot application..." -ForegroundColor Cyan
Write-Host ""

# Start the application
mvn spring-boot:run
