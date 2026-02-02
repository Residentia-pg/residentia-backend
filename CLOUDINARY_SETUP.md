# Cloudinary Integration Setup Guide

## Overview
This application now uses **Cloudinary** (FREE tier) for image storage instead of local file system storage. Cloudinary provides:
- ✅ **25 credits/month** (sufficient for moderate usage)
- ✅ **25GB storage**
- ✅ **25GB bandwidth**
- ✅ **Automatic image optimization**
- ✅ **CDN delivery worldwide**
- ✅ **Automatic format conversion** (WebP, AVIF)
- ✅ **Image transformations** (resize, crop, quality optimization)

---

## Step 1: Create a FREE Cloudinary Account

1. Go to [https://cloudinary.com/users/register/free](https://cloudinary.com/users/register/free)
2. Sign up with your email or Google/GitHub account
3. Verify your email address
4. You'll be redirected to your dashboard

---

## Step 2: Get Your Cloudinary Credentials

1. After logging in, you'll see your **Dashboard**
2. Find the **Account Details** section (usually at the top)
3. You'll see three important values:
   - **Cloud Name**: e.g., `dxyz123abc`
   - **API Key**: e.g., `123456789012345`
   - **API Secret**: e.g., `abcdefghijklmnopqrstuvwxyz123456`

4. **Keep these credentials secure!** Don't commit them to Git.

---

## Step 3: Configure Your Application

### Option A: Using Environment Variables (Recommended for Production)

**Windows (PowerShell):**
```powershell
$env:CLOUDINARY_CLOUD_NAME="your-cloud-name"
$env:CLOUDINARY_API_KEY="your-api-key"
$env:CLOUDINARY_API_SECRET="your-api-secret"
```

**Windows (Command Prompt):**
```cmd
set CLOUDINARY_CLOUD_NAME=your-cloud-name
set CLOUDINARY_API_KEY=your-api-key
set CLOUDINARY_API_SECRET=your-api-secret
```

**Linux/Mac:**
```bash
export CLOUDINARY_CLOUD_NAME="your-cloud-name"
export CLOUDINARY_API_KEY="your-api-key"
export CLOUDINARY_API_SECRET="your-api-secret"
```

### Option B: Update application.yml (For Development Only)

Open `src/main/resources/application.yml` and replace the placeholder values:

```yaml
cloudinary:
  cloud-name: dxyz123abc          # Replace with your cloud name
  api-key: 123456789012345        # Replace with your API key
  api-secret: abcdefgh1234        # Replace with your API secret
```

⚠️ **IMPORTANT:** If you use Option B, add `application.yml` to `.gitignore` to prevent committing credentials!

---

## Step 4: Build and Run

1. **Clean and rebuild** the project to download Cloudinary dependencies:
   ```powershell
   cd server/residentia-backend
   mvn clean install
   ```

2. **Run the application:**
   ```powershell
   mvn spring-boot:run
   ```

3. **Check the logs** for successful Cloudinary initialization:
   ```
   Cloudinary configuration initialized
   Cloudinary configured for cloud: your-cloud-name
   ```

---

## Step 5: Test Image Upload

### Using the Frontend:
1. Navigate to the Owner Dashboard → Add Property
2. Upload images (JPG, PNG, GIF)
3. The images will now be uploaded to Cloudinary

### Using Postman/cURL:
```bash
curl -X POST http://localhost:8888/api/files/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@path/to/your/image.jpg"
```

**Expected Response:**
```json
{
  "url": "https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/residentia/properties/uuid.jpg",
  "filename": "uuid.jpg"
}
```

---

## How It Works

### Before (Local Storage):
- Images saved to: `server/residentia-backend/uploads/properties/`
- Access via: `http://localhost:8888/api/files/images/{filename}`
- ❌ Not scalable
- ❌ No CDN
- ❌ No optimization

### After (Cloudinary):
- Images uploaded to: **Cloudinary Cloud**
- Access via: `https://res.cloudinary.com/your-cloud-name/...`
- ✅ Scalable
- ✅ Global CDN
- ✅ Automatic optimization (quality, format, size)
- ✅ Automatic transformations (width: 1200px, height: 800px, crop: limit)

---

## Code Changes Summary

### 1. **CloudinaryConfig.java**
- Configures Cloudinary SDK with your credentials
- Located: `src/main/java/com/residentia/config/CloudinaryConfig.java`

### 2. **CloudinaryService.java**
- Handles image upload to Cloudinary
- Handles image deletion from Cloudinary
- Auto-applies transformations (resize, quality)
- Located: `src/main/java/com/residentia/service/CloudinaryService.java`

### 3. **FileUploadController.java** (Updated)
- `/api/files/upload` → Uploads to Cloudinary
- `/api/files/upload-multiple` → Uploads multiple images to Cloudinary
- Returns full Cloudinary URLs (not relative paths)

### 4. **Frontend (No Changes Required!)**
- The API contract remains the same
- Upload endpoints return `{ url: "...", filename: "..." }`
- Images display directly from Cloudinary CDN

---

## Troubleshooting

### Error: "Cloudinary credentials are not fully configured"
- **Cause:** Environment variables not set or incorrect
- **Solution:** Double-check your credentials and restart the application

### Error: "Failed to upload file to Cloudinary"
- **Cause:** Network issues, invalid credentials, or free tier limit exceeded
- **Solution:** 
  1. Check your internet connection
  2. Verify credentials on Cloudinary dashboard
  3. Check your monthly usage: [Dashboard → Usage](https://cloudinary.com/console/usage)

### Images not displaying
- **Cause:** The database still has old local paths (`/api/files/images/...`)
- **Solution:** Re-upload images or manually update image URLs in the database

### Free Tier Limits Exceeded
- **Free tier:** 25 credits/month
- **Usage:** Check at [https://cloudinary.com/console/usage](https://cloudinary.com/console/usage)
- **Solution:** Upgrade to a paid plan or optimize image uploads

---

## Migration from Local Storage

If you have existing properties with local image paths:

1. **Option A: Re-upload images**
   - Edit each property in the Owner Dashboard
   - Upload new images
   - Save

2. **Option B: Manual migration script** (Advanced)
   ```sql
   -- Images stored with paths like: /api/files/images/uuid.jpg
   -- After Cloudinary migration, they'll be: https://res.cloudinary.com/...
   -- You'll need to re-upload or keep local files as fallback
   ```

---

## Benefits of Cloudinary

1. **No Server Storage Required**
   - Images stored in the cloud, not on your server
   - Reduces server disk usage

2. **Automatic Optimization**
   - Images automatically converted to WebP/AVIF for modern browsers
   - Quality optimization reduces file size

3. **Global CDN**
   - Images served from nearest CDN edge location
   - Faster loading times worldwide

4. **Transformations**
   - Automatic resize to max 1200x800px
   - Maintains aspect ratio
   - Quality: auto (optimal balance)

5. **Security**
   - Secure HTTPS URLs
   - Access control via Cloudinary dashboard

---

## Additional Resources

- **Cloudinary Dashboard:** [https://cloudinary.com/console](https://cloudinary.com/console)
- **Cloudinary Docs:** [https://cloudinary.com/documentation](https://cloudinary.com/documentation)
- **Usage Stats:** [https://cloudinary.com/console/usage](https://cloudinary.com/console/usage)
- **Java SDK Docs:** [https://cloudinary.com/documentation/java_integration](https://cloudinary.com/documentation/java_integration)

---

## Support

If you encounter any issues:
1. Check the application logs for detailed error messages
2. Verify your Cloudinary credentials
3. Check your free tier usage limits
4. Contact Cloudinary support: [https://support.cloudinary.com](https://support.cloudinary.com)

---

**Happy uploading! 🚀📸**
