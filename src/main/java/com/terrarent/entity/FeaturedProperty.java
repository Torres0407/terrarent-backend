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
@Table(name = "featured_properties", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"property_id"})
})
public class FeaturedProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", unique = true, nullable = false)
    private Property property;

    @CreationTimestamp
    @Column(name = "featured_at", updatable = false)
    private LocalDateTime featuredAt;

    @Column(name = "order_index")
    private Integer orderIndex;
}
