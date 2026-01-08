package model.entity;
import jakarta.persistence.*;
import lombok.*;
import model.constants.PropertyStatus;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private Double price;

    private String location;

    private Integer bedrooms;

    @ElementCollection
    private List<String> amenities;

    private Long landlordId;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status = PropertyStatus.PENDING_APPROVAL;

    @Column(length = 500)
    private String adminNote;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
