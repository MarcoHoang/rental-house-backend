# File Upload System Guide

## Tổng quan

Hệ thống upload file của Rental House backend hỗ trợ các loại file sau:
- **Images**: jpg, jpeg, png, gif, webp
- **Documents**: pdf (cho proof of ownership)

## Các Endpoint Available

### 1. Upload Avatar (User Profile)
```http
POST /api/files/upload/avatar
Content-Type: multipart/form-data

Form Data:
- file: [image file]
```

**Response:**
```json
{
  "code": "10",
  "message": "File uploaded successfully",
  "data": {
    "fileUrl": "http://localhost:8080/api/files/avatar/abc123.jpg",
    "originalFilename": "avatar.jpg",
    "storedFilename": "abc123.jpg",
    "fileSize": 1024000,
    "contentType": "image/jpeg",
    "uploadType": "avatar",
    "uploadedAt": "2024-01-01T10:00:00",
    "message": "File uploaded successfully"
  }
}
```

### 2. Upload House Images
```http
POST /api/files/upload/house-images
Content-Type: multipart/form-data

Form Data:
- files: [image file 1]
- files: [image file 2]
- files: [image file 3]
```

**Response:**
```json
{
  "code": "10",
  "message": "File uploaded successfully",
  "data": [
    {
      "fileUrl": "http://localhost:8080/api/files/house-image/def456.jpg",
      "originalFilename": "house1.jpg",
      "storedFilename": "def456.jpg",
      "fileSize": 2048000,
      "contentType": "image/jpeg",
      "uploadType": "house-image",
      "uploadedAt": "2024-01-01T10:00:00",
      "message": "File uploaded successfully"
    }
  ]
}
```

### 3. Upload Proof of Ownership
```http
POST /api/files/upload/proof-of-ownership
Content-Type: multipart/form-data

Form Data:
- file: [image or pdf file]
```

**Response:**
```json
{
  "code": "10",
  "message": "File uploaded successfully",
  "data": {
    "fileUrl": "http://localhost:8080/api/files/proof-of-ownership/ghi789.pdf",
    "originalFilename": "ownership.pdf",
    "storedFilename": "ghi789.pdf",
    "fileSize": 512000,
    "contentType": "application/pdf",
    "uploadType": "proof-of-ownership",
    "uploadedAt": "2024-01-01T10:00:00",
    "message": "File uploaded successfully"
  }
}
```

### 4. Generic Upload
```http
POST /api/files/upload
Content-Type: multipart/form-data

Form Data:
- file: [file]
- uploadType: "avatar" | "house-image" | "thumbnail" | "proof-of-ownership"
```

### 5. Delete File
```http
DELETE /api/files/delete?fileUrl=http://localhost:8080/api/files/avatar/abc123.jpg
```

## Cách sử dụng trong Frontend

### JavaScript/TypeScript Example

```javascript
// Upload Avatar
async function uploadAvatar(file) {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch('/api/files/upload/avatar', {
    method: 'POST',
    body: formData
  });

  const result = await response.json();
  
  if (result.code === '10') {
    // Update user avatar URL
    const avatarUrl = result.data.fileUrl;
    updateUserAvatar(avatarUrl);
  }
}

// Upload House Images
async function uploadHouseImages(files) {
  const formData = new FormData();
  
  for (let file of files) {
    formData.append('files', file);
  }

  const response = await fetch('/api/files/upload/house-images', {
    method: 'POST',
    body: formData
  });

  const result = await response.json();
  
  if (result.code === '10') {
    // Get all uploaded image URLs
    const imageUrls = result.data.map(item => item.fileUrl);
    updateHouseImages(imageUrls);
  }
}

// Upload Proof of Ownership
async function uploadProofOfOwnership(file) {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch('/api/files/upload/proof-of-ownership', {
    method: 'POST',
    body: formData
  });

  const result = await response.json();
  
  if (result.code === '10') {
    const documentUrl = result.data.fileUrl;
    updateProofOfOwnership(documentUrl);
  }
}
```

### React Example

```jsx
import React, { useState } from 'react';

const FileUpload = () => {
  const [uploading, setUploading] = useState(false);

  const handleAvatarUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    setUploading(true);
    
    try {
      const formData = new FormData();
      formData.append('file', file);

      const response = await fetch('/api/files/upload/avatar', {
        method: 'POST',
        body: formData
      });

      const result = await response.json();
      
      if (result.code === '10') {
        // Update user state with new avatar URL
        setUserAvatar(result.data.fileUrl);
      }
    } catch (error) {
      console.error('Upload failed:', error);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <input
        type="file"
        accept="image/*"
        onChange={handleAvatarUpload}
        disabled={uploading}
      />
      {uploading && <p>Uploading...</p>}
    </div>
  );
};
```

## Tích hợp với Entities

### 1. User Avatar
```javascript
// Sau khi upload thành công
const avatarUrl = result.data.fileUrl;

// Update user profile
await updateUserProfile({
  ...userData,
  avatarUrl: avatarUrl
});
```

### 2. House Images
```javascript
// Sau khi upload thành công
const imageUrls = result.data.map(item => item.fileUrl);

// Create house images
for (let imageUrl of imageUrls) {
  await createHouseImage({
    houseId: houseId,
    imageUrl: imageUrl,
    sortOrder: sortOrder++
  });
}
```

### 3. Proof of Ownership
```javascript
// Sau khi upload thành công
const documentUrl = result.data.fileUrl;

// Update house renter profile
await updateHouseRenterProfile({
  ...renterData,
  proofOfOwnershipUrl: documentUrl
});
```

## File Storage Structure

```
uploads/
├── avatar/                    # User avatars (200x200)
│   ├── abc123.jpg
│   └── def456.png
├── house-image/              # House images (800x600)
│   ├── ghi789.jpg
│   └── jkl012.png
├── thumbnail/                # Thumbnails (150x150)
│   └── mno345.jpg
└── proof-of-ownership/       # Documents (original size)
    ├── pqr678.pdf
    └── stu901.jpg
```

## Validation Rules

- **File Size**: Tối đa 10MB
- **Image Types**: jpg, jpeg, png, gif, webp
- **Document Types**: jpg, jpeg, png, gif, webp, pdf
- **Image Processing**: Tự động resize theo loại upload

## Error Handling

```javascript
try {
  const response = await fetch('/api/files/upload/avatar', {
    method: 'POST',
    body: formData
  });

  const result = await response.json();
  
  if (result.code === '10') {
    // Success
    console.log('Upload successful:', result.data.fileUrl);
  } else {
    // Error
    console.error('Upload failed:', result.message);
  }
} catch (error) {
  console.error('Network error:', error);
}
```

## Security Notes

- ✅ File validation (type, size)
- ✅ Unique filename generation
- ✅ Secure file serving
- ✅ Public access to uploaded files
- ✅ File deletion capability

## Production Considerations

1. **CDN**: Sử dụng CDN cho file serving
2. **Cloud Storage**: Chuyển sang AWS S3, Google Cloud Storage
3. **Image Optimization**: Thêm WebP conversion
4. **Backup**: Backup strategy cho uploaded files
