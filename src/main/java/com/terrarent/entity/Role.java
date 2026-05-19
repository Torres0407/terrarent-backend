package com.terrarent.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.terrarent.entity.Role.RoleName.ROLE_LANDLORD;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    @Enumerated(EnumType.STRING) // Store enum as string
    private RoleName name;

    @Column(name = "description")
    private String description;

    // Enum for role names
    public enum RoleName {
        ROLE_RENTER,
        ROLE_LANDLORD,
        ROLE_ADMIN;

        @JsonCreator
        public static RoleName fromString(String value) {
            if (value == null) return null;
            return switch (value.toUpperCase()) {
                case "RENTER", "ROLE_RENTER" -> ROLE_RENTER;
                case "LANDLORD", "ROLE_LANDLORD" -> ROLE_LANDLORD;
                case "ADMIN", "ROLE_ADMIN" -> ROLE_ADMIN;
                default -> throw new IllegalArgumentException("Invalid role: " + value);
            };
        }
    }
}
