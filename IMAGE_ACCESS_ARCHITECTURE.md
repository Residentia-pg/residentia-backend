# Image Access Architecture - Universal Public Access

## Overview
The Residentia application implements a universal image access system where **all property images are publicly accessible** to any authenticated user, regardless of their role (Owner, Client, or Admin).

## Architecture Design

### 1. Image Storage
- **Primary**: Cloudinary Cloud Storage (when configured)
- **Fallback**: Local file system (for backward compatibility)
- **Database**: Image URLs stored in `pgs.imageUrl` column (LONGTEXT)

### 2. Public Access Configuration

#### Cloudinary Upload Settings
Images are uploaded with the following settings to ensure public accessibility:
```java
"access_mode", "public"  // Ensures public access for all users
"type", "upload"         // Standard upload type
"overwrite", false       // Preserves unique filenames
```

#### URL Format
- **Cloudinary**: `https://res.cloudinary.com/{cloud-name}/image/upload/v{version}/residentia/properties/{filename}.jpg`
- **Local**: `/api/files/images/{filename}.jpg`

### 3. Access Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                        IMAGE UPLOAD                              │
│                                                                  │
│  Owner Panel → Upload Image → Cloudinary (PUBLIC) → Save URL    │
│                                          ↓                       │
│                                   Database Store                 │
│                                   (pgs.imageUrl)                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                     IMAGE RETRIEVAL                              │
│                                                                  │
│  ┌──────────┐       ┌──────────┐       ┌───────────┐           │
│  │  Owner   │       │  Client  │       │   Admin   │           │
│  │  Panel   │       │  Browse  │       │ Dashboard │           │
│  └────┬─────┘       └────┬─────┘       └─────┬─────┘           │
│       │                  │                    │                 │
│       └──────────────────┴────────────────────┘                 │
│                          │                                      │
│                    Fetch Property                               │
│                          │                                      │
│                    PropertyDTO                                  │
│                          │                                      │
│                  imageUrl (Public URL)                          │
│                          │                                      │
│            Frontend displays image                              │
│            (No permission check required)                       │
└─────────────────────────────────────────────────────────────────┘
```

## Implementation Details

### Backend Services

#### 1. CloudinaryService.java
```java
public String uploadImage(MultipartFile file, String folder) {
    Map uploadParams = ObjectUtils.asMap(
        "folder", folder,
        "resource_type", "image",
        "access_mode", "public",  // ← PUBLIC ACCESS
        // ... other params
    );
    // Returns secure_url: https://res.cloudinary.com/...
}
```

#### 2. PropertyService.java
- Stores imageUrl in Property entity
- No role-based filtering on image URLs
- All methods (create, update, get) preserve imageUrl

#### 3. ClientService.java
- `getAllAvailableProperties()` - Returns all ACTIVE properties with imageUrl
- `getPropertyById()` - Returns property with imageUrl
- `convertToDTO()` - Includes imageUrl for all clients

#### 4. AdminService (via AdminPgController)
- `getAll()` - Returns all properties with imageUrl
- Full access to all property data

### Frontend Handling

#### PublicBrowse.jsx (Client View)
```jsx
{property.imageUrl && (
  <img
    src={property.imageUrl.startsWith('http') 
      ? property.imageUrl           // Cloudinary URL (public)
      : `http://localhost:8888${property.imageUrl}`  // Local URL
    }
    alt={property.propertyName}
  />
)}
```

#### ViewProperty.jsx (Owner Panel)
- Same image display logic
- No special permissions required

#### AdminDashboard.jsx
- Full visibility of all property images
- Same URL access as other roles

## Security Model

### What IS Secured
✅ **API Endpoints**: JWT authentication required for all property operations  
✅ **Property Operations**: Create/Update/Delete restricted by role  
✅ **Admin Actions**: Approve/Reject limited to admin role  

### What Is NOT Secured (By Design)
❌ **Image URLs**: Publicly accessible via Cloudinary  
❌ **Image Display**: No role-based filtering  
❌ **Property Images**: Visible to all authenticated users  

**Rationale**: Property images are meant to be discoverable by potential clients. Restricting image access would defeat the purpose of a property browsing platform.

## Testing Image Access

### 1. As Owner
1. Login as owner (yash@gmail.com)
2. Add property with image
3. Image uploads to Cloudinary with public access
4. View in "My Properties" → Image visible ✓

### 2. As Client
1. Login as client
2. Browse properties → `/api/client/properties`
3. All ACTIVE property images visible ✓
4. Click property details → Image loads from same Cloudinary URL ✓

### 3. As Admin
1. Login as admin
2. View properties dashboard → `/api/admin/pgs`
3. All property images visible (PENDING, ACTIVE, REJECTED) ✓

## Troubleshooting

### "Images not visible for other users"

**Possible Causes:**
1. ❌ Cloudinary not configured → Images stored locally (server-specific)
2. ❌ Frontend checking wrong URL format
3. ❌ CORS issues blocking Cloudinary domain
4. ❌ Image URL not saved to database

**Solutions:**
1. ✅ Configure Cloudinary environment variables
2. ✅ Check browser console for CORS errors
3. ✅ Verify imageUrl in database: `SELECT imageUrl FROM pgs;`
4. ✅ Check backend logs for upload confirmation

### Verifying Public Access

```sql
-- Check if images are stored as Cloudinary URLs
SELECT 
    id, 
    property_name, 
    imageUrl,
    CASE 
        WHEN imageUrl LIKE 'https://res.cloudinary.com%' THEN 'CLOUDINARY (PUBLIC)'
        WHEN imageUrl LIKE '/api/files/images/%' THEN 'LOCAL (SERVER)'
        ELSE 'UNKNOWN'
    END as storage_type
