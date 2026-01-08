package service.admin;

import lombok.RequiredArgsConstructor;
import model.entity.Property;
import model.entity.User;
import model.response.AuthenticationResponse;
import model.response.BookingResponse;
import model.response.UserResponse;
import model.response.property.PropertyResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.BookingRepository;
import repository.PropertyRepository;
import repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;

    public Page<AuthenticationResponse.UserResponse> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(UserResponse::fromEntity);
    }

    public void verifyUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    public void suspendUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);

        if (user.getRole() == Role.LANDLORD) {
            propertyRepository.unlistAllByOwner(user);
        }
    }

    public Page<PropertyResponse> getAllProperties(int page, int size) {
        return propertyRepository.findAll(PageRequest.of(page, size))
                .map(PropertyResponse::fromEntity);
    }

    public void approveProperty(String propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow();
        if (property.getStatus() != PropertyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Invalid property state");
        }
        property.setStatus(PropertyStatus.LIVE);
        propertyRepository.save(property);
    }

    public void rejectProperty(String propertyId, String reason) {
        Property property = propertyRepository.findById(propertyId).orElseThrow();
        property.setStatus(PropertyStatus.REJECTED);
        property.setAdminNote(reason);
        propertyRepository.save(property);
    }

    public Page<BookingResponse> getAllBookings(int page, int size) {
        return bookingRepository.findAll(PageRequest.of(page, size))
                .map(BookingResponse::fromEntity);
    }
}
