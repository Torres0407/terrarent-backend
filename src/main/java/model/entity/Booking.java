package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import model.constants.BookingStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propertyId;

    private String renterId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int numberOfGuests;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    private String paymentMethod;

    private String paymentToken;

    private OffsetDateTime createdAt = OffsetDateTime.now();

   }
