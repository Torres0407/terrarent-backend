package com.terrarent.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for an image associated with a property")
public class ImageResponse {
    @Schema(description = "Unique identifier of the image", example = "c1d2e3f4-a5b6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "URL of the image", example = "https://example.com/property_images/image1.jpg")
    private String url;
    @Schema(description = "True if this is the primary image for the property", example = "true")
    private boolean isPrimary;
}
