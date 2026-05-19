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
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_property_id")
    private Property reportedProperty;

    @Column(name = "report_type", length = 50)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @CreationTimestamp
    @Column(name = "reported_at", updatable = false)
    private LocalDateTime reportedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin; // Admin who resolved

    public enum ReportType {
        SPAM,
        INAPPROPRIATE_CONTENT,
        FRAUD,
        OTHER
    }

    public enum ReportStatus {
        OPEN,
        IN_REVIEW,
        RESOLVED,
        REJECTED
    }
}
