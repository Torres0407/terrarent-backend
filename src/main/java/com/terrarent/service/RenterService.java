package com.terrarent.service;

import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.renter.NegotiationRequest;
import com.terrarent.dto.renter.RenterDashboardResponse;
import com.terrarent.dto.renter.RentalApplicationRequest;
import com.terrarent.dto.renter.TourRequest;
import com.terrarent.entity.*;
import com.terrarent.repository.*;
import com.terrarent.exception.CustomAuthenticationException;
import com.terrarent.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RenterService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final FavoriteRepository favoriteRepository;
    private final ApplicationRepository applicationRepository;
    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final NegotiationRepository negotiationRepository;
    private final PropertyService propertyService; // Reuse propertyService for mapping

    public RenterDashboardResponse getRenterDashboardMetrics(UUID renterId) {

        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Renter not found with id: " + renterId
                ));

        long savedPropertiesCount = favoriteRepository.countByRenterId(renterId);
        long bookingsCount = bookingRepository.countByRenterIdAndStatus(
                renterId, Booking.BookingStatus.CONFIRMED
        );
        long applicationsCount = applicationRepository.countByRenterId(renterId);
        long toursCount = tourRepository.countByRenterId(renterId);

        return RenterDashboardResponse.builder()
                // ✅ user data
                .userId(renter.getId())
                .firstName(renter.getFirstName())
                .lastName(renter.getLastName())
                .email(renter.getEmail())

                // ✅ metrics
                .savedPropertiesCount(savedPropertiesCount)
                .bookingsCount(bookingsCount)
                .applicationsCount(applicationsCount)
                .toursCount(toursCount)
                .build();
    }


    public List<PropertyResponse> getSavedProperties(UUID renterId) {
        return favoriteRepository.findByRenterId(renterId).stream()
                .map(Favorite::getProperty)
                .map(propertyService::mapPropertyToPropertyResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveProperty(UUID renterId, UUID propertyId) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found with id: " + renterId));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        if (favoriteRepository.existsByRenterIdAndPropertyId(renterId, propertyId)) {
            throw new CustomAuthenticationException("Property already saved by this renter.");
        }

        Favorite favorite = Favorite.builder()
                .renter(renter)
                .property(property)
                .build();
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void unsaveProperty(UUID renterId, UUID propertyId) {
        Favorite favorite = favoriteRepository.findByRenterIdAndPropertyId(renterId, propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved property not found."));
        favoriteRepository.delete(favorite);
    }

    @Transactional
    public Application createApplication(UUID renterId, RentalApplicationRequest request) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found with id: " + renterId));
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + request.getPropertyId()));

        if (applicationRepository.findByRenterIdAndPropertyId(renterId, request.getPropertyId()).isPresent()) {
            throw new CustomAuthenticationException("Application already exists for this property by this renter.");
        }

        Application application = Application.builder()
                .renter(renter)
                .property(property)
                .status(Application.ApplicationStatus.PENDING)
                .build();
        return applicationRepository.save(application);
    }

    @Transactional
    public Booking createBooking(UUID renterId, UUID propertyId) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found with id: " + renterId));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        // Additional logic like checking availability would go here
        // For now, let's assume a booking creates a placeholder for a booking on a generic future date.
        Booking booking = Booking.builder()
                .renter(renter)
                .property(property)
                .bookingDate(java.time.LocalDate.now().plusDays(7)) // Example: Book 7 days from now
                .status(Booking.BookingStatus.PENDING)
                .build();
        return bookingRepository.save(booking);
    }

    @Transactional
    public Tour scheduleTour(UUID renterId, TourRequest request) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found with id: " + renterId));
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + request.getPropertyId()));

        if (tourRepository.findByRenterIdAndPropertyIdAndTourDate(renterId, request.getPropertyId(), request.getTourDate()).isPresent()) {
            throw new CustomAuthenticationException("A tour is already scheduled for this property at this time.");
        }

        Tour tour = Tour.builder()
                .renter(renter)
                .property(property)
                .tourDate(request.getTourDate())
                .status(Tour.TourStatus.PENDING)
                .build();
        return tourRepository.save(tour);
    }

    @Transactional
    public Negotiation makeNegotiationOffer(UUID renterId, UUID applicationId, NegotiationRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        if (!application.getRenter().getId().equals(renterId)) {
            throw new CustomAuthenticationException("Renter is not authorized to make an offer on this application.");
        }

        // Check if an active negotiation already exists for this application
        if (negotiationRepository.findByApplicationId(applicationId).isPresent()) {
            throw new CustomAuthenticationException("An active negotiation already exists for this application.");
        }

        Negotiation negotiation = Negotiation.builder()
                .application(application)
                .offerAmount(request.getOffer())
                .status(Negotiation.NegotiationStatus.PENDING)
                .build();
        return negotiationRepository.save(negotiation);
    }
}
