package service.property;

import exception.ForbiddenException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import model.entity.Property;
import model.request.property.PropertyRequest;
import model.response.property.PropertyResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import repository.PropertyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public Page<PropertyResponse> getAllProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return propertyRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property with ID '" + id + "' not found."));
        return mapToResponse(property);
    }

    public PropertyResponse createProperty(PropertyRequest request, Long landlordId) {
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .location(request.getLocation())
                .bedrooms(request.getBedrooms())
                .amenities(request.getAmenities())
                .landlordId(landlordId)
                .build();
        return mapToResponse(propertyRepository.save(property));
    }

    public PropertyResponse updateProperty(Long id, PropertyRequest request, Long landlordId) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property with ID '" + id + "' not found."));
        if (!property.getLandlordId().equals(landlordId)) {
            throw new ForbiddenException("User does not have permission to edit this property.");
        }
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setLocation(request.getLocation());
        property.setBedrooms(request.getBedrooms());
        property.setAmenities(request.getAmenities());
        return mapToResponse(propertyRepository.save(property));
    }

    public void deleteProperty(Long id, Long landlordId) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property with ID '" + id + "' not found."));
        if (!property.getLandlordId().equals(landlordId)) {
            throw new ForbiddenException("User does not have permission to delete this property.");
        }
        propertyRepository.delete(property);
    }

    private PropertyResponse mapToResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price(property.getPrice())
                .location(property.getLocation())
                .bedrooms(property.getBedrooms())
                .amenities(property.getAmenities())
                .landlordId(property.getLandlordId())
                .build();
    }
}