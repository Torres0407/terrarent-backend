package com.terrarent.dto.property;

import com.terrarent.entity.Property;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating or updating a property")
public class PropertyRequest {
    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the property listing", example = "Spacious Downtown Apartment")
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "Detailed description of the property")
    private String description;

    @NotBlank(message = "Address is required")
    @Schema(description = "Full address of the property", example = "123 Main St, Anytown, USA")
    private String address;

    @DecimalMin(value = "0.01", message = "Nightly price must be positive")
    @Schema(description = "Nightly price of the property", example = "150.00")
    private BigDecimal nightlyPrice; // OPTIONAL

    @NotNull(message = "Annual price is required")
    @DecimalMin(value = "0.01", message = "Annual price must be positive")
    @Schema(description = "Annual price of the property", example = "18000.00")
    private BigDecimal annualPrice; // ✅ REQUIRED


    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 0, message = "Bedrooms cannot be negative")
    @Schema(description = "Number of bedrooms", example = "2")
    private Integer bedrooms;

    @NotNull(message = "Number of bathrooms is required")
    @Min(value = 0, message = "Bathrooms cannot be negative")
    @Schema(description = "Number of bathrooms", example = "1")
    private Integer bathrooms;

    @Schema(description = "Type of property", example = "APARTMENT", allowableValues = {"HOUSE", "APARTMENT", "CONDO", "TOWNHOUSE", "VILLA", "STUDIO", "OTHER"})
    private Property.PropertyType propertyType;

    @Schema(description = "Latitude coordinate of the property", example = "34.0522")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate of the property", example = "-118.2437")
    private BigDecimal longitude;

    @Schema(description = "List of image URLs for the property (previously uploaded)", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
    private List<String> imageUrls; // For initial image uploads or linking pre-uploaded images

    @Schema(description = "Set of amenity IDs to associate with the property", example = "[\"UUID1\", \"UUID2\"]")
    private Set<UUID> amenityIds; // For linking amenities
}
