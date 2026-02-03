# Fix Missing Property Images
# This script adds public Cloudinary/Unsplash image URLs to properties without images

Write-Host "🔧 Fixing Missing Property Images..." -ForegroundColor Cyan
Write-Host ""

# Database credentials (update from application.yml)
$DB_HOST = "residentia-db.cp2s0wi2ykmu.ap-south-1.rds.amazonaws.com"
$DB_PORT = "3306"
$DB_NAME = "residentia_db"
$DB_USER = "admin"
$DB_PASS = "Residentia25"

Write-Host "📊 Checking current image status..." -ForegroundColor Yellow

# Check current status
$checkQuery = @"
SELECT 
    id, 
    property_name, 
    city, 
    status,
    CASE 
        WHEN imageUrl IS NULL THEN 'NULL'
        WHEN imageUrl = '' THEN 'EMPTY'
        ELSE 'HAS_IMAGE'
    END as image_status
FROM pgs 
ORDER BY id;
"@

Write-Host "Before Update:" -ForegroundColor Yellow
mysql -h $DB_HOST -u $DB_USER -p"$DB_PASS" -D $DB_NAME -e $checkQuery 2>$null

Write-Host ""
Write-Host "🔄 Updating properties with sample images..." -ForegroundColor Cyan

# Update properties with public image URLs
$updateQuery = @"
-- Update properties without images
UPDATE pgs 
SET imageUrl = 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800&auto=format&fit=crop'
WHERE id = 1 AND (imageUrl IS NULL OR imageUrl = '');

UPDATE pgs 
SET imageUrl = 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800&auto=format&fit=crop'
WHERE id = 2 AND (imageUrl IS NULL OR imageUrl = '');

UPDATE pgs 
SET imageUrl = 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800&auto=format&fit=crop'
WHERE id = 3 AND (imageUrl IS NULL OR imageUrl = '');

UPDATE pgs 
SET imageUrl = 'https://images.unsplash.com/photo-1513584684374-8bab748fbf90?w=800&auto=format&fit=crop'
WHERE id = 4 AND (imageUrl IS NULL OR imageUrl = '');

UPDATE pgs 
SET imageUrl = 'https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800&auto=format&fit=crop'
WHERE (imageUrl IS NULL OR imageUrl = '') AND id > 4;
"@

$updateResult = mysql -h $DB_HOST -u $DB_USER -p"$DB_PASS" -D $DB_NAME -e $updateQuery 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Images updated successfully!" -ForegroundColor Green
} else {
    Write-Host "❌ Error updating images: $updateResult" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "📊 Verifying updates..." -ForegroundColor Yellow
Write-Host "After Update:" -ForegroundColor Yellow

# Verify updates
$verifyQuery = @"
SELECT 
    id, 
    property_name, 
    city, 
    status,
    CASE 
        WHEN imageUrl IS NULL THEN '❌ NULL'
        WHEN imageUrl = '' THEN '❌ EMPTY'
        WHEN imageUrl LIKE 'https://images.unsplash.com%' THEN '✅ UNSPLASH'
        WHEN imageUrl LIKE 'https://res.cloudinary.com%' THEN '✅ CLOUDINARY'
        ELSE '✅ OTHER'
    END as image_status,
    LEFT(imageUrl, 50) as image_preview
FROM pgs 
ORDER BY id;
"@

mysql -h $DB_HOST -u $DB_USER -p"$DB_PASS" -D $DB_NAME -e $verifyQuery 2>$null

Write-Host ""
Write-Host "✅ Done! Refresh your browser to see the images." -ForegroundColor Green
Write-Host "   Note: These are public Unsplash images for demonstration." -ForegroundColor Gray
Write-Host "   Replace with actual property images via the upload feature." -ForegroundColor Gray
