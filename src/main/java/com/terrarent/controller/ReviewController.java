package com.terrarent.controller;

import com.terrarent.dto.review.CreateReviewRequest;
import com.terrarent.entity.Review;
import com.terrarent.service.PropertyService;
import com.terrarent.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "API for managing property reviews")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("hasAnyAuthority('ROLE_RENTER', 'ROLE_ADMIN')") // Only renters or admins can create reviews
public class ReviewController {

    private final ReviewService reviewService;
    private final PropertyService propertyService; // Used for fetching user info implicitly

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return propertyService.getUserIdByEmail(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Submit a new property review",
            description = "Allows an authenticated renter to submit a review and rating for a property.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review submitted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review data or user has already reviewed this property"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Requires RENTER or ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Property or Renter not found")
    })
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody CreateReviewRequest request) {
        UUID renterId = getCurrentUserId();
        Review newReview = reviewService.createReview(renterId, request);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
}
