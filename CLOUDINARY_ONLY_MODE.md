# Cloudinary-Only Storage Configuration

## Overview
The backend has been configured to use **ONLY Cloudinary** for image storage. Local file storage fallback has been completely removed.

## Changes Made

### 1. FileUploadController.java
**Location**: `src/main/java/com/residentia/controller/FileUploadController.java`

#### Single File Upload (lines 73-104)
- **Removed**: Local storage fallback logic (previously lines 103-120)
- **Changed**: Now returns HTTP 503 error if Cloudinary is not configured
- **Changed**: Removed try-catch fallback - upload either succeeds to Cloudinary or fails with error

**Before**:
```java
if (isCloudinaryConfigured()) {
    try {
        // Upload to Cloudinary
    } catch (Exception e) {
        // Fallback to local storage (REMOVED)
    }
}
// Local storage code (REMOVED)
```

**After**:
```java
// ONLY use Cloudinary - no local storage fallback
if (!isCloudinaryConfigured()) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("Cloud storage not configured. Please configure Cloudinary credentials.");
}

// Upload to Cloudinary (no try-catch fallback)
String imageUrl = cloudinaryService.uploadImage(file, CLOUDINARY_FOLDER);
log.info("✅ File uploaded to Cloudinary with PUBLIC ACCESS");
log.info("   Storage: ☁️ CLOUD ONLY (no local copy)");
```

#### Multiple File Upload (lines 130+)
- **Removed**: Local storage fallback in catch block
- **Changed**: Skips files that fail Cloudinary upload instead of falling back to local

**Before**:
```java
if (usingCloudinary) {
    try {
        // Upload to Cloudinary
    } catch (Exception e) {
        // Fallback to local storage (REMOVED)
    }
} else {
    // Local storage (REMOVED)
}
```

**After**:
```java
// ONLY use Cloudinary - no local storage
if (!usingCloudinary) {
    log.error("❌ Cloudinary not configured - skipping file");
    continue;
}

// Upload to Cloudinary (no fallback)
imageUrl = cloudinaryService.uploadImage(file, CLOUDINARY_FOLDER);
```

#### Class Documentation
Updated class-level comment to reflect cloud-only mode:
```java
/**
 * Controller for handling file uploads
 * - Uses ONLY Cloudinary for new uploads (no local storage)
 * - Serves existing local images for backward compatibility only
 * - All new images are stored in the cloud
 */
```

### 2. Local Storage Endpoints (Unchanged)
- **GET `/api/files/images/{filename}`**: Still serves existing local images for backward compatibility
- **DELETE `/api/files/images/{filename}`**: Still deletes local images if needed

## Configuration

### application.yml
Cloudinary credentials are configured with default values:
```yaml
cloudinary:
  cloud-name: ${CLOUDINARY_CLOUD_NAME:doux976kl}
  api-key: ${CLOUDINARY_API_KEY:553679296573887}
  api-secret: ${CLOUDINARY_API_SECRET:xdyGM0V8Q6jBkx1I3ZNlZVoZRCc}
```

### Environment Variables (Optional)
You can still override via environment variables:
```powershell
$env:CLOUDINARY_CLOUD_NAME="doux976kl"
$env:CLOUDINARY_API_KEY="553679296573887"
$env:CLOUDINARY_API_SECRET="xdyGM0V8Q6jBkx1I3ZNlZVoZRCc"
```

## Behavior

### Successful Upload
1. Client uploads image to `/api/files/upload`
2. Backend validates Cloudinary configuration
3. Image is uploaded to Cloudinary (`residentia/properties` folder)
4. Returns Cloudinary URL: `https://res.cloudinary.com/doux976kl/image/upload/...`
5. **No local copy is created**

### Failed Upload Scenarios

#### Cloudinary Not Configured
- **HTTP 503 Service Unavailable**
- Message: `"Cloud storage not configured. Please configure Cloudinary credentials."`

#### Cloudinary Upload Fails
- **HTTP 500 Internal Server Error** (exception thrown)
- No fallback to local storage
- Image upload fails completely

## Testing

### Test Cloudinary Upload
1. **Start backend**: `java -jar target/residentia-backend-1.0.0.jar`
2. **Check logs** for: `✅ Cloudinary configured successfully for cloud: doux976kl`
3. **Upload image** via owner panel
4. **Verify logs** show:
   ```
   ✅ File uploaded to Cloudinary with PUBLIC ACCESS
   URL: https://res.cloudinary.com/doux976kl/image/upload/...
   Accessible by: ALL USERS (owner, client, admin roles)
   Storage: ☁️ CLOUD ONLY (no local copy)
   ```
5. **Check database**: `image_url` should start with `https://res.cloudinary.com/`
6. **Verify**: No new files in `uploads/properties/` directory

### Test Error Handling
To test Cloudinary-not-configured error:
1. Remove Cloudinary credentials from `application.yml`
2. Restart backend
3. Try to upload image
4. Should receive HTTP 503 error

## Migration Plan (Optional)

### Migrate Existing Local Images to Cloudinary
Current state: 6 active properties use local storage (`/api/files/images/...`)

To migrate:
1. Write script to upload each local image to Cloudinary
2. Update database `image_url` column with new Cloudinary URLs
3. Keep local files as backup (served via `/api/files/images/` endpoint)

Example SQL after migration:
```sql
UPDATE pgs 
SET image_url = 'https://res.cloudinary.com/doux976kl/image/upload/v1738602345/residentia/properties/xyz.jpg'
WHERE image_url = '/api/files/images/xyz.jpg';
```

## Benefits

1. **✅ Cloud-Native**: No dependency on local file system
2. **✅ Scalable**: Works in containerized/serverless environments  
3. **✅ Consistent**: All new images go to same storage
4. **✅ No Silent Failures**: Upload fails clearly instead of masking issues
5. **✅ Production-Ready**: Cloudinary handles CDN, resizing, optimization

## Rollback

To re-enable local storage fallback:
1. Restore previous version of `FileUploadController.java` from git
2. Rebuild: `mvn clean package -DskipTests`
3. Restart backend

## Logs

When Cloudinary-only mode is active, you'll see:
```
INFO  c.r.service.CloudinaryService - Cloudinary service initialized successfully for cloud: doux976kl
INFO  c.residentia.config.CloudinaryConfig - ✅ Cloudinary configured successfully for cloud: doux976kl
INFO  c.r.controller.FileUploadController - Using upload directory: C:\Prajwal\...\uploads\properties
INFO  c.r.controller.FileUploadController - ✅ File uploaded to Cloudinary with PUBLIC ACCESS
INFO  c.r.controller.FileUploadController -    URL: https://res.cloudinary.com/...
INFO  c.r.controller.FileUploadController -    Storage: ☁️ CLOUD ONLY (no local copy)
```

## Summary

**Before**: Cloudinary primary → local fallback → success (might be local)  
**After**: Cloudinary only → success or fail (never local)

All new image uploads **must** succeed on Cloudinary or they fail completely. No more hidden local storage.