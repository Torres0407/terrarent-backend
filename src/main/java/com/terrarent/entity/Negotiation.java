package com.terrarent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "negotiations")
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", unique = true, nullable = false)
    private Application application;

    @Column(name = "offer_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal offerAmount;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NegotiationStatus status;

    @CreationTimestamp
    @Column(name = "offered_at", updatable = false)
    private LocalDateTime offeredAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum NegotiationStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        WITHDRAWN
    }
}
