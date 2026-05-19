package com.terrarent.controller;

import com.terrarent.dto.property.AmenityResponse;
import com.terrarent.repository.AmenityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
@Tag(name = "Amenities", description = "API for managing property amenities")
public class AmenityController {

    private final AmenityRepository amenityRepository;

    @Operation(summary = "Get all available amenities",
            description = "Retrieves a list of all predefined amenities available for properties.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved amenities",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AmenityResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<AmenityResponse>> getAllAmenities() {
        List<AmenityResponse> amenities = amenityRepository.findAll().stream()
                .map(amenity -> AmenityResponse.builder().id(amenity.getId()).name(amenity.getName()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(amenities);
    }
}
