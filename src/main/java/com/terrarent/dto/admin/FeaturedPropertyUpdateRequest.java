package com.terrarent.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating the list of featured properties by Admin")
public class FeaturedPropertyUpdateRequest {
    @NotEmpty(message = "List of property IDs cannot be empty")
    @Schema(description = "List of property IDs to be featured, in desired order", example = "[\"UUID1\", \"UUID2\", \"UUID3\"]")
    private List<UUID> propertyIds;
}
