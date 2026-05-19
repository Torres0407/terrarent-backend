package com.terrarent.dto.bookingcom;

import lombok.Data;

@Data
public class BookingSearchRequest {
    private String cityId; // e.g., -2140479
    private String checkin; // e.g., "2025-11-06"
    private String checkout; // e.g., "2025-11-08"
    private String bookerCountry; // e.g., "nl"
    private String platform; // e.g., "desktop"
    private int numberOfRooms;
    private int numberOfAdults;
}
