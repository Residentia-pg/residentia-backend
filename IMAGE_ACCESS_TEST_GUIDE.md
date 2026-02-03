# Quick Test Guide - Universal Image Access

## ✅ Verification Checklist

### 1. Environment Setup
```bash
# Check Cloudinary is configured
# In application.yml or environment variables:
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
```

### 2. Test Image Upload (As Owner)
```bash
# Login as owner
POST /api/auth/owner/login
{
  "email": "yash@gmail.com",
  "password": "your-password"
}

# Upload image
POST /api/files/upload
Content-Type: multipart/form-data
file: [image file]

# Response:
{
  "url": "https://res.cloudinary.com/YOUR_CLOUD/image/upload/v123/residentia/properties/abc.jpg",
  "filename": "abc.jpg"
}

# Create property with image
POST /api/owner/pgs
{
  "propertyName": "Test PG",
  "imageUrl": "https://res.cloudinary.com/.../abc.jpg",
  "address": "123 Main St",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400001",
  "rentAmount": 10000,
  "maxCapacity": 4,
  "availableBeds": 2,
  "sharingType": "Double",
  "foodIncluded": true
}
```

### 3. Test Image Visibility (As Client)
```bash
# Login as client
POST /api/auth/user/login
{
  "email": "client@example.com",
  "password": "password"
}

# View all properties
GET /api/client/properties

# Response should include:
[
  {
    "propertyId": 1,
    "propertyName": "Test PG",
    "imageUrl": "https://res.cloudinary.com/.../abc.jpg",  ← SAME URL
    ...
  }
]

# View specific property
GET /api/client/properties/1

# Response should include same imageUrl
```

### 4. Test Image Visibility (As Admin)
```bash
# Login as admin
POST /api/auth/admin/login
{
  "email": "admin@residentia.com",
  "password": "admin-password"
}

# View all properties
GET /api/admin/pgs

# Response should include:
[
  {
    "id": 1,
    "propertyName": "Test PG",
    "imageUrl": "https://res.cloudinary.com/.../abc.jpg",  ← SAME URL
    "status": "PENDING"
  }
]
```

### 5. Browser Test
```bash
# Copy imageUrl from any API response
# Example: https://res.cloudinary.com/demo/image/upload/v123/residentia/properties/abc.jpg

# Paste in browser (new incognito window)
# Image should load WITHOUT authentication ✓
```

## ✅ Expected Log Output

### When uploading:
```
✅ Image uploaded to Cloudinary with PUBLIC access
   URL: https://res.cloudinary.com/.../image.jpg
   Public ID: residentia/properties/uuid
   Access: PUBLIC (viewable by all authenticated users)

✅ File uploaded to Cloudinary with PUBLIC ACCESS
   URL: https://res.cloudinary.com/.../image.jpg
   Accessible by: ALL USERS (owner, client, admin roles)
```

### When fetching properties:
```
Fetching all available properties
Retrieved 3 active properties
```

## ❌ Troubleshooting

### Images not visible?

1. **Check Cloudinary Config**
   ```bash
   # Backend logs should show:
   Cloudinary service initialized successfully for cloud: YOUR_CLOUD
   
   # If you see:
   Cloudinary is not configured. Using local storage...
   # → Set environment variables!
   ```

2. **Check Image URL Format**
   ```sql
   SELECT imageUrl FROM pgs LIMIT 1;
   
   -- Good: https://res.cloudinary.com/...
   -- Bad:  /api/files/images/... (local storage)
   ```

3. **Check Browser Console**
   ```
   F12 → Console → Look for:
   - CORS errors on cloudinary.com
   - 404 Not Found on image URLs
   - ERR_CONNECTION_REFUSED
   ```

4. **Check Database**
   ```sql
   -- Verify imageUrl is saved
   SELECT id, property_name, imageUrl FROM pgs;
   
   -- Should NOT be NULL or empty
   ```

5. **Test Direct Access**
   ```bash
   # Copy imageUrl from database
   curl https://res.cloudinary.com/.../image.jpg
   
   # Should return image data (not 403 Forbidden)
   ```

## ✅ Success Criteria

- [ ] Owner uploads image → Cloudinary URL returned
- [ ] Owner creates property → imageUrl saved in database
- [ ] Client views properties → Same imageUrl visible
- [ ] Admin views properties → Same imageUrl visible
- [ ] Browser test → Image loads without authentication
- [ ] Logs show "PUBLIC ACCESS" message

## 📞 Quick Commands

```bash
# Restart backend
cd server/residentia-backend
mvn spring-boot:run

# Check Cloudinary config
grep -A3 "cloudinary:" src/main/resources/application.yml

# Check database
mysql -h your-db-host -u username -p
USE residentia_db;
SELECT id, property_name, LEFT(imageUrl, 50) as image_url_preview FROM pgs;

# Test API
curl -X GET http://localhost:8888/api/client/properties \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 🎯 Key Points

1. ✅ **Images are PUBLIC by design** - This is correct behavior
2. ✅ **No role-based filtering** - All authenticated users see all images
3. ✅ **Cloudinary handles hosting** - Backend just stores URLs
4. ✅ **Frontend works universally** - No special logic per role
5. ✅ **Database stores full URLs** - Not encrypted or obfuscated

## 📚 Documentation
- Full architecture: `IMAGE_ACCESS_ARCHITECTURE.md`
- Changes summary: `IMAGE_ACCESS_CHANGES_SUMMARY.md`
- API docs: `README.md`
