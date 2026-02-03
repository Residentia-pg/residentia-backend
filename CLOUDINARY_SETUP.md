# Cloudinary Configuration Guide

## Overview
The Residentia application supports both **Cloudinary** (cloud storage) and **local file storage** for property images. By default, it uses local storage, but you can optionally configure Cloudinary for better scalability and performance.

## How It Works

### Without Cloudinary (Default)
- Images are stored in `/uploads/properties/` directory
- Images are served via `/api/files/images/{filename}` endpoint
- No external dependencies required

### With Cloudinary (Optional)
- Images are automatically uploaded to Cloudinary cloud storage
- If Cloudinary upload fails, the system automatically falls back to local storage
- Provides CDN delivery, automatic optimization, and transformations

## Setting Up Cloudinary

### Step 1: Create a Cloudinary Account
1. Go to [https://cloudinary.com/](https://cloudinary.com/)
2. Sign up for a free account
3. After login, you'll see your dashboard with credentials

### Step 2: Get Your Credentials
From your Cloudinary dashboard, note down:
- **Cloud Name**: Your unique cloud identifier
- **API Key**: Your API key
- **API Secret**: Your API secret

### Step 3: Configure Application

#### Option A: Environment Variables (Recommended for Production)
Set the following environment variables:

**Windows:**
```cmd
set CLOUDINARY_CLOUD_NAME=your-cloud-name
set CLOUDINARY_API_KEY=your-api-key
set CLOUDINARY_API_SECRET=your-api-secret
```

**Linux/Mac:**
```bash
export CLOUDINARY_CLOUD_NAME=your-cloud-name
export CLOUDINARY_API_KEY=your-api-key
export CLOUDINARY_API_SECRET=your-api-secret
```

#### Option B: Application Configuration (Development)
Edit `src/main/resources/application.yml`:

```yaml
cloudinary:
  cloud-name: your-cloud-name
  api-key: your-api-key
  api-secret: your-api-secret
```

**⚠️ Warning:** Never commit API secrets to version control!

### Step 4: Restart the Application
After configuring, restart your Spring Boot application. You should see:
```
INFO  c.r.service.CloudinaryService - Cloudinary service initialized successfully for cloud: your-cloud-name
```

## Testing the Configuration

### Upload a File
```bash
curl -X POST http://localhost:8888/api/files/upload \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/image.jpg"
```

### Expected Response (Cloudinary)
```json
{
  "url": "https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/residentia/properties/abc123.jpg",
  "filename": "abc123.jpg"
}
```

### Expected Response (Local Storage)
```json
{
  "url": "/api/files/images/abc123-uuid.jpg",
  "filename": "abc123-uuid.jpg"
}
```

## Image Upload Endpoints

### Single File Upload
- **Endpoint:** `POST /api/files/upload`
- **Parameter:** `file` (multipart/form-data)
- **Returns:** `{ url, filename }`

### Multiple Files Upload
- **Endpoint:** `POST /api/files/upload-multiple`
- **Parameter:** `files[]` (multipart/form-data)
- **Returns:** Array of `{ url, filename }`

### Get Image (Local Only)
- **Endpoint:** `GET /api/files/images/{filename}`
- **Returns:** Image binary data

### Delete Image (Local Only)
- **Endpoint:** `DELETE /api/files/images/{filename}`
- **Returns:** Success message

## Folder Structure in Cloudinary
All images are uploaded to: `residentia/properties/`

You can view and manage them in your Cloudinary Media Library.

## Troubleshooting

### Cloudinary Not Working
If you see logs like:
```
WARN c.r.controller.FileUploadController - Cloudinary upload failed, falling back to local storage
```

**Check:**
1. Credentials are correct in `application.yml` or environment variables
2. Your Cloudinary account is active
3. Network connectivity to Cloudinary servers
4. API limits not exceeded (free tier has limits)

### Local Storage Issues
If local storage fails:
```
ERROR c.r.controller.FileUploadController - Failed to upload file
```

**Check:**
1. Application has write permissions to `/uploads/properties/`
2. Sufficient disk space available
3. File size within limits (max 10MB per file, 50MB per request)

## Migration Guide

### From Local to Cloudinary
1. Configure Cloudinary as described above
2. Restart application
3. New uploads will go to Cloudinary
4. Existing local images will continue to work (backward compatible)

### From Cloudinary to Local
1. Remove/comment out Cloudinary configuration
2. Restart application
3. New uploads will go to local storage
4. Existing Cloudinary URLs will continue to work (they're permanent CDN links)

## Security Best Practices

1. **Never commit secrets** to git
2. Use **environment variables** in production
3. Enable **CORS** only for trusted domains
4. Consider implementing **signed uploads** for sensitive data
5. Use Cloudinary's **transformation** features for:
   - Automatic format optimization
   - Quality adjustment
   - Responsive images

## Cost Considerations

### Cloudinary Free Tier
- 25 GB storage
- 25 GB monthly bandwidth
- 25,000 transformations/month

### Local Storage
- Limited by server disk space
- No bandwidth costs
- Manual backups required

## Support
For issues related to:
- **Cloudinary:** [https://support.cloudinary.com/](https://support.cloudinary.com/)
- **Application:** Check application logs in `/logs/` directory
