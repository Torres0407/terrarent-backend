package com.terrarent.service;

import com.terrarent.dto.review.CreateReviewRequest;
import com.terrarent.entity.Property;
import com.terrarent.entity.Review;
import com.terrarent.entity.User;
import com.terrarent.exception.CustomAuthenticationException;
import com.terrarent.exception.ResourceNotFoundException;
import com.terrarent.repository.PropertyRepository;
import com.terrarent.repository.ReviewRepository;
import com.terrarent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Review createReview(UUID renterId, CreateReviewRequest request) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found with id: " + renterId));
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + request.getPropertyId()));

        // Prevent multiple reviews from the same renter for the same property
        Optional<Review> existingReview = reviewRepository.findByPropertyIdAndRenterId(request.getPropertyId(), renterId);
        if (existingReview.isPresent()) {
            throw new CustomAuthenticationException("You have already reviewed this property.");
        }

        Review review = Review.builder()
                .renter(renter)
                .property(property)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Update property's average rating and number of reviews
        updatePropertyRating(property);

        return savedReview;
    }

    private void updatePropertyRating(Property property) {
        Long totalReviews = reviewRepository.findByPropertyId(property.getId()).stream().count();
        Double sumRatings = reviewRepository.findByPropertyId(property.getId()).stream()
                .mapToDouble(Review::getRating)
                .sum();

        if (totalReviews > 0) {
            BigDecimal averageRating = BigDecimal.valueOf(sumRatings / totalReviews)
                    .setScale(2, RoundingMode.HALF_UP);
            property.setAverageRating(averageRating);
        } else {
            property.setAverageRating(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
        }
        property.setNumberOfReviews(totalReviews.intValue());
        propertyRepository.save(property);
    }
}
