package com.terrarent.service;

import com.terrarent.entity.Property;
import com.terrarent.entity.PropertyImage;
import com.terrarent.exception.ResourceNotFoundException;
import com.terrarent.repository.PropertyImageRepository;
import com.terrarent.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;

    @Transactional
    public String uploadPropertyImage(UUID propertyId, MultipartFile file) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        // In a real application, upload the file to cloud storage (e.g., AWS S3, Google Cloud Storage)
        // and get the URL. For this example, we'll simulate a URL.
        String imageUrl = simulateFileUpload(file);

        PropertyImage propertyImage = PropertyImage.builder()
                .property(property)
                .imageUrl(imageUrl)
                .isPrimary(property.getImages() == null || property.getImages().isEmpty()) // First image uploaded is primary
                .build();

        propertyImageRepository.save(propertyImage);

        return imageUrl;
    }

    public List<PropertyImage> getPropertyImages(UUID propertyId) {
        return propertyImageRepository.findByPropertyId(propertyId);
    }

    // This is a placeholder for actual file upload logic
    private String simulateFileUpload(MultipartFile file) {
        try {
            // Simulate saving to a path or getting a URL from a cloud service
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            log.info("Simulating file upload for: {} -> {}", originalFileName, uniqueFileName);
            // Example: return "https://your-cdn.com/images/" + uniqueFileName;
            return "/uploads/images/" + uniqueFileName;
        } catch (Exception e) {
            log.error("Failed to simulate file upload", e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
