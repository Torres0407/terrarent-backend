package com.terrarent.dto.property;

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
@Schema(description = "Response DTO for an amenity")
public class AmenityResponse {
    @Schema(description = "Unique identifier of the amenity", example = "b1c2d3e4-f5a6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "Name of the amenity", example = "Swimming Pool")
    private String name;
}
