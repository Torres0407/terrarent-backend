package com.terrarent.service;

import com.terrarent.dto.admin.AdminDashboardResponse;
import com.terrarent.dto.admin.FeaturedPropertyUpdateRequest;
import com.terrarent.dto.admin.PropertyStatusUpdateRequest;
import com.terrarent.dto.admin.UserStatusUpdateRequest;
import com.terrarent.dto.admin.VerificationActionRequest;
import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.user.UserResponse;
import com.terrarent.entity.*;
import com.terrarent.repository.*;
import com.terrarent.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final VerificationRepository verificationRepository;
    private final ReportRepository reportRepository;
    private final FeaturedPropertyRepository featuredPropertyRepository;
    private final PropertyService propertyService; // To reuse property mapping

    public AdminDashboardResponse getAdminDashboardMetrics() {
        long totalUsers = userRepository.count();
        long totalProperties = propertyRepository.count();
        long pendingVerifications = verificationRepository.countByStatus(Verification.VerificationStatus.PENDING);
        long openReports = reportRepository.countByStatus(Report.ReportStatus.OPEN);

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalProperties(totalProperties)
                .pendingVerifications(pendingVerifications)
                .openReports(openReports)
                .build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserService::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUserStatus(UUID userId, UserStatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setStatus(request.getStatus());
        return UserService.mapUserToUserResponse(userRepository.save(user));
    }

    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(propertyService::mapPropertyToPropertyResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PropertyResponse updatePropertyStatus(UUID propertyId, PropertyStatusUpdateRequest request) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        property.setStatus(request.getStatus());
        return propertyService.mapPropertyToPropertyResponse(propertyRepository.save(property));
    }

    public List<Verification> getPendingVerifications() {
        return verificationRepository.findByStatus(Verification.VerificationStatus.PENDING);
    }

    @Transactional
    public Verification processVerification(UUID verificationId, VerificationActionRequest request, UUID adminId) {
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Verification not found with id: " + verificationId));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id: " + adminId));

        verification.setStatus(request.getAction());
        verification.setReviewedAt(LocalDateTime.now());
        verification.setAdmin(admin);

        // If approved, update user status
        if (request.getAction() == Verification.VerificationStatus.APPROVED) {
            User user = verification.getUser();
            user.setStatus(User.UserStatus.VERIFIED);
            userRepository.save(user);
        }

        return verificationRepository.save(verification);
    }

    public List<Report> getOpenReports() {
        return reportRepository.findByStatus(Report.ReportStatus.OPEN);
    }

    @Transactional
    public Report resolveReport(UUID reportId, UUID adminId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id: " + adminId));

        report.setStatus(Report.ReportStatus.RESOLVED);
        report.setResolvedAt(LocalDateTime.now());
        report.setAdmin(admin);
        return reportRepository.save(report);
    }

    public List<PropertyResponse> getFeaturedProperties() {
        // Fetch only the properties that are currently featured
        List<UUID> featuredPropertyIds = featuredPropertyRepository.findFeaturedPropertyIdsOrdered();

        // Retrieve the full Property objects based on the ordered IDs
        return featuredPropertyIds.stream()
                .map(propertyRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(propertyService::mapPropertyToPropertyResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PropertyResponse> updateFeaturedProperties(FeaturedPropertyUpdateRequest request) {
        // Clear all existing featured properties first
        featuredPropertyRepository.deleteAll();

        // Add new featured properties in the specified order
        for (int i = 0; i < request.getPropertyIds().size(); i++) {
            UUID propertyId = request.getPropertyIds().get(i);
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

            FeaturedProperty featuredProperty = FeaturedProperty.builder()
                    .property(property)
                    .orderIndex(i)
                    .build();
            featuredPropertyRepository.save(featuredProperty);
        }
        return getFeaturedProperties(); // Return the updated list
    }
}