FROM pgs;
```

## Environment Configuration

### Required for Cloudinary (Public Access)
```properties
# application.yml or environment variables
cloudinary:
  cloud-name: ${CLOUDINARY_CLOUD_NAME}
  api-key: ${CLOUDINARY_API_KEY}
  api-secret: ${CLOUDINARY_API_SECRET}
```

### Frontend CORS (if needed)
```yaml
spring:
  mvc:
    cors:
      allowed-origins: http://localhost:5173
      allowed-headers: "*"
      allow-credentials: true
```

## Best Practices

1. ✅ **Use Cloudinary for production** - Ensures universal access
2. ✅ **Store full URLs in database** - Not just filenames
3. ✅ **Log upload confirmation** - Verify public access mode
4. ✅ **Handle both URL formats in frontend** - Cloudinary + local fallback
5. ✅ **Test with multiple roles** - Verify cross-role visibility

## API Response Examples

### Owner Creates Property
```json
POST /api/owner/pgs
{
  "propertyName": "YOLO PG",
  "imageUrl": "https://res.cloudinary.com/demo/image/upload/v123/residentia/properties/abc.jpg"
}
```

### Client Views Property
```json
GET /api/client/properties
Response:
[
  {
    "propertyId": 1,
    "propertyName": "YOLO PG",
    "imageUrl": "https://res.cloudinary.com/demo/image/upload/v123/residentia/properties/abc.jpg",
    "ownerId": 1
  }
]
```

### Admin Views All Properties
```json
GET /api/admin/pgs
Response:
[
  {
    "id": 1,
    "propertyName": "YOLO PG",
    "imageUrl": "https://res.cloudinary.com/demo/image/upload/v123/residentia/properties/abc.jpg",
    "status": "PENDING"
  }
]
```

**Notice**: Same imageUrl is returned for all roles - this is intentional and correct.

## Conclusion

The Residentia application is designed with **universal image access** as a core feature. All property images are publicly accessible via Cloudinary URLs, and all authenticated users (regardless of role) can view any property image. This is the expected and desired behavior for a property browsing platform.

**No code changes are needed to enable cross-role image visibility - the system already works this way by design.**
