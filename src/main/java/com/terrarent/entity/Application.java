package com.terrarent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"renter_id", "property_id"})
})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(name = "application_date", updatable = false)
    private LocalDateTime applicationDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private Negotiation negotiation;

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        WITHDRAWN
    }
}
