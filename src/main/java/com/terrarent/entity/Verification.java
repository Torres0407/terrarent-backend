package com.terrarent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verifications")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "document_type", length = 50)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "document_url", columnDefinition = "TEXT")
    private String documentUrl;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin; // Admin who reviewed

    public enum DocumentType {
        DRIVERS_LICENSE,
        PASSPORT,
        NATIONAL_ID,
        OTHER
    }

    public enum VerificationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
