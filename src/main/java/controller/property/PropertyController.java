package controller.property;

import lombok.RequiredArgsConstructor;
import model.request.property.PropertyRequest;
import model.response.property.PropertyResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.property.PropertyService;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<Page<PropertyResponse>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(propertyService.getAllProperties(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<PropertyResponse> createProperty(@RequestBody PropertyRequest request,
                                                           @RequestParam Long landlordId) {
        return ResponseEntity.ok(propertyService.createProperty(request, landlordId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long id,
                                                           @RequestBody PropertyRequest request,
                                                           @RequestParam Long landlordId) {
        return ResponseEntity.ok(propertyService.updateProperty(id, request, landlordId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id,
                                               @RequestParam Long landlordId) {
        propertyService.deleteProperty(id, landlordId);
        return ResponseEntity.noContent().build();
    }
}