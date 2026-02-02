# Kill any existing Java processes
Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object { $_.Path -like "*residentia*" } | Stop-Process -Force

Write-Host "Starting Residentia Backend Server..." -ForegroundColor Green
Write-Host "Backend will be available at: http://localhost:8888" -ForegroundColor Cyan
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Yellow
Write-Host ""

# Navigate to project directory
Set-Location "C:\Prajwal\Sunbeam\Project\final PG\residentia\residentia\server\residentia-backend"

# Run the JAR with proper console attachment
java -jar target\residentia-backend-1.0.0.jar
