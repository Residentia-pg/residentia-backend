package com.residentia.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Service class for handling image uploads to Cloudinary
 */
@Slf4j
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Upload an image to Cloudinary
     * 
     * @param file The image file to upload
     * @param folder The folder path in Cloudinary (e.g., "residentia/properties")
     * @return The public URL of the uploaded image
     * @throws IOException if upload fails
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        try {
            // Generate a unique public ID
            String publicId = folder + "/" + UUID.randomUUID().toString();
            
            // Upload the file to Cloudinary
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", folder,
                            "resource_type", "image",
                            "transformation", new com.cloudinary.Transformation<>()
                                    .width(1200)
                                    .height(800)
                                    .crop("limit")
                                    .quality("auto")
                                    .fetchFormat("auto")
                    )
            );
            
            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploaded to Cloudinary: {}", imageUrl);
            return imageUrl;
            
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete an image from Cloudinary
     * 
     * @param imageUrl The URL of the image to delete
     * @return true if deletion was successful
     */
    public boolean deleteImage(String imageUrl) {
        try {
            // Extract public_id from the Cloudinary URL
            String publicId = extractPublicId(imageUrl);
            
            if (publicId == null) {
                log.warn("Could not extract public_id from URL: {}", imageUrl);
                return false;
            }
            
            @SuppressWarnings("rawtypes")
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            
            if ("ok".equals(resultStatus)) {
                log.info("Image deleted from Cloudinary: {}", publicId);
                return true;
            } else {
                log.warn("Failed to delete image from Cloudinary: {}, result: {}", publicId, resultStatus);
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error deleting image from Cloudinary: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Extract the public_id from a Cloudinary URL
     * Format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
     */
    private String extractPublicId(String imageUrl) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return null;
            }
            
            // Split the URL and extract the part after /upload/
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }
            
            // Get the public_id part (remove version and extension)
            String publicIdWithExt = parts[1];
            
            // Remove version number (e.g., v1234567890/)
            if (publicIdWithExt.matches("^v\\d+/.*")) {
                publicIdWithExt = publicIdWithExt.replaceFirst("^v\\d+/", "");
            }
            
            // Remove file extension
            int lastDot = publicIdWithExt.lastIndexOf('.');
            if (lastDot > 0) {
                return publicIdWithExt.substring(0, lastDot);
            }
            
            return publicIdWithExt;
            
        } catch (Exception e) {
            log.error("Error extracting public_id from URL: {}", imageUrl, e);
            return null;
        }
    }

    /**
     * Upload multiple images to Cloudinary
     * 
     * @param files Array of image files to upload
     * @param folder The folder path in Cloudinary
     * @return Array of public URLs of uploaded images
     */
    public String[] uploadMultipleImages(MultipartFile[] files, String folder) throws IOException {
        String[] urls = new String[files.length];
        int index = 0;
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                urls[index++] = uploadImage(file, folder);
            }
        }
        
        return urls;
    }
}
