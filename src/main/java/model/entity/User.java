package model.entity;

import jakarta.persistence.*;
import lombok.*;
import model.constants.Role;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User  {
    @Id
    @GeneratedValue
    private UUID id;
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean verified;
    private String verificationCode;
    private String resetToken;
    private Instant resetTokenExpiry;
}
