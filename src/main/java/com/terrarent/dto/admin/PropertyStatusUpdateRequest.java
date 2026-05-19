package com.terrarent.dto.admin;

import com.terrarent.entity.Property;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating property status by Admin")
public class PropertyStatusUpdateRequest {
    @NotNull(message = "Property status is required")
    @Schema(description = "New status for the property", example = "LIVE", allowableValues = {"PENDING", "LIVE", "REJECTED", "DELETED"})
    private Property.PropertyStatus status;
}
